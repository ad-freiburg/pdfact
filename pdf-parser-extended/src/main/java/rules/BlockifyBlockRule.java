package rules;

import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import model.PdfArea;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a "block" into several text lines.
 *
 * @author Claudius Korzen
 */
public class BlockifyBlockRule implements BlockifyRule {
  
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
    float sumOverlaps = 0;
      
    if (area != null) {
      List<? extends HasRectangle> els = area.getElementsOverlappedBy(lane);
      
      for (HasRectangle el : els) {          
        sumOverlaps += lane.computeOverlap(el.getRectangle());
      }
    }
    
    return sumOverlaps / lane.getArea() < 0.0075f;
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
}
