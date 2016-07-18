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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleLine;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.Characters;
import model.Comparators;
import model.DimensionStatistics;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfElement;
import model.PdfNonTextParagraph;
import model.PdfPage;
import model.PdfTextAlignment;
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
import model.TextStatistics;
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
      
//      List<PdfNonTextParagraph> nonTextParas = identifyNonTextParagraphs(page);
//      page.setNonTextParagraphs(nonTextParas);
            
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
        identifyLineAlignments(textBlock);
        page.addParagraphs(identifyParagraphs(textBlock));
      }
    }

    return document;
  }

  // ___________________________________________________________________________
  // Identification methods.

  /**
   * Identifies text blocks from text characters in the given page.
   */
  protected List<PdfArea> identifyTextBlocks(PdfPage page) {
    PdfArea area = new PdfXYCutArea(page, page.getElements());
    
    area.setColumnXRange(identifyReliableXRange(area));
    
    List<PdfArea> blocks = blockify(area, this.blockifyPageRule);
    
    // TODO: Move it.
    for (PdfArea block : blocks) {
      for (PdfElement element : block.getElements()) {
        element.setBlock(block);
      }
    }
        
    return blocks;
  }

//  /**
//   * Identifies non text paragraphs from non text elements in the given page.
//   */
//  protected List<PdfNonTextParagraph> identifyNonTextParagraphs(PdfPage page) {
//    PdfXYCutArea area = new PdfXYCutArea(page, page.getNonTextElements());
//    List<PdfArea> paraAreas = blockify(area, this.blockifyNonTextPageRule);
//    List<PdfNonTextParagraph> paras = toNonTextParagraphs(page, paraAreas);
//    
//    // TODO: Move it.
//    for (PdfNonTextParagraph para : paras) {
//      for (PdfElement element : para.getElements()) {
//        element.setBlock();
//      }
//    }
//    
//    return paras;
//  }
  
  boolean b = false;
  
  /**
   * Identifies text lines in the given list of blocks.
   */
  protected List<PdfXYCutTextLine> identifyLines(PdfArea textBlock) {
    b = true;
    List<PdfArea> lineAreas = blockify(textBlock, this.blockifyBlockRule);
    b = false;
        
    List<PdfXYCutTextLine> lines =  new ArrayList<>(toTextLines(lineAreas));
    
    // TODO: Move it.
    for (int i = 0; i < lines.size(); i++) {
      PdfXYCutTextLine line = lines.get(i);
            
      line.setBlock(textBlock);
      line.setColumnXRange(textBlock.getColumnXRange());
    }
    
    return lines;
  }
  
  protected void identifyLineAlignments(PdfArea textBlock) {
    List<PdfTextLine> lines = textBlock.getTextLines();
    
    // TODO: Move it.
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;
            
      line.setAlignment(computeLineAlignment(textBlock, prevLine, line, 
          nextLine));
    }
  }
  
  protected PdfTextAlignment computeLineAlignment(PdfArea block, 
      PdfTextLine prevLine, PdfTextLine line, PdfTextLine nextLine) {
    PdfTextAlignment alignment = computeLineAlignment(block, line);
    
    if (prevLine != null) {
      Rectangle prevBoundingBox = computeBoundingBoxToConsider(prevLine);
      float prevMinX = MathUtils.round(prevBoundingBox.getMinX(), 1);
      Rectangle boundingBox = computeBoundingBoxToConsider(line);
      float minX = MathUtils.round(boundingBox.getMinX(), 1);
           
      PdfTextAlignment prevAlignment = prevLine.getAlignment();
      
      // If line.minX == prevLine.minX, take the alignment of prevLine.
      if (prevAlignment != PdfTextAlignment.CENTERED 
          && MathUtils.isEqual(prevMinX, minX, 0.1f)) {
        return prevLine.getAlignment();
      }
    }
    
    return alignment;
  }
  
  // Support formula labels like (1), (10), (100), (1.2) and (1a), (A1)
  Pattern formulaLabelPattern = Pattern.compile("\\(([A-Z])?\\d{1,3}([a-z]?)(\\.\\d{1,2})?\\)");
  
  protected PdfTextAlignment computeLineAlignment(PdfArea block,
      PdfTextLine line) {
    if (line != null) {   
      Rectangle boundingBox = computeBoundingBoxToConsider(line);
      Line columnXRange = line.getColumnXRange();
      
      if (columnXRange != null) {
        float lineMinX = boundingBox.getMinX();
        float lineMaxX = boundingBox.getMaxX();
        float columnMinX = columnXRange.getStartX();
        float columnMaxX = columnXRange.getEndX();
        
        float leftMargin = Math.max(0, lineMinX - columnMinX);
        float rightMargin = Math.max(0, columnMaxX - lineMaxX);
        
        DimensionStatistics stats = block.getDimensionStatistics();
        float tolerance = 0.5f * stats.getMostCommonWidth();
                
        if (leftMargin > tolerance && rightMargin > tolerance) {
          return PdfTextAlignment.CENTERED;
        }
        
        if (leftMargin < tolerance && rightMargin < tolerance) {
          return PdfTextAlignment.JUSTIFIED;
        }
        
        if (leftMargin > tolerance) {
          return PdfTextAlignment.RIGHT;
        }
        
        return PdfTextAlignment.LEFT;
      }
    }
    return null;
  }
  
  protected Rectangle computeBoundingBoxToConsider(PdfTextLine line) {
    List<PdfWord> words = line.getWords();   
    List<PdfWord> wordsToConsider = new ArrayList<>();
    
    if (words != null && !words.isEmpty()) {
      // Check if to consider first word.        
      PdfWord firstWord = words.get(0);
      
      // Don't consider the first word if its starts with an itemize bullet.
      if (!firstWord.getUnicode().startsWith("•")) {
        wordsToConsider.add(firstWord);
      }
    
      // Add all words in the middle.
      for (int i = 1; i < words.size() - 1; i++) {
        wordsToConsider.add(words.get(i));
      }
        
      // Check if to consider last word.
      PdfWord lastWord = words.get(words.size() - 1);
      if (lastWord != null) {
        Matcher m = formulaLabelPattern.matcher(lastWord.getUnicode());
        if (!m.matches()) {
          wordsToConsider.add(lastWord);
        }
      }
    }
    
    return SimpleRectangle.computeBoundingBox(wordsToConsider);
  }
  
  /**
   * Identifies words in the given list of text lines. This method will add the
   * words to the correspondent line and returns a list of all identified words
   * in the lines.
   */
  protected List<PdfXYCutWord> identifyWords(PdfArea textBlock) {
    List<PdfXYCutWord> words = new ArrayList<>();
    
    List<PdfTextLine> lines = textBlock.getTextLines();
    
    for (PdfTextLine line : lines) {
      List<PdfArea> wordAreas = blockify(line, this.blockifyLineRule);

      // Sort the words and the characters in the words by x-value.
      Collections.sort(wordAreas, new Comparators.MinXComparator());
      for (PdfArea area : wordAreas) {
        Collections.sort(area.getElements(), new Comparators.MinXComparator());
      }

      List<PdfXYCutWord> lineWords = toWords(wordAreas, line);

      // TODO: Move it.
      for (PdfXYCutWord word : lineWords) {
        word.setBlock(textBlock);
        word.setColumnXRange(textBlock.getColumnXRange());
      }
      
      words.addAll(lineWords);
      line.setWords(lineWords);
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
        paragraph.setBlock(block);
        paragraph.setColumnXRange(block.getColumnXRange());
      }
      
      if (ParagraphifyRule.introducesNewParagraph(block, paragraph, prevLine,
          line, nextLine)) {
        paragraphs.add(paragraph);
        paragraph = new PdfXYCutTextParagraph(page);
        paragraph.setBlock(block);
        paragraph.setColumnXRange(block.getColumnXRange());
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
        Line xRange = identifyReliableXRange(subarea);
        if (xRange != null) {
          subarea.setColumnXRange(xRange);
        }
        
        blockify(subarea, rule, result);
      }
    }
  }

  /**
   * Identifies the "reliable" range in x dimension of the given area. In 
   * general, the minX and maxX values of an area are dictated by the outermost 
   * elements. This is critical if most of the elements (lines for example) 
   * have comparable minX and maxX values but only a single line exceeds these
   * values. 
   * 
   * For example, a column could look like this:
   * 
   * This is a very long heading.
   * This is the text
   * in the column to
   * show the problem
   * ................
   * ................
   * ................
   * 
   * The first line of the column is an outlier (because its maxX value exceeds 
   * the (comparable) maxX values of all other lines. So the x-range of the 
   * column is dictated by this line. 
   * 
   * This methods tries to identify the x-range that is defined by the "most 
   * common" minX and maxX values. 
   * 
   * To be exact, this method takes the minX and maxX values that are taken by
   * at most half of the elements in this area. 
   */
  protected Line identifyReliableXRange(PdfArea area) {
    if (area == null) {
      return null;
    }
    
    Rectangle rect = area.getRectangle();
    if (rect == null) {
      return null;
    }
    
    Line xRange = new SimpleLine(rect.getLowerLeft(), rect.getLowerRight());
    
    FloatCounter minXCounter = new FloatCounter();
    FloatCounter maxXCounter = new FloatCounter();
    FloatCounter minYCounter = new FloatCounter();
    
    for (PdfElement element : area.getElements()) {
      // Count the occurrences of minX values.
      minXCounter.add(MathUtils.round(element.getRectangle().getMinX(), 1));
      // Count the occurrences of maxX values.
      maxXCounter.add(MathUtils.round(element.getRectangle().getMaxX(), 1));
      // Count the occurrences of minY values.
      minYCounter.add(MathUtils.round(element.getRectangle().getMinY(), 0));
    }
    
    int numMostFrequentMinX = minXCounter.getMostFrequentFloatCount();
    int numMostFrequentMaxX = maxXCounter.getMostFrequentFloatCount();
    int numMinYValues = minYCounter.size();
    int minXThreshold = Math.min(numMostFrequentMinX / 2, numMinYValues / 2);
    int maxXThreshold = Math.min(numMostFrequentMaxX / 2, numMinYValues / 2);
    int threshold = Math.min(minXThreshold, maxXThreshold);
    
    xRange.setStartX(minXCounter.getSmallestFloatOccuringAtLeast(threshold));
    xRange.setEndX(maxXCounter.getLargestFloatOccuringAtLeast(threshold));
        
    return xRange;
  }
  
  // ___________________________________________________________________________

  /**
   * Tries to split the given area vertically.
   */
  public List<PdfArea> splitVertically(PdfArea area, 
      BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyVerticalLane(area, rule);

      if (lane != null) {
        List<Rectangle> subRects = rect.splitVertically(lane.getXMidpoint());
        for (Rectangle subRect : subRects) {
          List<PdfElement> subs = area.getElementsOverlapping(subRect, 0.75f);
          if (!subs.isEmpty()) {            
            PdfXYCutArea subarea = new PdfXYCutArea(area, subs);
            subarea.setColumnXRange(area.getColumnXRange());
            subarea.setRawRectangle(subRect);
            result.add(subarea);
          }
        }
      }
    }
    
    // Return the result list only if there are at least 2 subareas.
    if (result.size() > 1) {
      return result;
    } else {
      return new ArrayList<>();
    }
  }
  
  /**
   * Tries to split the given area horizontally.
   */
  public List<PdfArea> splitHorizontally(PdfArea area, 
      BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyHorizontalLane(area, rule);
      
      if (lane != null) {        
        List<Rectangle> subRects = rect.splitHorizontally(lane.getYMidpoint());
        for (Rectangle subRect : subRects) {
//          PdfArea subarea = new PdfXYCutArea(area, subRect);
//          subarea.setColumnXRange(area.getColumnXRange());
//          result.add(subarea);
          
          List<PdfElement> subs = area.getElementsOverlapping(subRect, 0.75f);
          if (!subs.isEmpty()) {            
            PdfXYCutArea subarea = new PdfXYCutArea(area, subs);
            subarea.setColumnXRange(area.getColumnXRange());
            subarea.setRawRectangle(subRect);            
            result.add(subarea);
          }
        }
      }
    }
    
    // Return the result list only if there are at least 2 subareas.
    if (result.size() > 1) {
      return result;
    } else {
      return new ArrayList<>();
    }
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
          if (MathUtils.isLargerOrEqual(lane.getWidth(), ruleLaneWidth, 0.01f)) {
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
          if (MathUtils.isLargerOrEqual(lane.getHeight(), ruleLaneHeight, 0.01f)) {
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
    Map<PdfElement, Pair<Float, PdfXYCutTextLine>> mappings = new HashMap<>();
    
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

        // The element isn't fully contained by the shape.
        float bestRatioSoFar = 0;
        if (mappings.containsKey(element)) {
          bestRatioSoFar = mappings.get(element).getLeft();
        }
        
        // Update best matching text line if necessary.
        if (ratio > bestRatioSoFar) {
          mappings.put(element, new ImmutablePair<>(ratio, textLine));
        }
      }
    }
    
    // Assign each undecided elements to the best macthing textlines.
    for (Entry<PdfElement, Pair<Float, PdfXYCutTextLine>> entry 
        : mappings.entrySet()) {
      PdfElement element = entry.getKey();
      PdfTextLine textLine = entry.getValue().getRight();
      textLine.addAnyElement(element);
    }
    
    // Compute baseline and meanline.
    for (PdfTextLine line : textLines) {
      computeBaseAndMeanLine(line);
    }
    
    return textLines;
  }

  /**
   * Transforms the given list of page areas to a list of words. The prevLine is
   * used for dehyphenation.
   */
  protected List<PdfXYCutWord> toWords(List<PdfArea> areas, PdfTextLine line) {
    List<PdfXYCutWord> result = new ArrayList<>();

    if (areas != null) {
      for (int i = 0; i < areas.size(); i++) {
        PdfArea area = areas.get(i);
        PdfXYCutWord word = new PdfXYCutWord(area.getPage(), area);

        for (PdfCharacter character : word.getTextCharacters()) {
          // Compute subscripts and superscripts.
          if (Characters.isPunctuationMark(character)) {
            character.setIsPunctuationMark(true);
            word.setContainsPunctuationMark(true);
          }
          if (isSubscript(line, character)) {
            character.setIsSubScript(true);
            word.setContainsSubScript(true);
          }
          if (isSuperscript(line, character)) {
            character.setIsSuperScript(true);
            word.setContainsSuperScript(true);
          }
        }
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
          
  public void computeBaseAndMeanLine(PdfTextLine line) {
    Line meanLine = null;
    FloatCounter maxYCounter = new FloatCounter();
    
    TextStatistics stats = line.getTextStatistics();
    float fontSize = MathUtils.round(stats.getMostCommonFontsize(), 1);
    
    for (PdfCharacter character : line.getTextCharacters()) {
      float charFontsize = MathUtils.round(character.getFontsize(), 1);
      if (Characters.isMeanlineCharacter(character)
          && fontSize == charFontsize) {
        float maxY = MathUtils.round(character.getRectangle().getMaxY(), 1);
        maxYCounter.add(maxY);
      }
    }
    
    if (!maxYCounter.isEmpty()) {
      float mostCommonMaxY = maxYCounter.getMostFrequentFloat();
      meanLine = new SimpleLine(line.getRectangle().getMinX(), mostCommonMaxY,
          line.getRectangle().getMaxX(), mostCommonMaxY);
    }
    
    Line baseLine = null;
    FloatCounter minYCounter = new FloatCounter();
    
    for (PdfCharacter character : line.getTextCharacters()) {
      float charFontsize = MathUtils.round(character.getFontsize(), 1);
      if (Characters.isBaselineCharacter(character)
          && fontSize == charFontsize) {
        float minY = MathUtils.round(character.getRectangle().getMinY(), 1);
        minYCounter.add(minY);
      }
    }
    
    if (!minYCounter.isEmpty()) {
      float mostCommonMinY = minYCounter.getMostFrequentFloat();
      baseLine = new SimpleLine(line.getRectangle().getMinX(), mostCommonMinY,
          line.getRectangle().getMaxX(), mostCommonMinY);
    }
    
    // Check if the baseline and meanLine are valid
    if (baseLine != null && meanLine != null) {
      if (baseLine.getStartY() < meanLine.getStartY()) {
        line.setBaseLine(baseLine);
        line.setMeanLine(meanLine);
      }
    } else {
      line.setBaseLine(baseLine);
      line.setMeanLine(meanLine);
    }
  }
      
  protected boolean isSubscript(PdfTextLine line, PdfCharacter character) {
    if (character == null) {
      return false;
    }
    
    if (Characters.isPunctuationMark(character)) {
      return false;
    }
    
    Rectangle rect = character.getRectangle();
    float minY = rect.getMinY();
    float maxY = rect.getMaxY();
    
    Line baseLine = line.getBaseLine();
    Line meanLine = line.getMeanLine();
    
    if (baseLine != null) {
      float baseLineY = baseLine.getStartY();
      boolean isBelowBaseLine = MathUtils.isSmaller(minY, baseLineY, 0.5f);
      
      if (isBelowBaseLine && Characters.isLatinLetterOrDigit(character) 
          && !Characters.isDescender(character)) {
        return true;
      }
      
      if (meanLine != null) {
        float meanLineY = meanLine.getStartY();
        boolean isBelowMeanLine = MathUtils.isSmaller(maxY, meanLineY, 0.5f);
        
        return isBelowBaseLine && isBelowMeanLine;
      }
    }
    return false;
  }
  
  protected boolean isSuperscript(PdfTextLine line, PdfCharacter character) {
    if (character == null) {
      return false;
    }
    
    if (Characters.isPunctuationMark(character)) {
      return false;
    }
    
    Rectangle rect = character.getRectangle();
    float minY = rect.getMinY();
    float maxY = rect.getMaxY();
    
    Line baseLine = line.getBaseLine();
    Line meanLine = line.getMeanLine();
    
    if (meanLine != null) {
      float meanLineY = meanLine.getStartY();
      boolean isAboveMeanLine = MathUtils.isLarger(maxY, meanLineY, 0.5f);
      
      if (isAboveMeanLine && Characters.isLatinLetterOrDigit(character) 
          && !Characters.isAscender(character)) {
        return true;
      }
      
      if (baseLine != null) {
        float baseLineY = baseLine.getStartY();
        boolean isAboveBaseLine = MathUtils.isLarger(minY, baseLineY, 0.5f);
      
        return isAboveMeanLine && isAboveBaseLine;
      }
    }
    return false;
  }
}
