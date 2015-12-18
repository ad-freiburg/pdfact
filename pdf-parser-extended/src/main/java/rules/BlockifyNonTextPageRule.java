package rules;

import static model.SweepDirection.HorizontalSweepDirection.TOP_TO_BOTTOM;
import static model.SweepDirection.VerticalSweepDirection.LEFT_TO_RIGHT;

import de.freiburg.iif.model.Rectangle;
import model.PdfArea;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a page into several "blocks".
 *
 * @author Claudius Korzen
 */
public class BlockifyNonTextPageRule implements BlockifyRule {
  /** The sweep direction for the horizontal lane. */
  protected HorizontalSweepDirection horizontalLaneSweepDirection;
  /** The sweep direction for the vertical lane. */
  protected VerticalSweepDirection verticalLaneSweepDirection;

  /** 
   * The default constructor. 
   */
  public BlockifyNonTextPageRule() {
    this.horizontalLaneSweepDirection = TOP_TO_BOTTOM;
    this.verticalLaneSweepDirection = LEFT_TO_RIGHT;
  }
  
  /**
   * The default constructor.
   */
  public BlockifyNonTextPageRule(
      HorizontalSweepDirection horizontalSweepDirection, 
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
    return 1f;
  }

  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) {
    return area.getNonTextElementsOverlapping(lane).isEmpty();
  }

  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return this.horizontalLaneSweepDirection;
  }

  @Override
  public float getHorizontalLaneHeight(PdfArea area) {        
    return 1f;
  }

  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    return isValidVerticalLane(area, lane);
  }
}

