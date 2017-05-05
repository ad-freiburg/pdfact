package icecite.utils.geometric;

/**
 * The interface that declares the methods of a rectangle.
 * 
 * @author Claudius Korzen.
 */
public abstract class Rectangle extends Geometric {
  /**
   * Returns the minimal x value of this rectangle.
   * 
   * @return The minimal x value of this rectangle.
   */
  public abstract float getMinX();

  /**
   * Sets the minimal x value of this rectangle.
   * 
   * @param minX
   *        The minimal x value of this rectangle.
   */
  public abstract void setMinX(float minX);

  /**
   * Returns the minimal y value of this rectangle.
   * 
   * @return The minimal y value of this rectangle.
   */
  public abstract float getMinY();

  /**
   * Sets the minimal y value of this rectangle.
   * 
   * @param minY
   *        The minimal y value of this rectangle.
   */
  public abstract void setMinY(float minY);

  /**
   * Returns the maximal x value of this rectangle.
   * 
   * @return The maximal x value of this rectangle.
   */
  public abstract float getMaxX();

  /**
   * Sets the maximal x value of this rectangle.
   * 
   * @param maxX
   *        The maximal x value of this rectangle.
   */
  public abstract void setMaxX(float maxX);

  /**
   * Returns the maximal y value of this rectangle.
   * 
   * @return The maximal y value of this rectangle.
   */
  public abstract float getMaxY();

  /**
   * Sets the maximal y value of this rectangle.
   * 
   * @param maxY
   *        The maximal y value of this rectangle.
   */
  public abstract void setMaxY(float maxY);

  /**
   * Returns the lower left point of this rectangle.
   * 
   * @return The lower left point of this rectangle.
   */
  public abstract Point getLowerLeft();

  /**
   * Returns the lower right point of this rectangle.
   * 
   * @return The lower right point of this rectangle.
   */
  public abstract Point getLowerRight();

  /**
   * Returns the lower left point of this rectangle.
   * 
   * @return the lower left point of this rectangle.
   */
  public abstract Point getUpperLeft();

  /**
   * Returns the upper right point of this rectangle.
   * 
   * @return The upper right point of this rectangle.
   */
  public abstract Point getUpperRight();

  /**
   * Returns the midpoint of this rectangle.
   * 
   * @return The midpoint of this rectangle.
   */
  public abstract Point getMidpoint();

  /**
   * Returns the midpoint of this rectangle in x dimension.
   * 
   * @return The midpoint of this rectangle in x dimension.
   */
  public abstract float getXMidpoint();

  /**
   * Returns the midpoint of this rectangle in y dimension.
   * 
   * @return The midpoint of this rectangle in y dimension.
   */
  public abstract float getYMidpoint();

  /**
   * Returns the width of this rectangle.
   * 
   * @return The width of this rectangle.
   */
  public abstract float getWidth();

  /**
   * Returns the height of this rectangle.
   * 
   * @return The height of this rectangle.
   */
  public abstract float getHeight();

  /**
   * Get the area of this rectangle.
   * 
   * @return The area of this rectangle.
   */
  public abstract float getArea();

  /**
   * Computes the overlap ratio of this rectangle with the given geometric.
   *
   * @param geom
   *        The other geoemtric object.
   * @return The overlap ratio of this rectangle with the given geometric.
   */
  public abstract float getOverlapRatio(Geometric geom);

  /**
   * Merges this rect with given rect. Returns the minimum bounding box that
   * contains both rectangles.
   * 
   * @param rect
   *        The other rectangle.
   * 
   * @return A new, merged rectangle object.
   */
  public abstract Rectangle union(Rectangle rect);

  /**
   * Intersects this rectangle with the given other rectangle.
   * 
   * @param rect
   *        The rectangle to process.
   * 
   * @return A rectangle, representing the intersection.
   */
  public abstract Rectangle intersection(Rectangle rect);
}