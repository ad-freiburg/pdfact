package rules;

import de.freiburg.iif.model.Rectangle;
import model.PdfArea;

/**
 * Splitter to split a textline into words.
 * 
 * @author Claudius Korzen
 */
public class XYWordCut extends XYCut {

  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    return 0.5f;
  }

  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    return 0;
  }

  // ___________________________________________________________________________
  
  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    return false;
  }
    
  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle prevLane,
      Rectangle lane) {
    return area.getTextCharactersOverlapping(lane).isEmpty();
  }
}
