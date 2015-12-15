package model;

/**
 * Some sweep directions.
 *
 * @author Claudius Korzen
 */
public class SweepDirection {
  /**
   * The vertical sweep directions.
   *
   * @author Claudius Korzen
   */
  public static enum VerticalSweepDirection {
    /** The direction left to right. */
    LEFT_TO_RIGHT,
    /** The direction right to left. */
    RIGHT_TO_LEFT
  }
  
  /**
   * The horizontal sweep directions.
   *
   * @author Claudius Korzen
   */
  public static enum HorizontalSweepDirection {
    /** The direction top to bottom. */
    TOP_TO_BOTTOM,
    /** The direction bottom to top. */
    BOTTOM_TO_TOP
  }
}
