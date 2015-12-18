package rules;

import de.freiburg.iif.model.Rectangle;
import model.PdfArea;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a line into several words.
 *
 * @author Claudius Korzen
 */
public class BlockifyTextLineRule implements BlockifyRule {
  
  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return HorizontalSweepDirection.TOP_TO_BOTTOM;
  }
  
  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    return 0;
  }

  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    return false;
  }
  
  @Override
  public VerticalSweepDirection getVerticalLaneSweepDirection() {
    return VerticalSweepDirection.LEFT_TO_RIGHT;
  }
  
  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    return 0.5f;
  }
  
  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) {    
    return area.getElementsOverlapping(lane).isEmpty();
  }
}