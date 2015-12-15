package rules;

import de.freiburg.iif.model.Rectangle;
import model.PdfArea;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * Interface for all implementing blockify rules.
 *
 * @author Claudius Korzen
 */
public interface BlockifyRule {
  /**
   * Returns the direction, in which the vertical lane should be swept.
   * 
   * @return the direction, in which the vertical lane should be swept.
   */
  VerticalSweepDirection getVerticalLaneSweepDirection();
  
  /**
   * Returns the width of vertical lanes, that should be swept on splitting.
   */
  float getVerticalLaneWidth(PdfArea area);
  
  /**
   * Returns true, when the given lane is a valid vertical lane.
   */
  boolean isValidVerticalLane(PdfArea area, Rectangle lane);
  
  // ___________________________________________________________________________
  
  /**
   * Returns the direction, in which the horizontal lane should be swept.
   */
  HorizontalSweepDirection getHorizontalLaneSweepDirection();
  
  /**
   * Returns the height of horizontal lanes, that should be swept on
   * splitting.
   */
  float getHorizontalLaneHeight(PdfArea area);
  
  /**
   * Returns true, when the given lane is a valid horizontal lane.
   */
  boolean isValidHorizontalLane(PdfArea area, Rectangle lane);
}
