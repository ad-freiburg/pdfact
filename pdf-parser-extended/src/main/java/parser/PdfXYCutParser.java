package parser;

import static model.SweepDirection.HorizontalSweepDirection.BOTTOM_TO_TOP;
import static model.SweepDirection.HorizontalSweepDirection.TOP_TO_BOTTOM;
import static model.SweepDirection.VerticalSweepDirection.LEFT_TO_RIGHT;
import static model.SweepDirection.VerticalSweepDirection.RIGHT_TO_LEFT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.text.StringUtils;
import model.Comparators;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfElement;
import model.PdfNonTextParagraph;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;
import model.PdfXYCutArea;
import model.PdfXYCutNonTextParagraph;
import model.PdfXYCutTextLine;
import model.PdfXYCutTextParagraph;
import model.PdfXYCutWord;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;
import rules.BlockifyNonTextPageRule;
import rules.BlockifyRule;
import rules.BlockifyTextBlockRule;
import rules.BlockifyTextLineRule;
import rules.BlockifyTextPageRule;
import rules.ParagraphifyRule;

/**
 * Implementation of a PdfAnalyzer mainly based on the xy-cut algorithm.
 *
 * @author Claudius Korzen
 */
public class PdfXYCutParser implements PdfExtendedParser {
  /**
   * The rule to blockify a page into text blocks.
   */
  BlockifyTextPageRule blockifyPageRule = new BlockifyTextPageRule();

  /**
   * The rule to blockify a page into non text blocks.
   */
  BlockifyNonTextPageRule blockifyNonTextPageRule =
      new BlockifyNonTextPageRule();

  /**
   * The rule to blockify a text block into text lines.
   */
  BlockifyTextBlockRule blockifyBlockRule = new BlockifyTextBlockRule();

  /**
   * The rule to blockify a text line into words.
   */
  BlockifyTextLineRule blockifyLineRule = new BlockifyTextLineRule();
  
  // ___________________________________________________________________________

  @Override
  public PdfDocument parse(PdfDocument document) {
    if (document == null) {
      return document;
    }

    if (document.getPages() == null) {
      return document;
    }

    for (PdfPage page : document.getPages()) {
      List<PdfArea> textBlocks = identifyTextBlocks(page);
      page.setBlocks(textBlocks);
      
      List<PdfNonTextParagraph> nonTextParas = identifyNonTextParagraphs(page);
      page.setNonTextParagraphs(nonTextParas);
      
      for (PdfArea textBlock : textBlocks) {
        textBlock.setTextLines(identifyLines(textBlock));
        textBlock.setWords(identifyWords(textBlock));
        // textBlock.setParagraphs(identifyParagraphs(textBlock));
        
        page.addTextLines(textBlock.getTextLines());
        page.addWords(textBlock.getWords());
        // page.addParagraphs(textBlock.getParagraphs());
      }
    }
    
    for (PdfPage page : document.getPages()) {      
      for (PdfArea textBlock : page.getBlocks()) {
        page.addParagraphs(identifyParagraphs(textBlock));
      }
    }
    
    // TODO: Dehyphenize only after analyzing.
    dehyphenize(document);

    return document;
  }

  // ___________________________________________________________________________
  // Identification methods.

  /**
   * Identifies text blocks from text characters in the given page.
   */
  protected List<PdfArea> identifyTextBlocks(PdfPage page) {
    PdfArea area = new PdfXYCutArea(page, page.getTextCharacters());
    return blockify(area, this.blockifyPageRule);
  }

  /**
   * Identifies non text paragraphs from non text elements in the given page.
   */
  protected List<PdfNonTextParagraph> identifyNonTextParagraphs(PdfPage page) {
    PdfXYCutArea area = new PdfXYCutArea(page, page.getNonTextElements());
    List<PdfArea> paraAreas = blockify(area, this.blockifyNonTextPageRule);
    return toNonTextParagraphs(page, paraAreas);
  }

  /**
   * Identifies text lines in the given list of blocks.
   */
  protected List<PdfXYCutTextLine> identifyLines(PdfArea textBlock) {
    List<PdfArea> lineAreas = blockify(textBlock, this.blockifyBlockRule);
    return new ArrayList<PdfXYCutTextLine>(toTextLines(lineAreas));
  }
  
  /**
   * Identifies words in the given list of text lines. This method will add the
   * words to the correspondent line and returns a list of all identified words
   * in the lines.
   */
  protected List<PdfXYCutWord> identifyWords(PdfArea textBlock) {
    List<PdfXYCutWord> words = new ArrayList<>();
    PdfTextLine prevLine = null;
    
    List<PdfTextLine> lines = textBlock.getTextLines();
    
    for (PdfTextLine line : lines) {
      List<PdfArea> wordAreas = blockify(line, this.blockifyLineRule);

      // Sort the words and the characters in the words by x-value.
      Collections.sort(wordAreas, new Comparators.MinXComparator());
      for (PdfArea area : wordAreas) {
        Collections.sort(area.getElements(), new Comparators.MinXComparator());
      }

      List<PdfXYCutWord> lineWords = toWords(wordAreas, prevLine);

      words.addAll(lineWords);
      line.setWords(lineWords);
      prevLine = line;
    }
    
    textBlock.setWords(words);
    
    return words;
  }

//  /**
//   * Identifies words in the given list of text lines. This method will add the
//   * words to the correspondent line and returns a list of all identified words
//   * in the lines.
//   */
//  protected List<PdfXYCutWord> identifyWords(PdfPage page,
//      List<PdfXYCutTextLine> lines) {
//    List<PdfXYCutWord> words = new ArrayList<>();
//    PdfTextLine prevLine = null;
//
//    
//    for (PdfXYCutTextLine line : lines) {
//      List<PdfCharacter> characters = line.getTextCharacters();
//      // Sort the words and the characters in the words by x-value.
//      Collections.sort(characters, new Comparators.MinXComparator());
//      
//      Map<Float, Float> map = new HashMap<>();
//      
//      for (int i = 0; i < characters.size(); i++) {
//        PdfCharacter prevCharacter = i > 0 ? characters.get(i - 1) : null;
//        PdfCharacter character = characters.get(i);
//        PdfCharacter nextCharacter = i < characters.size() - 1 ? characters.get(i + 1) : null;
//        
//        float leftMargin = prevCharacter != null ? (character.getRectangle().getMinX() - prevCharacter.getRectangle().getMaxX()) : 0;
//        float rightMargin = nextCharacter != null ? (nextCharacter.getRectangle().getMinX() - character.getRectangle().getMaxX()) : 0;
//        
//        leftMargin = MathUtils.round(leftMargin, 1);
//        rightMargin = MathUtils.round(rightMargin, 1);
//        
//        if (rightMargin > 0 && rightMargin > leftMargin + 1) {
//          if (map.containsKey(rightMargin)) {
//            map.put(rightMargin, map.get(rightMargin) + 1f);
//          } else {
//            map.put(rightMargin, 1f);
//          }
//        }
//      }
//      
//      float mostCommonWhitespaceWidth = 1f;
//      float mostCommonWhitespaceWidthOcc = 0;
//      
//      for (Entry<Float, Float> entry : map.entrySet()) {
//        if (entry.getValue() > mostCommonWhitespaceWidthOcc) {
//          mostCommonWhitespaceWidth = entry.getKey();
//          mostCommonWhitespaceWidthOcc = entry.getValue();
//        }
//      }
//      
//      List<PdfXYCutArea> wordAreas = new ArrayList<>();
//      PdfXYCutArea wordArea = new PdfXYCutArea(page);
//      for (int i = 0; i < characters.size(); i++) {
//        PdfCharacter character = characters.get(i);
//        PdfCharacter nextCharacter = i < characters.size() - 1 ? characters.get(i + 1) : null;
//        
//        float rightMargin = nextCharacter != null ? (nextCharacter.getRectangle().getMinX() - character.getRectangle().getMaxX()) : 0;
//                
//        wordArea.addAnyElement(character);
//        
////        System.out.println(character + " " + rightMargin + " " + mostCommonWhitespaceWidth);
//        
//        if (MathUtils.isEqual(rightMargin, mostCommonWhitespaceWidth, 0.5f)
//            || MathUtils.isLarger(rightMargin, mostCommonWhitespaceWidth, 0.5f)) {
//          wordAreas.add(wordArea);
//          wordArea = new PdfXYCutArea(page);
//        }
//      }
//      
//      wordAreas.add(wordArea);
//      
//      List<PdfXYCutWord> lineWords = toWords(page, wordAreas, prevLine);
//
//      words.addAll(lineWords);
//      line.setWords(lineWords);
//      prevLine = line;
//    }
//    return words;
//  }
  
  /**
   * Identifies paragraphs from the given list of text lines.
   */
  protected List<PdfXYCutTextParagraph> identifyParagraphs(PdfArea block) {    
    List<PdfXYCutTextParagraph> paragraphs = new ArrayList<>();
    PdfXYCutTextParagraph paragraph = null;

    List<PdfTextLine> lines = block.getTextLines();
        
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

      PdfPage page = line.getPage();
      
      if (paragraph == null) {
        paragraph = new PdfXYCutTextParagraph(page);
      }
      
      if (ParagraphifyRule.introducesNewParagraph(block, paragraph, prevLine,
          line, nextLine)) {
        paragraphs.add(paragraph);
        paragraph = new PdfXYCutTextParagraph(page);
      }
      paragraph.addTextLine(line);
    }
    paragraphs.add(paragraph);

    block.setParagraphs(paragraphs);
    
    
    return paragraphs;
  }

  // ___________________________________________________________________________
  // Blockify methods.

  /**
   * Splits the given area into smaller areas, correspondent to the given
   * blockify rules.
   */
  protected List<PdfArea> blockify(PdfArea area, BlockifyRule rule) {
    List<PdfArea> subareas = new ArrayList<>();
    blockify(area, rule, subareas);

    return subareas;
  }

  /**
   * Splits the given page area into blocks recursively, correspondent to the
   * given rule.
   */
  protected void blockify(PdfArea area, BlockifyRule rule,
      List<PdfArea> result) {
    // Try to split the area vertically.
    List<PdfArea> areas = splitVertically(area, rule);

    if (areas.isEmpty()) {
      // The area couldn't be separated vertically. Try it horizontally.
      areas = splitHorizontally(area, rule);

      if (areas.isEmpty()) {
        // The area couldn't also be separated horizontally.
        // Add the area to result.
        result.add(area);
      } else {
        // The area was split horizontally.
        for (PdfArea subarea : areas) {
          blockify(subarea, rule, result);
        }
      }
    } else {
      // The area was split vertically.
      for (PdfArea subarea : areas) {
        blockify(subarea, rule, result);
      }
    }
  }

  // ___________________________________________________________________________

  /**
   * Tries to split the given area vertically.
   */
  protected List<PdfArea> splitVertically(PdfArea area, 
      BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyVerticalLane(area, rule);

      if (lane != null) {
        List<Rectangle> subRects = rect.splitVertically(lane.getXMidpoint());
        for (Rectangle subRect : subRects) {
          result.add(new PdfXYCutArea(area, subRect));
        }
      }
    }
    return result;
  }

  int i = 0;
  
  /**
   * Tries to split the given area horizontally.
   */
  protected List<PdfArea> splitHorizontally(PdfArea area, 
      BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyHorizontalLane(area, rule);
      
      if (lane != null) {        
        List<Rectangle> subRects = rect.splitHorizontally(lane.getYMidpoint());
        for (Rectangle subRect : subRects) {
          result.add(new PdfXYCutArea(area, subRect));
        }
      }
    }
    return result;
  }

  // ___________________________________________________________________________

  /**
   * Identifies a vertical lane in the given area.
   */
  protected Rectangle identifyVerticalLane(PdfArea area, BlockifyRule rule) {
    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Determine the sweep direction.
      VerticalSweepDirection dir = rule.getVerticalLaneSweepDirection();
      // Determine the width of vertical lane to sweep.
      float ruleLaneWidth = rule.getVerticalLaneWidth(area);
      float lMinY = rect.getMinY();
      float lMaxY = rect.getMaxY();
      float lMinX = rect.getMinX();
      float lMaxX = rect.getMinX() + ruleLaneWidth;
      if (dir == RIGHT_TO_LEFT) {
        // Adjust the x values, if sweep direction is from right to left.
        lMinX = (rect.getMaxX() - ruleLaneWidth);
        lMaxX = rect.getMaxX();
      }

      // We don't want to simply find the first valid lane (in relation
      // to the given rules), but try to expand the lane as far as possible.
      // The ruleLane corresponds to the values given by the rules:
      Rectangle ruleLane = new SimpleRectangle(lMinX, lMinY, lMaxX, lMaxY);
      // The lane corresponds to the the "expanded" lane (the lane to return).
      Rectangle lane = new SimpleRectangle(Float.MAX_VALUE, lMinY,
          -Float.MAX_VALUE, lMaxY);

      float pos = lMinX;
      // TODO: Do we really need this flag?
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinX() && pos <= rect.getMaxX() - ruleLaneWidth) {
        ruleLane.moveTo(pos, ruleLane.getMinY());

        if (rule.isValidVerticalLane(area, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinX(Math.min(lane.getMinX(), ruleLane.getMinX()));
            lane.setMaxX(Math.max(lane.getMaxX(), ruleLane.getMaxX()));
          }
        } else {
          if (lane.getWidth() >= ruleLaneWidth) {
            return lane;
          }

          lane.setMinX(Float.MAX_VALUE);
          lane.setMaxX(-Float.MAX_VALUE);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == LEFT_TO_RIGHT) ? (pos + .5f) : (pos - .5f);
      }
    }
    return null;
  }

  /**
   * Identifies a horizontal lane in the given area.
   */
  protected Rectangle identifyHorizontalLane(PdfArea area, BlockifyRule rule) {
    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Determine the sweep direction.
      HorizontalSweepDirection dir = rule.getHorizontalLaneSweepDirection();
      // Determine the width of vertical lane to sweep.
      float ruleLaneHeight = rule.getHorizontalLaneHeight(area);

      float lMinX = rect.getMinX();
      float lMaxX = rect.getMaxX();
      float lMinY = rect.getMaxY() - ruleLaneHeight;
      float lMaxY = rect.getMaxY();
      if (dir == BOTTOM_TO_TOP) {
        lMinY = rect.getMinY();
        lMaxY = rect.getMinY() + ruleLaneHeight;
      }

      // We don't want to simply find the first valid lane (in relation
      // to the given rules), but try to expand the lane as far as possible.
      // The ruleLane corresponds to the values given by the rules:
      Rectangle ruleLane = new SimpleRectangle(lMinX, lMinY, lMaxX, lMaxY);
      // The lane corresponds to the the "expanded" lane (the lane to return).
      Rectangle lane = new SimpleRectangle(lMinX, Float.MAX_VALUE,
          lMaxX, -Float.MAX_VALUE);

      float pos = lMinY;
      // TODO: Do we really need this flag?
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinY() && pos <= rect.getMaxY() - ruleLaneHeight) {
        ruleLane.moveTo(ruleLane.getMinX(), pos);
                
        if (rule.isValidHorizontalLane(area, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinY(Math.min(lane.getMinY(), ruleLane.getMinY()));
            lane.setMaxY(Math.max(lane.getMaxY(), ruleLane.getMaxY()));
          }
        } else {
          if (lane.getHeight() >= ruleLaneHeight) {            
            return lane;
          }

          lane.setMinY(Float.MAX_VALUE);
          lane.setMaxY(-Float.MAX_VALUE);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == TOP_TO_BOTTOM) ? (pos - .5f) : (pos + .5f);
      }
    }
    return null;
  }

  // ___________________________________________________________________________
  // Util methods.

  /**
   * Given the shapes of the textlines, this method assigns each element to
   * the best matching textline (those that has the largest overlap with the 
   * element).
   */
  protected List<PdfXYCutTextLine> toTextLines(List<PdfArea> areas) {
    List<PdfXYCutTextLine> textLines = new ArrayList<>();
    
    // Map all elements that do not perfectly match to any textline to a pair
    // that holds the best overlap ratio seen so far for this element and the
    // related textline.
    // At the end, the element will be assigned to the textline with highest
    // overlap ratio.
    Map<PdfElement, Pair<Float, PdfXYCutTextLine>> undecided = new HashMap<>();
    
    for (PdfArea area : areas) {
      Rectangle rect = area.getRawRectangle();
      PdfPage page = area.getPage();      
      
      // Create a new textline for this shape.
      PdfXYCutTextLine textLine = new PdfXYCutTextLine(page);
      textLines.add(textLine);
      
      // Obtain all elements that overlaps this shape.
      List<PdfCharacter> elements = page.getTextCharactersOverlapping(rect);
      
      for (PdfCharacter element : elements) {
        float ratio = element.getRectangle().computeOverlapRatio(rect);
        
        if (ratio < 1) {
          // The element isn't fully contained by the shape.
          float bestRatioSoFar = 0;
          if (undecided.containsKey(element)) {
            bestRatioSoFar = undecided.get(element).getLeft();
          }
          
          // Update best matching text line if necessary.
          if (ratio > bestRatioSoFar) {
            undecided.put(element, new ImmutablePair<>(ratio, textLine));
          }
        } else {
          // The element is fully contained by the shape. Assign the element.
          textLine.addAnyElement(element);
        }
      }
    }
    
    // Assign each undecided elements to the best macthing textlines.
    for (Entry<PdfElement, Pair<Float, PdfXYCutTextLine>> entry 
        : undecided.entrySet()) {
      PdfElement element = entry.getKey();
      PdfTextLine textLine = entry.getValue().getRight();
      textLine.addAnyElement(element);
    }
    
    
    return textLines;
  }

  /**
   * Transforms the given list of page areas to a list of words. The prevLine is
   * used for dehyphenation.
   */
  protected List<PdfXYCutWord> toWords(List<PdfArea> areas,
      PdfTextLine prevLine) {
    List<PdfXYCutWord> result = new ArrayList<>();

    if (areas != null) {
//      int start = 0;
//
//      // Dehyphenize.
//      if (areas.size() > 0 && prevLine != null) {
//        PdfWord lastWord = prevLine.getLastWord();
//        if (lastWord != null) {
//          PdfCharacter lastCharacter = lastWord.getLastTextCharacter();
//          if (lastCharacter != null) {
//            // TODO: Allow another dashes ("--". "---")
//            if (lastCharacter.getUnicode().equals("-")) {
//              // The last word in the previous line ends with "-". Append the
//              // elements of the first area in current line to this last word.
//              PdfXYCutArea area = areas.get(0);
//
//              lastWord.addAnyElements(area.getElements());
//
//              start = 1;
//
//              // Ignore the hyphen, if the word doesn't start with an uppercase.
//              if (area.getElements() != null && !area.getElements().isEmpty()) {
//                // Get the first character.
//                String firstCharacter = area.getElements().get(0).toString();
//                if (!Character.isUpperCase(firstCharacter.charAt(0))) {
//                  // Ignore the hyphen
//                  lastCharacter.setIgnore(true);
//                }
//              }
//            }
//          }
//        }
//      }

      for (int i = 0; i < areas.size(); i++) {
        PdfArea area = areas.get(i);
        PdfXYCutWord word = new PdfXYCutWord(area.getPage(), area);

        result.add(word);
      }
    }

    return result;
  }

  /**
   * Transforms the given list of page areas to a list of paragraphs.
   */
  protected List<PdfTextParagraph> toParagraphs(List<PdfXYCutArea> areas) {
    List<PdfTextParagraph> result = new ArrayList<>();
    for (PdfXYCutArea a : areas) {
      result.add(new PdfXYCutTextParagraph(a.getPage(), a));
    }
    return result;
  }

  /**
   * Transforms the given list of page areas to a list of non text paragraphs.
   */
  protected List<PdfNonTextParagraph> toNonTextParagraphs(PdfPage page,
      List<PdfArea> areas) {
    List<PdfNonTextParagraph> result = new ArrayList<>();
    for (PdfArea area : areas) {
      result.add(new PdfXYCutNonTextParagraph(page, area));
    }
    return result;
  }
  
  /**
   * Dehyphenizes the words in the given document, i.e. merges separated word 
   * parts (separated by a hyphen) that belong together. This method tries to 
   * distinguish between hyphens that in fact separate a belonging word and 
   * between intended hyphens (like the hyphen in "self-contained"). Intended
   * hyphens won't be dehyphenized.  
   */
  protected void dehyphenize(PdfDocument document) {
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages(); 
    if (pages == null) {
      return;
    }
    
    // To identify intended hyphens, check if there are multiple occurrences of
    // the hyphen zone in the document.
    // Count the frequencies of "hyphen zones" in the document. A hyphen zone
    // is defined as the zone around a hyphen, consisting of the whole 
    // substring of the word in front of the hyphen, the hyphen itself and a 
    // well defined number of characters of the substring after the hyphen.
    // For example, the hyphen zone of the string "quasi-ergodicity" could be 
    // "quasi-erg". This allows to identify hyphens in "quasi-ergodicity" as 
    // well as in "quasi-ergodic".
    Map<String, Integer> hyphenZoneFreqs = computeHyphenZoneFreqs(document);
    
    for (PdfPage page : document.getPages()) {
      dehyphenize(page, hyphenZoneFreqs);
    }
  }
  
  /**
   * Dehyphenizes the words in the given page. 'hyphenFreqs' contains the 
   * frequencies of the hyphen zones in the document and is needed to identify
   * intended hyphens.
   */
  protected void dehyphenize(PdfPage page, Map<String, Integer> hyphenFreqs) {
    if (page == null) {
      return;
    }
    
    List<PdfTextLine> lines = page.getTextLines();
    
    if (lines == null) {
      return;
    }
    
    PdfTextLine prevLine = null;
    for (PdfTextLine line : lines) {
      if (prevLine != null) {
        PdfWord lastWord = prevLine.getLastWord();
        if (lastWord != null) {
          PdfCharacter lastCharacter = lastWord.getLastTextCharacter();
          if (lastCharacter != null) {
            // Check, if the last line ends with an hyphen.
            // TODO: Allow another dashes ("--". "---")
            if (lastCharacter.getUnicode().equals("-")) {
              // The last line ends with a hyphen. 
              PdfWord word = line.getFirstWord();
  
              // Decide, if we have to ignore the hyphen or not.
              boolean ignoreHyphen = true;
              
              // Obtain the frequency of the hyphen zone of the word in the 
              // document.
              int hyphenZoneFreq = 0;
              String withHyphen = lastWord.getUnicode() + word.getUnicode();
              String hyphenZone = getHyphenZone(withHyphen, 3);
              if (hyphenFreqs.containsKey(hyphenZone)) {
                hyphenZoneFreq = hyphenFreqs.get(hyphenZone);
              }
              
              if (hyphenZoneFreq > 0) {
                // Don't ignore the hyphen, if there are further occurrences of 
                // the hyphen zone in the document.
                ignoreHyphen = false;
              } else {
                // Otherwise, ignore the hyphen, if the first character after
                // the hyphen isn't a upper case.
                String firstChar = word.getFirstTextCharacter().toString();
                ignoreHyphen = !Character.isUpperCase(firstChar.charAt(0));
              }
              
              // Merge the both words.
              lastWord.addAnyElements(word.getElements());
              // Ignore the word (because it was merged with the previous word)
              word.setIgnore(true);  
              // Ignore the hyphen if necessary.
              if (ignoreHyphen) {
                lastCharacter.setIgnore(true);
              }
            }
          }
        }
      }
      prevLine = line;
    }
  }    
  
  /**
   * Computes the frequencies of hyphen zones in the given document.
   */
  protected Map<String, Integer> computeHyphenZoneFreqs(PdfDocument document) {
    Map<String, Integer> hyphenZoneFreqs = new HashMap<>();
    
    if (document != null) {
      List<PdfPage> pages = document.getPages();
      if (pages != null) {
        for (PdfPage page : pages) {
          if (page == null) {
            continue;
          }
          
          List<PdfWord> words = page.getWords();
          if (words == null) {
            continue;
          }
          
          for (PdfWord word : words) {
            // Find the hyphen zone, if any.
            String hyphenZone = getHyphenZone(word.getUnicode(), 3);
            
            if (hyphenZone != null) {
              int freq = 1;
              if (hyphenZoneFreqs.containsKey(hyphenZone)) {
                freq = hyphenZoneFreqs.get(hyphenZone) + 1;
              }
              hyphenZoneFreqs.put(hyphenZone, freq);
            }
          }
        }
      }
    }
    return hyphenZoneFreqs;
  }
  
  /**
   * Computes the hyphen zone with an appendix of at most k characters. The
   * appendix is the part after the hyphen. For example the hyphen zone of
   * "self-contained" with k=3 is "self-con". 
   */
  protected String getHyphenZone(String word, int k) {
    if (word == null) {
      return null;
    }
    
    // Remove all punctuation marks (except "-").
    String normalized = StringUtils.normalize(word, '-');
    
    if (normalized == null) {
      return null;
    }
    
    String[] parts = normalized.split("-");
      
    if (parts.length == 2) {
      // Build the hyphen zone.
      StringBuilder sb = new StringBuilder();
      sb.append(parts[0]);
      sb.append("-");
      if (parts[1] != null) {
        String appendix = parts[1].trim();
        int lengthAppendix = Math.min(appendix.length(), k);
        sb.append(appendix.substring(0, lengthAppendix));
        return sb.toString();
      }
    }
    return null;
  } 
}
