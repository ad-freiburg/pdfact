package icecite.utils.geometric;

/**
 * The interface that declares the methods of a 2-dim point.
 * 
 * @author Claudius Korzen.
 */
public abstract class Point extends Geometric {
  /**
   * Returns the x coordinate of this point.
   * 
   * @return The x coordinate of this point.
   */
  public abstract float getX();

  /**
   * Sets the x coordinate of this point.
   * 
   * @param x
   *        The x coordinate of this point.
   */
  public abstract void setX(float x);

  /**
   * Returns the y coordinate of this point.
   * 
   * @return The y coordinate of this point.
   */
  public abstract float getY();

  /**
   * Sets the y coordinate of this point.
   * 
   * @param y
   *        The y coordinate of this point.
   */
  public abstract void setY(float y);
}
