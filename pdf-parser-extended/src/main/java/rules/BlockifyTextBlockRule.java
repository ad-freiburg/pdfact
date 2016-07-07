package rules;

import java.util.List;

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
    List<PdfElement> overlappingEls = area.getElementsOverlapping(lane);
    
    // The lane is totally valid, if it doesn't overlap any elements.
    if (overlappingEls.isEmpty()) {
      return true;
    }

    // If the lane is valid depends on the type of the overlapping elements.
    if (consistsOnlyOfAscendersOrDescenders(overlappingEls)) {
      // The lane is valid, if the overlapping elements are ascenders or 
      // descenders exclusively and the line to which the element belongs to
      // contains at least one non-ascender/descender.
      return !lineConsistsOnlyOfAscendersOrDescenders(area, overlappingEls);
    }
        
    return false;
  }

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

  // ---------------------------------------------------------------------------
  
  /**
   * Returns true, if the given elements consists only of ascenders and 
   * descenders.
   */
  protected boolean consistsOnlyOfAscendersOrDescenders(List<PdfElement> els) {
    for (PdfElement element : els) {
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

  /**
   * Returns true, if the area which is spanned by the given elements in the 
   * given area only consists of ascenders and descenders.
   */
  protected boolean lineConsistsOnlyOfAscendersOrDescenders(PdfArea area,
      List<PdfElement> elements) {
    // Define the search area (the bounding box of elements).
    Rectangle bBox = SimpleRectangle.computeBoundingBox(elements);
    // Expand the search area to full width of area.
    bBox.setMinX(area.getRectangle().getMinX()); 
    bBox.setMaxX(area.getRectangle().getMaxX());
    // Add minimal margin to allow things like a'
    bBox.setMinY(bBox.getMinY());
    bBox.setMaxY(bBox.getMaxY());
        
    // Compute overlapping elements.
    List<PdfElement> overlappingElements = area.getElementsOverlapping(bBox);
    
    if (overlappingElements.isEmpty()) {
      return false;
    }
    
    // Obtain if the overlapping elements only consists of ascenders/descenders.
    for (PdfElement element : overlappingElements) {
      String string = element.toString();

      if (string != null && !string.isEmpty()) {
        char c = string.charAt(0);
        if (Characters.isLatinLetter(c)
            && !Characters.isDescender(c)
            && !Characters.isAscender(c)) {
          return false;
        }
      }
    }

    return true;
  }
}
