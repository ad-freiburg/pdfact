package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A simple (geometric) rectangle.
 * 
 * @author Claudius Korzen.
 */
public interface Rectangle {
  /**
   * Returns the minimum x-coordinate of this rectangle.
   * 
   * @return The minimum x-coordinate of this rectangle.
   */
  float getMinX();

  /**
   * Sets the minimum x-coordinate of this rectangle.
   * 
   * @param minX
   *        The minimum x-coordinate of this rectangle.
   */
  void setMinX(float minX);

  // ==========================================================================

  /**
   * Returns the minimum y-coordinate of this rectangle.
   * 
   * @return The minimum y-coordinate of this rectangle.
   */
  float getMinY();

  /**
   * Sets the minimum y-coordinate of this rectangle.
   * 
   * @param minY
   *        The minimum y-coordinate of this rectangle.
   */
  void setMinY(float minY);

  // ==========================================================================

  /**
   * Returns the maximum x-coordinate of this rectangle.
   * 
   * @return The maximum x-coordinate of this rectangle.
   */
  float getMaxX();

  /**
   * Sets the maximum x-coordinate of this rectangle.
   * 
   * @param maxX
   *        The maximum x-coordinate of this rectangle.
   */
  void setMaxX(float maxX);

  // ==========================================================================

  /**
   * Returns the maximum y-coordinate of this rectangle.
   * 
   * @return The maximum y-coordinate of this rectangle.
   */
  float getMaxY();

  /**
   * Sets the maximum y-coordinate of this rectangle.
   * 
   * @param maxY
   *        The maximum y-coordinate of this rectangle.
   */
  void setMaxY(float maxY);

  // ==========================================================================

  /**
   * Returns the lower left point of this rectangle.
   * 
   * @return The lower left point of this rectangle.
   */
  Point getLowerLeft();

  /**
   * Returns the lower right point of this rectangle.
   * 
   * @return The lower right point of this rectangle.
   */
  Point getLowerRight();

  /**
   * Returns the upper left point of this rectangle.
   * 
   * @return The upper left point of this rectangle.
   */
  Point getUpperLeft();

  /**
   * Returns the upper right point of this rectangle.
   * 
   * @return The upper right point of this rectangle.
   */
  Point getUpperRight();

  // ==========================================================================

  /**
   * Returns the midpoint of this rectangle.
   * 
   * @return The midpoint of this rectangle.
   */
  Point getMidpoint();

  /**
   * Returns the midpoint of this rectangle in x-dimension.
   * 
   * @return The midpoint of this rectangle in x-dimension.
   */
  float getXMidpoint();

  /**
   * Returns the midpoint of this rectangle in y-dimension.
   * 
   * @return The midpoint of this rectangle in y-dimension.
   */
  float getYMidpoint();

  // ==========================================================================

  /**
   * Returns the width of this rectangle.
   * 
   * @return The width of this rectangle.
   */
  float getWidth();

  /**
   * Returns the height of this rectangle.
   * 
   * @return The height of this rectangle.
   */
  float getHeight();

  // ==========================================================================

  /**
   * Get the area of this rectangle.
   * 
   * @return The area of this rectangle.
   */
  float getArea();

  // ==========================================================================

  /**
   * Extends this rectangle by the rectangle of the given element.
   * 
   * @param rect
   *        The element with a rectangle to process.
   */
  void extend(Rectangle rect);

  /**
   * Merges this rectangle with rectangle of the given element. Returns the
   * minimum bounding box that contains both rectangles.
   * 
   * @param rect
   *        The element with a rectangle to process.
   * 
   * @return The minimum bounding box that contains both rectangles.
   */
  Rectangle union(Rectangle rect);

  /**
   * Intersects this rectangle with the rectangle of the given element.
   * 
   * @param rect
   *        The element with a rectangle to process.
   * 
   * @return A rectangle that represents the intersection of both rectangles.
   */
  Rectangle intersection(Rectangle rect);

  // ==========================================================================

  /**
   * Returns true, when this rectangle completely contains the given geometric
   * rectangle.
   * 
   * @param rect
   *        The other geometric object.
   * 
   * @return True, if the bounding box of this geometric object completely
   *         contains the other geometric object.
   */
  boolean contains(Rectangle rect);

  // ==========================================================================

  /**
   * Returns true, if this rectangle overlaps the given rectangle horizontally
   * and vertically (if there is an area that share both objects).
   * 
   * @param rect
   *        The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle.
   */
  boolean overlaps(Rectangle rect);

  /**
   * Computes the overlap ratio of this rectangle with the given rectangle.
   *
   * @param rect
   *        The other geometric rectangle.
   * 
   * @return The overlap ratio of this rectangle with the given rectangle.
   */
  float getOverlapRatio(Rectangle rect);

  // ==========================================================================

  /**
   * Returns true, if this rectangle overlaps the given rectangle horizontally.
   * 
   * @param rect
   *        The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle horizontally.
   */
  boolean overlapsHorizontally(Rectangle rect);

  /**
   * Computes the length of the horizontal overlap between this rectangle and
   * the given rectangle.
   * 
   * @param rect
   *        The other geometric object.
   * 
   * @return The length of the vertical overlap.
   */
  float getHorizontalOverlapLength(Rectangle rect);

  // ==========================================================================
  
  /**
   * Returns true, if this rectangle overlaps the given rectangle vertically.
   * 
   * @param rect
   *        The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle vertically.
   */
  boolean overlapsVertically(Rectangle rect);

  /**
   * Computes the length of the vertical overlap between this rectangle and the
   * given rectangle.
   * 
   * @param rect
   *        The other rectangle.
   * 
   * @return The length of the vertical overlap.
   */
  float getVerticalOverlapLength(Rectangle rect);

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
        @Assisted("minX") float minX,
        @Assisted("minY") float minY,
        @Assisted("maxX") float maxX,
        @Assisted("maxY") float maxY);

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