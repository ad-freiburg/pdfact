package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleLine;
import model.Characters;
import model.Comparators;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfElement;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfXYCutTextLine;
import model.PdfXYCutWord;
import rules.XYColumnsCut;
import rules.XYTextlineCut;
import rules.XYWordCut;

/**
 * Implementation of a PdfExtendedParser mainly based on the xy-cut algorithm.
 *
 * @author Claudius Korzen
 */
public class NewPdfXYCutParser implements PdfExtendedParser {
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

      for (PdfArea textBlock : textBlocks) {
//        textBlock.setTextLines(identifyLines(textBlock));
//        textBlock.setWords(identifyWords(textBlock));
      }
    }
      
    List<? extends PdfArea> blocks = document.getPages().get(6).getBlocks();
    PdfArea textBlock = blocks.get(blocks.size() - 1);
    textBlock.setTextLines(identifyLines(textBlock));
    textBlock.setWords(identifyWords(textBlock));
    
    return document;
  }

  // ___________________________________________________________________________
  // Identification methods.

  /**
   * Identifies text blocks from text characters in the given page.
   */
  protected List<PdfArea> identifyTextBlocks(PdfPage page) {
    XYColumnsCut splitter = new XYColumnsCut();
    List<PdfArea> ares = splitter.split(page);

    return ares;
  }

  /**
   * Identifies text lines in the given list of blocks.
   */
  protected List<PdfXYCutTextLine> identifyLines(PdfArea textBlock) {
    XYTextlineCut cutter = new XYTextlineCut();
    List<PdfArea> lineAreas = cutter.split(textBlock);
    List<PdfXYCutTextLine> lines = new ArrayList<>(toTextLines(lineAreas));

    return lines;
  }

  /**
   * Identifies words in the given list of text lines. This method will add the
   * words to the correspondent line and returns a list of all identified words
   * in the lines.
   */
  protected List<PdfXYCutWord> identifyWords(PdfArea textBlock) {
    List<PdfXYCutWord> words = new ArrayList<>();
    
    List<PdfTextLine> lines = textBlock.getTextLines();
    XYWordCut cutter = new XYWordCut();
    
    for (PdfTextLine line : lines) {
      List<PdfArea> wordAreas = cutter.split(line);

      // Sort the words and the characters in the words by x-value.
      Collections.sort(wordAreas, new Comparators.MinXComparator());
      for (PdfArea area : wordAreas) {
        Collections.sort(area.getElements(), new Comparators.MinXComparator());
      }

      List<PdfXYCutWord> lineWords = toWords(wordAreas, line);

      // TODO: Move it.
      for (PdfXYCutWord word : lineWords) {
        word.setBlock(textBlock);
      }
      
      words.addAll(lineWords);
      line.setWords(lineWords);
    }
    
    textBlock.setWords(words);
    
    return words;
  }
  
  // ___________________________________________________________________________
  // Util methods.

  /**
   * Given the shapes of the textlines, this method assigns each element to the
   * best matching textline (those that has the largest overlap with the
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

      Collections.sort(elements, new Comparators.MinXComparator());
      System.out.println(rect);
      System.out.println(elements);
      
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
    for (Entry<PdfElement, Pair<Float, PdfXYCutTextLine>> entry : undecided
        .entrySet()) {
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

  public void computeBaseAndMeanLine(PdfTextLine line) {
    Line meanLine = null;
    FloatCounter maxYCounter = new FloatCounter();

    for (PdfCharacter character : line.getTextCharacters()) {
      if (Characters.isMeanlineCharacter(character)) {
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
      if (Characters.isBaselineCharacter(character)) {
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
