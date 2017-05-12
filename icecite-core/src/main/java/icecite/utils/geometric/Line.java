package icecite.utils.geometric;

/**
 * The interface that declares the methods of a line.
 * 
 * @author Claudius Korzen.
 */
public abstract class Line extends Geometric {
  /**
   * Returns the start point of this line.
   * 
   * @return The start point of this line.
   */
  public abstract Point getStartPoint();

  /**
   * Sets the start point of this line.
   * 
   * @param start
   *        The start point of this line.
   */
  public abstract void setStartPoint(Point start);

  /**
   * Returns the x coordinate of the start point.
   * 
   * @return The x coordinate of the start point.
   */
  public abstract float getStartX();

  /**
   * Sets the x coordinate of the start point.
   * 
   * @param x
   *        The x coordinate of the start point.
   */
  public abstract void setStartX(float x);

  /**
   * Returns the y coordinate of the start point.
   * 
   * @return The y coordinate of the start point.
   */
  public abstract float getStartY();

  /**
   * Sets the y coordinate of the start point.
   * 
   * @param y
   *        The y coordinate of the start point.
   */
  public abstract void setStartY(float y);

  /**
   * Returns the end point of this line.
   * 
   * @return The end point of this line.
   */
  public abstract Point getEndPoint();

  /**
   * Sets the end point of this line.
   * 
   * @param end
   *        The end point of this line.
   */
  public abstract void setEndPoint(Point end);

  /**
   * Returns the x coordinate of the end point.
   * 
   * @return The x coordinate of the end point.
   */
  public abstract float getEndX();

  /**
   * Sets the x coordinate of the end point.
   * 
   * @param x
   *        The x coordinate of the end point.
   */
  public abstract void setEndX(float x);

  /**
   * Returns the y coordinate of the end point.
   * 
   * @return The y coordinate of the end point.
   */
  public abstract float getEndY();

  /**
   * Sets the y coordinate of the end point.
   * 
   * @param y
   *        The y coordinate of the end point.
   */
  public abstract void setEndY(float y);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Line}.
   * 
   * @author Claudius Korzen
   */
  public interface LineFactory {
<<<<<<< HEAD
    /**
     * Creates a new line.
     * 
     * @return An instance of {@PdfLine}.
     */
    Line create();
=======

>>>>>>> 4a83aad6ffeebc5358f26a771b31e6e9388fa3fe
  }
}
