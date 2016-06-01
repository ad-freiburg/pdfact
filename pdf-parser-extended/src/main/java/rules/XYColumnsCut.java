package rules;

import java.util.ArrayList;
import java.util.List;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.PdfArea;
import model.PdfDocument;
import model.PdfElement;
import model.PdfXYCutArea;

/**
 * Splitter to split a page into blocks.
 * 
 * @author Claudius Korzen
 */
public class XYColumnsCut extends XYCut {

  @Override
  protected float getVerticalLaneWidth(PdfArea area) {
    PdfDocument doc = area.getPdfDocument();

    float docWidths = doc.getDimensionStatistics().getMostCommonWidth();
    float pageWidths = area.getDimensionStatistics().getMostCommonWidth();

    return 1f * Math.max(docWidths, pageWidths);
  }

  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    // Ideally, we should use most common values here. But doing so fails
    // for grotoap-20902190.pdf - because of so many dots on page 3 and 4.
    PdfDocument doc = area.getPdfDocument();
    float docHeights = doc.getDimensionStatistics().getMostCommonHeight();
    float pageHeights = area.getDimensionStatistics().getMostCommonHeight();

    // return 1.5f * doc.getEstimatedLinePitch();
    return 1.5f * Math.max(docHeights, pageHeights);
  }

  // ___________________________________________________________________________

  @Override
  protected boolean isValidVerticalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    float leftHalfWidth = computeWidthOfLeftHalf(area, prevLane, lane);
    float rightHalfWidth = computeWidthOfRightHalf(area, prevLane, lane);

    PdfDocument doc = area.getPdfDocument();
    float mostCommonWidth = doc.getDimensionStatistics().getMostCommonWidth();

    // The resulting rects to the left or to the right must not be too slim.
    if (leftHalfWidth < 5 * mostCommonWidth
        || rightHalfWidth < 5 * mostCommonWidth) {
      return false;
    }

    return area.getElementsOverlapping(lane).isEmpty();
  }

  /**
   * Computes the width of the left half that results if the given area would
   * be splitted by the given lane.
   */
  protected float computeWidthOfLeftHalf(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    Rectangle leftHalf = new SimpleRectangle();
    leftHalf.setMinX(area.getRectangle().getMinX());
    if (prevLane != null) {
      leftHalf.setMinX(prevLane.getRectangle().getMaxX());
    }
    leftHalf.setMinY(area.getRectangle().getMinY());
    leftHalf.setMaxX(lane.getRectangle().getMinX());
    leftHalf.setMaxY(area.getRectangle().getMaxY());
    List<PdfElement> leftHalfElements = area.getElementsOverlapping(leftHalf);
    Rectangle boundBox = SimpleRectangle.computeBoundingBox(leftHalfElements);
    return boundBox.getWidth();
  }

  /**
   * Computes the width of the right half that results if the given area would
   * be splitted by the given lane.
   */
  protected float computeWidthOfRightHalf(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    Rectangle rightHalf = new SimpleRectangle();
    rightHalf.setMinX(lane.getRectangle().getMaxX());
    rightHalf.setMinY(area.getRectangle().getMinY());
    rightHalf.setMaxX(area.getRectangle().getMaxX());
    rightHalf.setMaxY(area.getRectangle().getMaxY());
    List<PdfElement> rightHalfElements = area.getElementsOverlapping(rightHalf);
    Rectangle boundBox = SimpleRectangle.computeBoundingBox(rightHalfElements);
    return boundBox.getWidth();
  }

  // ___________________________________________________________________________

  @Override
  protected List<Rectangle> chooseHorizontalLanes(PdfArea area,
      List<Rectangle> lanes) {
    List<Rectangle> chosen = new ArrayList<>();
    
    if (lanes != null) {
      Rectangle rect = area.getRectangle();
      if (rect != null) {
        List<Rectangle> subRects = rect.splitHorizontally(lanes);
  
        List<Rectangle> prevVertLanes = null;
        for (int i = 0; i < subRects.size(); i++) {
          PdfArea subarea = new PdfXYCutArea(area.getPage(), subRects.get(i));
  
          // Choose only those horizontal lanes, which allows to split the 
          // resulting subareas vertically.  
          List<Rectangle> vertLanes = identifyVerticalLanes(subarea);
  
          if (!vertLanes.isEmpty()) {
            // The subarea could be splitted vertically. Add both, the upper and
            // the lower bounding lane to result list.
            Rectangle upperLane = i > 0 ? lanes.get(i - 1) : null;
            Rectangle lowerLane = i < lanes.size() ? lanes.get(i) : null;
  
            boolean compatibleLanes =
                areVerticalLanesCompatible(prevVertLanes, vertLanes);
  
            if (chosen.size() > 0) {
              chosen.remove(chosen.size() - 1);
            }
  
            // Choose the upper lane if the vertical lanes aren't compatible.
            if (upperLane != null && !compatibleLanes) {
              chosen.add(upperLane);
            }
            if (lowerLane != null) {
              chosen.add(lowerLane);
            }
          }
          prevVertLanes = vertLanes;
        }
      }
    }
    area.setRects(chosen);
    return chosen;
  }

  /** 
   * Returns true, if the previous vertical lanes and the current vertical 
   * lanes are compatible, i.e. if they contains the same number of lanes and 
   * each lane pair overlaps horizontally.
   */
  protected boolean areVerticalLanesCompatible(List<Rectangle> prevLanes,
      List<Rectangle> lanes) {
    if (prevLanes == null || prevLanes.isEmpty()) {
      return false;
    }

    if (lanes == null || lanes.isEmpty()) {
      return false;
    }

    if (prevLanes.size() != lanes.size()) {
      return false;
    }

    for (int i = 0; i < prevLanes.size(); i++) {
      Rectangle prevLane = prevLanes.get(i);
      Rectangle lane = lanes.get(i);

      if (!prevLane.overlapsHorizontally(lane)) {
        return false;
      }
    }

    return true;
  }
}
