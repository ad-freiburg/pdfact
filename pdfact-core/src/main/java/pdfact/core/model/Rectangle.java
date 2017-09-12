package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A simple (geometric rectangle).
 * 
 * @author Claudius Korzen.
 */
public abstract class Rectangle extends PlainGeometric {
  /**
   * Returns the minimum x-coordinate of this rectangle.
   * 
   * @return The minimum x-coordinate of this rectangle.
   */
  public abstract float getMinX();

  /**
   * Sets the minimum x-coordinate of this rectangle.
   * 
   * @param minX
   *        The minimum x-coordinate of this rectangle.
   */
  public abstract void setMinX(float minX);

  // ==========================================================================

  /**
   * Returns the minimum y-coordinate of this rectangle.
   * 
   * @return The minimum y-coordinate of this rectangle.
   */
  public abstract float getMinY();

  /**
   * Sets the minimum y-coordinate of this rectangle.
   * 
   * @param minY
   *        The minimum y-coordinate of this rectangle.
   */
  public abstract void setMinY(float minY);

  // ==========================================================================

  /**
   * Returns the maximum x-coordinate of this rectangle.
   * 
   * @return The maximum x-coordinate of this rectangle.
   */
  public abstract float getMaxX();

  /**
   * Sets the maximum x-coordinate of this rectangle.
   * 
   * @param maxX
   *        The maximum x-coordinate of this rectangle.
   */
  public abstract void setMaxX(float maxX);

  // ==========================================================================

  /**
   * Returns the maximum y-coordinate of this rectangle.
   * 
   * @return The maximum y-coordinate of this rectangle.
   */
  public abstract float getMaxY();

  /**
   * Sets the maximum y-coordinate of this rectangle.
   * 
   * @param maxY
   *        The maximum y-coordinate of this rectangle.
   */
  public abstract void setMaxY(float maxY);

  // ==========================================================================

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
   * Returns the upper left point of this rectangle.
   * 
   * @return The upper left point of this rectangle.
   */
  public abstract Point getUpperLeft();

  /**
   * Returns the upper right point of this rectangle.
   * 
   * @return The upper right point of this rectangle.
   */
  public abstract Point getUpperRight();

  // ==========================================================================

  /**
   * Returns the midpoint of this rectangle.
   * 
   * @return The midpoint of this rectangle.
   */
  public abstract Point getMidpoint();

  /**
   * Returns the midpoint of this rectangle in x-dimension.
   * 
   * @return The midpoint of this rectangle in x-dimension.
   */
  public abstract float getXMidpoint();

  /**
   * Returns the midpoint of this rectangle in y-dimension.
   * 
   * @return The midpoint of this rectangle in y-dimension.
   */
  public abstract float getYMidpoint();

  // ==========================================================================

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

  // ==========================================================================

  /**
   * Get the area of this rectangle.
   * 
   * @return The area of this rectangle.
   */
  public abstract float getArea();

  // ==========================================================================

  /**
   * Computes the overlap ratio of this rectangle with the given geometric
   * object.
   *
   * @param geom
   *        The other geometric object.
   * 
   * @return The overlap ratio of this rectangle with the given geometric
   *         object.
   */
  public abstract float getOverlapRatio(PlainGeometric geom);

  // ==========================================================================

  /**
   * Extends this rectangle by the rectangle of the given element.
   * 
   * @param rect
   *        The element with a rectangle to process.
   */
  public abstract void extend(HasPosition rect);

  /**
   * Merges this rectangle with rectangle of the given element. Returns the
   * minimum bounding box that contains both rectangles.
   * 
   * @param rect
   *        The element with a rectangle to process.
   * 
   * @return The minimum bounding box that contains both rectangles.
   */
  public abstract Rectangle union(HasRectangle rect);

  /**
   * Intersects this rectangle with the rectangle of the given element.
   * 
   * @param rect
   *        The element with a rectangle to process.
   * 
   * @return A rectangle that represents the intersection of both rectangles.
   */
  public abstract Rectangle intersection(HasRectangle rect);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Rectangle}.
   * 
   * @author Claudius Korzen
   */
  public interface RectangleFactory {
    /**
     * Creates a new instance of {@link Rectangle}.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle create();

    /**
     * Creates a copy of the given {@link Rectangle}.
     * 
     * @param rect
     *        The rectangle to create a copy from.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle create(Rectangle rect);

    /**
     * Creates a new instance of {@link Rectangle} from given rectangle.
     * 
     * @param rect
     *        The rectangle to create a copy from.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle create(java.awt.Rectangle rect);

    /**
     * Creates a new instance of {@link Rectangle}.
     * 
     * @param lowerLeft
     *        The lower left point of the rectangle.
     * @param upperRight
     *        The upper right point of the rectangle.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle create(
        @Assisted("lowerLeft") Point lowerLeft,
        @Assisted("upperRight") Point upperRight);

    /**
     * Creates a new instance of {@link Rectangle}.
     * 
     * @param minX
     *        The x-coordinate of the lower left point of the rectangle.
     * @param minY
     *        The y-coordinate of the lower left point of the rectangle.
     * @param maxX
     *        The x-coordinate of the upper right point of the rectangle.
     * @param maxY
     *        The y-coordinate of the upper right point of the rectangle.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle create(
        @Assisted("minX") double minX,
        @Assisted("minY") double minY,
        @Assisted("maxX") double maxX,
        @Assisted("maxY") double maxY);

    /**
     * Creates a new instance of {@link Rectangle} from the union of the given
     * rectangles.
     * 
     * @param rectangles
     *        The rectangle to process.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle fromUnion(@Assisted Rectangle... rectangles);

    /**
     * Creates a new instance of {@link Rectangle} that represents the bounding
     * box around the given elements that have a single position.
     * 
     * @param elements
     *        The elements to process.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle fromHasPositionElements(
        @Assisted("hasPosition") Iterable<? extends HasPosition> elements);

    /**
     * Creates a new instance of {@link Rectangle} that represents the bounding
     * box around the given elements that have multiple positions.
     * 
     * @param elements
     *        The elements to process.
     * 
     * @return A new instance of {@link Rectangle}.
     */
    Rectangle fromHasPositionsElements(
        @Assisted("hasPositions") Iterable<? extends HasPositions> elements);
  }
}