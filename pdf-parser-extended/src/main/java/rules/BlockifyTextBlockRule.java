package rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.freiburg.iif.model.Rectangle;
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
   * The overlapping elements of previous lane. 
   */
  protected List<PdfElement> prevOverlappingElements;
  
  /** 
   * The flag to indicate whether the previous lane is valid.
   */
  protected boolean prevIsValidHorizontalLane;
  
  /** 
   * The flag to indicate whether the previous overlapping elements consists 
   * only of ascenders and descenders.
   */
  protected boolean prevHasOnlyAscendersDescenders;

  // ===========================================================================
  
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
  
  // ===========================================================================
  
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

    // Decide if the given lane is a valid lane based on overlapping elements.
    List<PdfElement> overlappingElements = area.getElementsOverlapping(lane);

    if (equals(prevOverlappingElements, overlappingElements)) {
      // The set of current overlapping elements is equal to the previous 
      // overlapping elements.
      return handleEqualOverlappingElements(area, lane, overlappingElements);
    } else if (overlappingElements.isEmpty()) {
      // The set of current overlapping elements is empty.
      return handleEmptyOverlappingElements(area, lane, overlappingElements);
    } else {
      // The set of current overlapping elements is *not* empty.
      return handleNonEmptyOverlappingElements(area, lane, overlappingElements);
    }
  }

  // ===========================================================================
  
  /**
   * Decides if the given lane is valid, given that the set of given 
   * overlapping elements is equal to the previous overlapping elements.
   */
  protected boolean handleEqualOverlappingElements(PdfArea area, Rectangle lane,
      List<PdfElement> elements) {
    this.prevOverlappingElements = elements;
    this.prevHasOnlyAscendersDescenders = hasOnlyAscendersDescenders(elements);
    // No need to update this.prevIsValidHorizontalLane.
    return this.prevIsValidHorizontalLane;
  }
  
  /**
   * Decides if the given lane is valid, given that the given lane *doesn't* 
   * overlap any elements.
   */
  protected boolean handleEmptyOverlappingElements(PdfArea area, Rectangle lane,
      List<PdfElement> elements) {
    this.prevOverlappingElements = elements;
    this.prevHasOnlyAscendersDescenders = true; // (elements is empty)
    // The lane is valid if it *doesn't* split associated elements.
    this.prevIsValidHorizontalLane = !splitsAssociatedElements(area, lane);
    return this.prevIsValidHorizontalLane;
  }

  /**
   * Decides if the given lane is valid, given that the given lane *does* 
   * overlap any elements.
   */
  protected boolean handleNonEmptyOverlappingElements(PdfArea area,
      Rectangle lane, List<PdfElement> elements) {
    // Obtain, if the elements consists only of ascenders/descenders.
    boolean hasOnlyAscsDescs = hasOnlyAscendersDescenders(elements);

    if (hasOnlyAscsDescs) {
      // The current overlapping elements consist only of ascenders and
      // descenders. If also the previous overlapping elements consist only of 
      // ascenders/descenders and the set of current elements is a subset
      // of the previous overlapping elements, return the previous computed 
      // result.
      if (this.prevHasOnlyAscendersDescenders) {
        if (isSubSet(elements, prevOverlappingElements)) {
          this.prevOverlappingElements = elements;
          this.prevHasOnlyAscendersDescenders = true;
          // No need to update this.prevIsValidHorizontalLane
          return this.prevIsValidHorizontalLane;
        }
      }

      // The current overlapping elements consist only of ascenders and
      // descenders. If the previous overlapping elements *don't* consist only 
      // of ascenders/descenders, the lane is valid if it doesn't split 
      // associated elements.
      if (!this.prevHasOnlyAscendersDescenders) {
        this.prevOverlappingElements = elements;
        this.prevIsValidHorizontalLane = !splitsAssociatedElements(area, lane);
        this.prevHasOnlyAscendersDescenders = true;
        return prevIsValidHorizontalLane;
      }
    }
    
    // The current overlapping elements consists of at least one 
    // non-ascender/descender. The lane is not valid.
    this.prevOverlappingElements = elements;
    this.prevIsValidHorizontalLane = false;
    this.prevHasOnlyAscendersDescenders = hasOnlyAscsDescs;

    return false;
  }

  // ===========================================================================

  /**
   * Returns true if the given lane splits associated elements in the given 
   * area.
   */
  protected boolean splitsAssociatedElements(PdfArea area, Rectangle lane) {
    // Obtain, if we have split two consecutive elements.
    Rectangle rectangle = area.getRectangle();
    float midpoint = lane.getYMidpoint();

    List<Rectangle> subRectangles = rectangle.splitHorizontally(midpoint);

    Rectangle upperHalf = subRectangles.get(0);
    List<PdfElement> upperElements = area.getElementsWithin(upperHalf);

    Rectangle lowerHalf = subRectangles.get(1);
    List<PdfElement> lowerElements = area.getElementsWithin(lowerHalf);

    // Find the element with highest extraction order number in upper half.
    int highestUpperElementNum = -1;
    for (PdfElement element : upperElements) {
      if (element.getExtractionOrderNumber() > highestUpperElementNum) {
        highestUpperElementNum = element.getExtractionOrderNumber();
      }
    }

    // Find the element with highest extraction order number in lower half.
    int lowestLowerElementNum = Integer.MAX_VALUE;
    for (PdfElement element : lowerElements) {
      if (element.getExtractionOrderNumber() < lowestLowerElementNum) {
        lowestLowerElementNum = element.getExtractionOrderNumber();
      }
    }

    return highestUpperElementNum > lowestLowerElementNum;
  }

  // ---------------------------------------------------------------------------

  /**
   * Returns true, if the two given lists constains the same elements.
   */
  protected boolean equals(List<PdfElement> els1, List<PdfElement> els2) {
    if (els1 == null || els2 == null) {
      return false;
    }

    if (els1.size() != els2.size()) {
      return false;
    }

    Set<PdfElement> els2Set = new HashSet<>(els2);
    for (PdfElement el : els1) {
      if (!els2Set.contains(el)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true, if elements1 is a subset if elements2.
   */
  protected boolean isSubSet(List<PdfElement> elements1,
      List<PdfElement> elements2) {
    if (elements1 == null || elements2 == null) {
      return false;
    }

    HashSet<PdfElement> elements2Set = new HashSet<>(elements2);
    for (PdfElement element : elements1) {
      if (!elements2Set.contains(element)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns true, if the given elements consists only of ascenders and 
   * descenders.
   */
  protected boolean hasOnlyAscendersDescenders(List<PdfElement> els) {
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
      }
    }
    return true;
  }
}
