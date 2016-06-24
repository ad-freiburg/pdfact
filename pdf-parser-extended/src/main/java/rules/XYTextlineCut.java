package rules;

import java.util.Collections;
import java.util.List;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleLine;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.Characters;
import model.Comparators;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfElement;

/**
 * Splitter to split a page into blocks.
 * 
 * @author Claudius Korzen
 */
public class XYTextlineCut extends XYCut {

  public XYTextlineCut() {
    this.debug = true;
  }

  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    return Float.MAX_VALUE;
  }

  @Override
  protected float getHorizontalLaneHeight(PdfArea area) {
    return 1f;
  }

  // ___________________________________________________________________________

  @Override
  protected boolean isValidVerticalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    return false;
  }

  @Override
  protected boolean isValidHorizontalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    if (area == null || lane == null) {
      return false;
    }

//    float mostCommonFontsize = area.getTextStatistics().getMostCommonFontsize();

    // Try to decide if the given lane is a valid line separator by only
    // allowing specific elements intersecting this lane.
    List<PdfElement> overlappingElements = area.getElementsOverlapping(lane);
    
    Collections.sort(overlappingElements, new Comparators.MinXComparator());
    
    System.out.println("  " + overlappingElements);
    
    if (overlappingElements.isEmpty()) {
      return true;
    }
    
    int numAscenders = 0;
//    int numDescenders = 0;
    
    // Inspect each intersected element.
    for (PdfElement element : overlappingElements) {
      String string = element.toString();
//      float fontsize = MathUtils.round(element.getFontsize(), 1);

      // Ignore elements whose fontsize is significantly smaller than the most
      // common fontsize in the given area. This is supposed to allow sub- and
      // supscripts to intersect the lane.
      // TODO: Do we need this? Makes trouble in the header of broocoli paper
      // (because the authors are written in smaller fontsize)
      // if (MathUtils.isSmaller(fontsize, mostCommonFontsize, 0.5f)) {
      // continue;
      // }

      if (string != null && !string.isEmpty()) {
        char c = string.charAt(0);

        boolean isAscender = Characters.isAscender(c);
        boolean isDescender = Characters.isDescender(c);
        
        numAscenders += isAscender ? 1 : 0;
//        numDescenders += isDescender ? 1 : 0;
        
        // It is not allowed that characters other than descenders and ascenders
        // intersect the lane.
        if (Character.isAlphabetic(c)) {
          if (!isAscender && !isDescender) {
            return false;
          }
        }
        
        // It is not allowed that digits intersect the lane.
        if (Character.isDigit(c)) {
          return false;
        }
      }
    }
    
//    Rectangle xHeightRect = computeXHeightRectangle(area, overlappingElements);
//    
//    System.out.println("  " + xHeightRect);
//    List<PdfCharacter> chars = area.getTextCharactersOverlapping(xHeightRect);
//    System.out.println("  x-overlapping: " + chars);
//    boolean containsNonDescAsc = false;
//    for (PdfCharacter chara : chars) {
//      boolean contains = !Characters.isDescender(chara) && !Characters.isAscender(chara);
//      containsNonDescAsc = containsNonDescAsc || contains;
//    }
//    
//    System.out.println(containsNonDescAsc);
    
//    return containsNonDescAsc;
//    System.out.println("True");
    
    if (numAscenders == overlappingElements.size()) {
      return false;
    }
    
    return true;
  }

  /**
   * Chooses the horizontal lane to use.
   */
  protected List<Rectangle> chooseHorizontalLanes(PdfArea area,
      List<Rectangle> lanes) {
    List<Rectangle> result = super.chooseHorizontalLanes(area, lanes);

    area.setRects(lanes);
    return result;
  }

  protected Rectangle computeXHeightRectangle(PdfArea area,
      List<PdfElement> elements) {
    Rectangle rect = SimpleRectangle.computeBoundingBox(elements);
    rect.setMinX(area.getRectangle().getMinX());
    rect.setMaxX(area.getRectangle().getMaxX());

    List<PdfCharacter> characters = area.getTextCharactersOverlapping(rect);

    Line meanLine = computeMeanLine(characters);
    Line baseLine = computeBaseLine(characters);

    if (meanLine != null) {
      rect.setMaxY(meanLine.getStartY());
    }

    if (baseLine != null) {
      rect.setMinY(baseLine.getStartY());
    }

    return rect;
  }

  protected Line computeBaseLine(List<PdfCharacter> characters) {
    Line baseLine = null;
    FloatCounter minYCounter = new FloatCounter();

    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new Comparators.MinXComparator());

      float minX = Float.NaN;
      float maxX = Float.NaN;
      for (PdfCharacter character : characters) {
        if (Characters.isBaselineCharacter(character)) {
          float minY = MathUtils.round(character.getRectangle().getMinY(), 1);
          minYCounter.add(minY);
        }

        if (Float.isNaN(minX)) {
          minX = character.getRectangle().getMinX();
        }
        maxX = character.getRectangle().getMaxX();
      }

      if (!minYCounter.isEmpty()) {
        float mostCommonMinY = minYCounter.getMostFrequentFloat();
        baseLine = new SimpleLine(minX, mostCommonMinY, maxX, mostCommonMinY);
      }
    }

    return baseLine;
  }

  protected Line computeMeanLine(List<PdfCharacter> characters) {
    FloatCounter maxYCounter = new FloatCounter();
    Line meanLine = null;

    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new Comparators.MinXComparator());

      float minX = Float.NaN;
      float maxX = Float.NaN;
      for (PdfCharacter character : characters) {
        if (Characters.isMeanlineCharacter(character)) {
          float maxY = MathUtils.round(character.getRectangle().getMaxY(), 1);
          maxYCounter.add(maxY);
        }

        if (Float.isNaN(minX)) {
          minX = character.getRectangle().getMinX();
        }
        maxX = character.getRectangle().getMaxX();
      }

      if (!maxYCounter.isEmpty()) {
        float mostCommonMaxY = maxYCounter.getMostFrequentFloat();
        meanLine = new SimpleLine(minX, mostCommonMaxY, maxX, mostCommonMaxY);
      }
    }

    return meanLine;
  }
}
