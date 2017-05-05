package icecite.utils.geometric;

import com.google.inject.assistedinject.Assisted;

/**
 * The factory to create instances of {@link Point}.
 * 
 * @author Claudius Korzen
 */
public interface PointFactory {
  /**
   * Creates a new Point.
   * 
   * @return An instance of {@link Point}.
   */
  Point create();

  /**
   * Creates a new Point.
   * 
   * @param x
   *        The x value of the point
   * @param y
   *        The y value of the point
   * 
   * @return An instance of {@link Point}.
   */
  Point create(@Assisted("x") float x, @Assisted("y") float y);

  /**
   * Creates a new Point.
   * 
   * @param x
   *        The x value of the point
   * @param y
   *        The y value of the point
   * 
   * @return An instance of {@link Point}.
   */
  Point create(@Assisted("x") double x, @Assisted("y") double y);
}
