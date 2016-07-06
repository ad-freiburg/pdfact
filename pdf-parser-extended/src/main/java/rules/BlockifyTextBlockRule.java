package rules;

import java.util.HashMap;
import java.util.List;

import com.google.inject.spi.Element;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.Characters;
import model.PdfArea;
import model.PdfElement;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a "block" into several text lines.
 *
 * @author Claudius Korzen
 */
public class BlockifyTextBlockRule implements BlockifyRule {
  /**
   * Remember the last splitted block.
   */
  protected PdfArea prevArea;

  /**
   * Remember the last split result for the last splitted block.
   */
  protected List<PdfElement> prevOverlappingElements;

  /**
   * Remember the last split result for the last splitted block.
   */
  protected boolean prevLaneIsValid;

  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return HorizontalSweepDirection.TOP_TO_BOTTOM;
  }

  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    return 0.1f;
  }

  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    if (area == null || lane == null) {
      return false;
    }

    // Try to decide if the given lane is a valid line separator by only 
    // allowing specific elements intersecting this lane.
    List<PdfElement> overlappingElements = area.getElementsOverlapping(lane);

    if (area.getPage().getPageNumber() == 17) {
      System.out.println(overlappingElements);
    }
    
    // The lane is totally valid, if it doesn't overlap any elements.
    if (overlappingElements.isEmpty()) {
      return true;
    }

    if (area.getPage().getPageNumber() == 17) {
      System.out.println("contains1: " + containsOnlyAscDesc(overlappingElements));
    }
    if (containsOnlyAscDesc(overlappingElements)) {
      if (area.getPage().getPageNumber() == 17) {
      }
      return !containsOnlyAscDesc(area, overlappingElements);
    }
        
    return false;
  }

  //  @Override
  //  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
  //    if (area == null || lane == null) {
  //      return false;
  //    }
  //        
  //    float mostCommonFontsize = area.getTextStatistics().getMostCommonFontsize();
  //    
  //    // Try to decide if the given lane is a valid line separator by only 
  //    // allowing specific elements intersecting this lane.
  //    List<PdfElement> overlappingElements = area.getElementsOverlapping(lane);
  //              
  //    if (area.getPage().getPageNumber() == 24) {
  //      System.out.println(overlappingElements);
  //    }
  //    
  //    // The lane is totally valid, if it doesn't overlap any elements.
  //    if (overlappingElements.isEmpty()) {
  //      this.prevArea = area;
  //      this.prevLaneIsValid = true;
  //      this.prevOverlappingElements = overlappingElements;
  //      if (area.getPage().getPageNumber() == 24) {
  //        System.out.println(true);
  //      }
  //      return true;
  //    }
  //        
  //    // Otherwise, we have decision depends on the characteristics of the 
  //    // overlapped elements.
  //    for (PdfElement element : overlappingElements) {
  //      String string = element.toString();
  //      float fontsize = MathUtils.round(element.getFontsize(), 1);
  //      
  //      // Ignore elements whose fontsize is significantly smaller than the most 
  //      // common fontsize in the given area. This is supposed to allow sub- and 
  //      // supscripts to intersect the lane.
  ////      if (MathUtils.isSmaller(fontsize, mostCommonFontsize, 0.5f)) {
  ////        continue;
  ////      }
  //            
  //      if (string != null && !string.isEmpty()) {
  //        char c = string.charAt(0);
  //        
  //        // It is not allowed that characters other than descenders and ascenders
  //        // intersect the lane.
  //        if (Character.isAlphabetic(c) 
  //            && !Characters.isDescender(c) 
  //            && !Characters.isAscender(c)) {
  //          this.prevArea = area;
  //          this.prevLaneIsValid = false;
  //          this.prevOverlappingElements = overlappingElements;
  //          if (area.getPage().getPageNumber() == 24) {
  //            System.out.println(false);
  //          }
  //          return false;
  //        }
  //        
  //        // It is not allowed that digits intersect the lane.
  //        if (Character.isDigit(c)) {
  //          this.prevArea = area;
  //          this.prevLaneIsValid = false;
  //          this.prevOverlappingElements = overlappingElements;
  //          if (area.getPage().getPageNumber() == 24) {
  //            System.out.println(false);
  //          }
  //          return false;
  //        }
  //      }
  //    }
  //        
  //    // If this code is reached, only ascenders or descenders overlaps the 
  //    // lane. The lane is only valid, if the previous lane in the area doesn't 
  //    // overlap any characters.     
  //    if (isEqualOverlappingElements(prevOverlappingElements, overlappingElements)) {
  //      if (area.getPage().getPageNumber() == 24) {
  //        System.out.println(prevLaneIsValid);
  //      }
  //      return prevLaneIsValid;
  //    } else {
  //      if (area.getPage().getPageNumber() == 24) {
  //        System.out.println(prevArea != area || !prevLaneIsValid);
  //      }
  //      
  //      return prevArea != area || !prevLaneIsValid;
  //    }
  //  }

  @Override
  public VerticalSweepDirection getVerticalLaneSweepDirection() {
    return VerticalSweepDirection.LEFT_TO_RIGHT;
  }

  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    return Float.MAX_VALUE;
  }

  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) {
    return false;
  }

  protected boolean isEqualOverlappingElements(List<PdfElement> elements1,
      List<PdfElement> elements2) {
    if (elements1 == null && elements2 == null) {
      return true;
    }

    if (elements1 == null) {
      return false;
    }

    if (elements2 == null) {
      return false;
    }

    if (elements1.size() != elements2.size()) {
      return false;
    }

    for (int i = 0; i < elements1.size(); i++) {
      if (!elements1.get(i).equals(elements2.get(i))) {
        return false;
      }
    }
    return true;
  }

  protected boolean containsOnlyAscDesc(List<PdfElement> elements) {
    for (PdfElement element : elements) {
      String string = element.toString();

      if (string != null && !string.isEmpty()) {
        char c = string.charAt(0);

        // It is not allowed that characters other than descenders and ascenders
        // intersect the lane.
        if (Character.isAlphabetic(c)
            && !Characters.isDescender(c)
            && !Characters.isAscender(c)) {
          return false;
        }

        // It is not allowed that digits intersect the lane.
        if (Character.isDigit(c)) {
          return false;
        }
      }
    }
    return true;
  }

  protected boolean containsOnlyAscDesc(PdfArea area,
      List<PdfElement> elements) {
    Rectangle boundingBox = SimpleRectangle.computeBoundingBox(elements);

    boundingBox.setMinX(area.getRectangle().getMinX());
    boundingBox.setMaxX(area.getRectangle().getMaxX());

    List<PdfElement> overlappingElements =
        area.getElementsOverlapping(boundingBox);

    if (overlappingElements.isEmpty()) {
      return false;
    }

    if (area.getPage().getPageNumber() == 17) {
      System.out.println("  *  " + overlappingElements);
    }
    
    for (PdfElement element : overlappingElements) {
      String string = element.toString();

      if (string != null && !string.isEmpty()) {
        char c = string.charAt(0);
        if (Characters.isLatinLetter(c)
            && !Characters.isDescender(c)
            && !Characters.isAscender(c)) {
          if (area.getPage().getPageNumber() == 17) {
            System.out.println(c);
          }
          return false;
        }
      }
    }

    return true;
  }
}
