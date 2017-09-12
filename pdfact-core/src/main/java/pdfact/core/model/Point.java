package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A single, 2-dim point.
 * 
 * @author Claudius Korzen.
 */
public abstract class Point extends PlainGeometric {
  /**
   * Returns the x-coordinate of this point.
   * 
   * @return The x-coordinate of this point.
   */
  public abstract float getX();

  /**
   * Sets the x-coordinate of this point.
   * 
   * @param x
   *        The x-coordinate of this point.
   */
  public abstract void setX(float x);

  // ==========================================================================

  /**
   * Returns the y-coordinate of this point.
   * 
   * @return The y-coordinate of this point.
   */
  public abstract float getY();

  /**
   * Sets the y-coordinate of this point.
   * 
   * @param y
   *        The y-coordinate of this point.
   */
  public abstract void setY(float y);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Point}.
   * 
   * @author Claudius Korzen
   */
  public interface PointFactory {
    /**
     * Creates a new instance of {@link Point}.
     * 
     * @return A new instance of {@link Point}.
     */
    Point create();

    /**
     * Creates a new instance of {@link Point}.
     * 
     * @param x
     *        The x-coordinate of the point
     * @param y
     *        The y-coordinate of the point
     * 
     * @return A new instance of {@link Point}.
     */
    Point create(@Assisted("x") float x, @Assisted("y") float y);

    /**
     * Creates a new instance of {@link Point}.
     * 
     * @param x
     *        The x-coordinate of the point
     * @param y
     *        The y-coordinate of the point
     * 
     * @return A new instance of {@link Point}.
     */
    Point create(@Assisted("x") double x, @Assisted("y") double y);
  }
}
