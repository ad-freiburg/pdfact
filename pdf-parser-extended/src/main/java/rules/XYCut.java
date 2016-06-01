package rules;

import static model.SweepDirection.HorizontalSweepDirection.BOTTOM_TO_TOP;
import static model.SweepDirection.HorizontalSweepDirection.TOP_TO_BOTTOM;
import static model.SweepDirection.VerticalSweepDirection.LEFT_TO_RIGHT;
import static model.SweepDirection.VerticalSweepDirection.RIGHT_TO_LEFT;

import java.util.ArrayList;
import java.util.List;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.PdfArea;
import model.PdfXYCutArea;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * Class to split pdf areas using XYCut algorithm.
 * 
 * @author Claudius Korzen
 */
public class XYCut {
  /** Positive infinity. */
  protected static final float INF = Float.MAX_VALUE;

  boolean debug = false;
  
  /**
   * Splits the given area according to the given rules.
   */
  public List<PdfArea> split(PdfArea area) {
    List<PdfArea> subareas = new ArrayList<>();
    split(area, subareas);
    return subareas;
  }

  /**
   * Splits the given area into smaller subareas according to the given rules.
   * Fills the subareas into the given result list.
   */
  protected void split(PdfArea area, List<PdfArea> result) {
    // Try to split the area vertically.
    List<PdfArea> areas = splitVertically(area);

    if (areas.isEmpty()) {
      // The area couldn't be splitted vertically. Try it horizontally.
      areas = splitHorizontally(area);

      if (areas.isEmpty()) {
        // The area couldn't be splitted horizontally. Add the area to result.
        result.add(area);
      } else {
        // The area was split horizontally.
        for (PdfArea subarea : areas) {
          split(subarea, result);
        }
      }
    } else {      
      // The area was split vertically.
      for (PdfArea subarea : areas) {
        split(subarea, result);
      }
    }
  }

  // ___________________________________________________________________________

  /**
   * Tries to split the given area vertically.
   */
  public List<PdfArea> splitVertically(PdfArea area) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    // Identify vertical split lanes.
    List<Rectangle> lanes = identifyVerticalLanes(area);

    if (lanes != null && !lanes.isEmpty()) {
      List<Rectangle> subRects = area.getRectangle().splitVertically(lanes);

      for (Rectangle subRect : subRects) {
        result.add(new PdfXYCutArea(area, subRect));
      }
    }

    return result;
  }

  /**
   * Tries to split the given area horizontally.
   */
  public List<PdfArea> splitHorizontally(PdfArea area) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    // Identify the vertical split lane.
    List<Rectangle> lanes = identifyHorizontalLanes(area);

    if (lanes != null && !lanes.isEmpty()) {      
      List<Rectangle> subRects = area.getRectangle().splitHorizontally(lanes);
      
      for (Rectangle subRect : subRects) {
        result.add(new PdfXYCutArea(area, subRect));
      }
    }
    return result;
  }

  // ___________________________________________________________________________

  /**
   * Identifies a vertical lane in the given area.
   */
  protected List<Rectangle> identifyVerticalLanes(PdfArea area) {
    return chooseVerticalLanes(area, identifyVerticalLaneCandidates(area));
  }

  /**
   * Identifies a vertical lane in the given area.
   */
  protected List<Rectangle> identifyHorizontalLanes(PdfArea area) {
    return chooseHorizontalLanes(area, identifyHorizontalLaneCandidates(area));
  }

  // ___________________________________________________________________________

  /**
   * Identifies a vertical lane in the given area.
   */
  protected List<Rectangle> identifyVerticalLaneCandidates(PdfArea area) {
    List<Rectangle> candidates = new ArrayList<>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Obtain the sweep direction.
      VerticalSweepDirection dir = getVerticalLaneSweepDirection();

      // Obtain the dimensions of lane to sweep.
      float ruleLaneWidth = getVerticalLaneWidth(area);
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
      Rectangle lane = new SimpleRectangle(INF, lMinY, -INF, lMaxY);
      Rectangle prevLane = null;

      float pos = lMinX;
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinX() && pos <= rect.getMaxX() - ruleLaneWidth) {
        ruleLane.moveTo(pos, ruleLane.getMinY());

        if (isValidVerticalLane(area, prevLane, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinX(Math.min(lane.getMinX(), ruleLane.getMinX()));
            lane.setMaxX(Math.max(lane.getMaxX(), ruleLane.getMaxX()));
          }
        } else {
          if (lane.getWidth() >= ruleLaneWidth) {
            candidates.add(lane);
            prevLane = lane;
          }

          lane = new SimpleRectangle(INF, lMinY, -INF, lMaxY);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == LEFT_TO_RIGHT) ? (pos + .5f) : (pos - .5f);
      }
    }
    return candidates;
  }

  /**
   * Identifies a horizontal lane in the given area.
   */
  protected List<Rectangle> identifyHorizontalLaneCandidates(PdfArea area) {
    List<Rectangle> candidates = new ArrayList<>();
    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Determine the sweep direction.
      HorizontalSweepDirection dir = getHorizontalLaneSweepDirection();
      // Determine the width of vertical lane to sweep.
      float ruleLaneHeight = getHorizontalLaneHeight(area);

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
      Rectangle lane = new SimpleRectangle(lMinX, INF, lMaxX, -INF);
      Rectangle prevLane = null;

      float pos = lMinY;
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinY() && pos <= rect.getMaxY() - ruleLaneHeight) {
        ruleLane.moveTo(ruleLane.getMinX(), pos);
                        
        if (isValidHorizontalLane(area, prevLane, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinY(Math.min(lane.getMinY(), ruleLane.getMinY()));
            lane.setMaxY(Math.max(lane.getMaxY(), ruleLane.getMaxY()));
          }
        } else {
          if (lane.getHeight() >= ruleLaneHeight) {
            candidates.add(lane);
            prevLane = lane;
          }

          lane = new SimpleRectangle(lMinX, INF, lMaxX, -INF);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == TOP_TO_BOTTOM) ? (pos - .5f) : (pos + .5f);
      }
    }
    System.out.println("!!! " + candidates);
    
    return candidates;
  }

  // ___________________________________________________________________________

  /**
   * Returns the minimal width a vertical lane must offer.
   */
  protected float getVerticalLaneWidth(PdfArea area) {
    return 1f;
  }

  /**
   * Returns the minimal height a horizontal lane must offer.
   */
  protected float getHorizontalLaneHeight(PdfArea area) {
    return 1f;
  }

  // ___________________________________________________________________________

  /**
   * Returns the sweep direction for the vertical lane.
   */
  protected VerticalSweepDirection getVerticalLaneSweepDirection() {
    return VerticalSweepDirection.LEFT_TO_RIGHT;
  }

  /**
   * Returns the sweep direction for the horizontal lane.
   */
  protected HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return HorizontalSweepDirection.TOP_TO_BOTTOM;
  }

  // ___________________________________________________________________________

  /**
   * Returns true, if the given lane is a valid vertical lane.
   */
  protected boolean isValidVerticalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    return area.getElementsOverlapping(lane).isEmpty();
  }

  /**
   * Returns true, if the given lane is a valid horizontal lane.
   */
  protected boolean isValidHorizontalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    return area.getElementsOverlapping(lane).isEmpty();
  }

  // ___________________________________________________________________________

  /**
   * Chooses the vertical lane to use.
   */
  protected List<Rectangle> chooseVerticalLanes(PdfArea area,
      List<Rectangle> lanes) {
    return lanes;
  }

  /**
   * Chooses the horizontal lane to use.
   */
  protected List<Rectangle> chooseHorizontalLanes(PdfArea area,
      List<Rectangle> lanes) {
    return lanes;
  }
}
