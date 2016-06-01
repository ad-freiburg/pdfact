package rules;

import static model.SweepDirection.HorizontalSweepDirection.TOP_TO_BOTTOM;
import static model.SweepDirection.VerticalSweepDirection.LEFT_TO_RIGHT;

import java.util.List;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.PdfArea;
import model.PdfDocument;
import model.PdfElement;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a page into several "blocks".
 *
 * @author Claudius Korzen
 */
public class BlockifyTextPageRule implements BlockifyRule {
  /** The sweep direction for the horizontal lane. */
  protected HorizontalSweepDirection horizontalLaneSweepDirection;
  /** The sweep direction for the vertical lane. */
  protected VerticalSweepDirection verticalLaneSweepDirection;

  /**
   * The default constructor.
   */
  public BlockifyTextPageRule() {
    this.horizontalLaneSweepDirection = TOP_TO_BOTTOM;
    this.verticalLaneSweepDirection = LEFT_TO_RIGHT;
  }

  /**
   * The default constructor.
   */
  public BlockifyTextPageRule(HorizontalSweepDirection horizontalSweepDirection,
      VerticalSweepDirection verticalSweepDirection) {
    this.horizontalLaneSweepDirection = horizontalSweepDirection;
    this.verticalLaneSweepDirection = verticalSweepDirection;
  }

  @Override
  public VerticalSweepDirection getVerticalLaneSweepDirection() {
    return this.verticalLaneSweepDirection;
  }

  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    // Ideally, we should use most common values here. But doing so fails
    // for grotoap-20902190.pdf - because of so many dots on page 3 and 4.
    PdfDocument doc = area.getPdfDocument();

    float docWidths = doc.getDimensionStatistics().getMostCommonWidth();
    float pageWidths = area.getDimensionStatistics().getMostCommonWidth();

    return 2f * Math.max(docWidths, pageWidths);
  }

  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) {
    float leftHalfWidth = computeWidthOfLeftHalf(area, lane);
    float rightHalfWidth = computeWidthOfRightHalf(area, lane);

    PdfDocument doc = area.getPdfDocument();
    float mostCommonWidth = doc.getDimensionStatistics().getMostCommonWidth();

    if (leftHalfWidth < 5 * mostCommonWidth
        || rightHalfWidth < 5 * mostCommonWidth) {
      return false;
    }

    return area.getElementsOverlapping(lane).isEmpty();
  }

  protected float computeWidthOfLeftHalf(PdfArea area, Rectangle lane) {
    Rectangle leftHalf = new SimpleRectangle();
    leftHalf.setMinX(area.getRectangle().getMinX());
    leftHalf.setMinY(area.getRectangle().getMinY());
    leftHalf.setMaxX(lane.getRectangle().getMinX());
    leftHalf.setMaxY(area.getRectangle().getMaxY());
    List<PdfElement> leftHalfElements = area.getElementsOverlapping(leftHalf);
    Rectangle boundBox = SimpleRectangle.computeBoundingBox(leftHalfElements);
    return boundBox.getWidth();
  }

  protected float computeWidthOfRightHalf(PdfArea area, Rectangle lane) {
    Rectangle rightHalf = new SimpleRectangle();
    rightHalf.setMinX(lane.getRectangle().getMaxX());
    rightHalf.setMinY(area.getRectangle().getMinY());
    rightHalf.setMaxX(area.getRectangle().getMaxX());
    rightHalf.setMaxY(area.getRectangle().getMaxY());
    List<PdfElement> rightHalfElements = area.getElementsOverlapping(rightHalf);
    Rectangle boundBox = SimpleRectangle.computeBoundingBox(rightHalfElements);
    return boundBox.getWidth();
  }

  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return this.horizontalLaneSweepDirection;
  }

  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    // Ideally, we should use most common values here. But doing so fails
    // for grotoap-20902190.pdf - because of so many dots on page 3 and 4.
    PdfDocument doc = area.getPdfDocument();
    float docHeights = doc.getDimensionStatistics().getMostCommonHeight();
    float pageHeights = area.getDimensionStatistics().getMostCommonHeight();

    // System.out.println(Math.max(docHeights, pageHeights) + " " +
    // doc.getEstimatedLinePitch());

    return 1.5f * doc.getEstimatedLinePitch();
    // return 1.5f * Math.max(docHeights, pageHeights);
  }

  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    return area.getElementsOverlapping(lane).isEmpty();
  }
}
