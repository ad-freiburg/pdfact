package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A simple (geometric) line.
 * 
 * @author Claudius Korzen.
 */
public interface Line {
  /**
   * Returns the start point of this line.
   * 
   * @return The start point of this line.
   */
  Point getStartPoint();

  /**
   * Sets the start point of this line.
   * 
   * @param start
   *        The start point of this line.
   */
  void setStartPoint(Point start);

  // ==========================================================================

  /**
   * Returns the x-coordinate of the start point.
   * 
   * @return The x-coordinate of the start point.
   */
  float getStartX();

  /**
   * Sets the x-coordinate of the start point.
   * 
   * @param x
   *        The x-coordinate of the start point.
   */
  void setStartX(float x);

  // ==========================================================================

  /**
   * Returns the y-coordinate of the start point.
   * 
   * @return The y-coordinate of the start point.
   */
  float getStartY();

  /**
   * Sets the y-coordinate of the start point.
   * 
   * @param y
   *        The y-coordinate of the start point.
   */
  void setStartY(float y);

  // ==========================================================================

  /**
   * Returns the end point of this line.
   * 
   * @return The end point of this line.
   */
  Point getEndPoint();

  /**
   * Sets the end point of this line.
   * 
   * @param end
   *        The end point of this line.
   */
  void setEndPoint(Point end);

  // ==========================================================================

  /**
   * Returns the x-coordinate of the end point.
   * 
   * @return The x-coordinate of the end point.
   */
  float getEndX();

  /**
   * Sets the x-coordinate of the end point.
   * 
   * @param x
   *        The x-coordinate of the end point.
   */
  void setEndX(float x);

  // ==========================================================================

  /**
   * Returns the y-coordinate of the end point.
   * 
   * @return The y-coordinate of the end point.
   */
  float getEndY();

  /**
   * Sets the y-coordinate of the end point.
   * 
   * @param y
   *        The y-coordinate of the end point.
   */
  void setEndY(float y);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Line}.
   * 
   * @author Claudius Korzen
   */
  public interface LineFactory {
    /**
     * Creates a new instance of {@link Line}.
     * 
     * @return A new instance of {@link Line}.
     */
    Line create();

    /**
     * Creates a new instance of {@link Line}.
     * 
     * @param startPoint
     *        The start point of the line.
     * @param endPoint
     *        The end point of the line.
     * 
     * @return A new instance of {@link Line}.
     */
    Line create(
        @Assisted("start") Point startPoint,
        @Assisted("end") Point endPoint);

    /**
     * Creates a new instance of {@link Line}.
     * 
     * @param startX
     *        The x-coordinate of the start point of the line.
     * @param startY
     *        The y-coordinate of the start point of the line.
     * @param endX
     *        The x-coordinate of the end point of the line.
     * @param endY
     *        The y-coordinate of the end point of the line.
     * 
     * @return A new instance of {@link Line}.
     */
    Line create(
        @Assisted("startX") float startX,
        @Assisted("startY") float startY,
        @Assisted("endX") float endX,
        @Assisted("endY") float endY);
  }
}
