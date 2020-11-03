package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A simple (geometric) rectangle.
 * 
 * @author Claudius Korzen.
 */
public class Rectangle {
  /**
   * The x-coordinate of the lower left point of this rectangle.
   */
  protected float minX = Float.MAX_VALUE;

  /**
   * The y-coordinate of the lower left point of this rectangle.
   */
  protected float minY = Float.MAX_VALUE;

  /**
   * The x-coordinate of the upper right point of this rectangle.
   */
  protected float maxX = -Float.MAX_VALUE;

  /**
   * The y-coordinate of the upper right point of this rectangle.
   */
  protected float maxY = -Float.MAX_VALUE;

  /**
   * Creates a new rectangle with the lower left point (0, 0) and the upper right
   * point (0, 0).
   */
  public Rectangle() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a copy of the given rectangle.
   *
   * @param rect The rectangle to copy.
   */
  public Rectangle(Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rect The rectangle to copy.
   */
  public Rectangle(java.awt.Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle that is spanned by the given lower left point and the
   * given upper right point.
   *
   * @param ll The lower left point.
   * @param ur The upper right point.
   */
  public Rectangle(Point ll, Point ur) {
    this(ll.getX(), ll.getY(), ur.getX(), ur.getY());
  }

  /**
   * Creates a new rectangle with the given coordinates.
   * 
   * @param minX The x-coordinate of the lower left point of this rectangle.
   * @param minY The y-coordinate of the lower left point of this rectangle.
   * @param maxX The x-coordinate of the upper right point of this rectangle.
   * @param maxY The y-coordinate of the upper right point of this rectangle.
   */
  public Rectangle(double minX, double minY, double maxX, double maxY) {
    this((float) minX, (float) minY, (float) maxX, (float) maxY);
  }

  /**
   * Creates a new rectangle with the lower left point (minX, minY) and the upper
   * right (maxX, maxY).
   * 
   * @param minX The x-coordinate of the lower left point of this rectangle.
   * @param minY The y-coordinate of the lower left point of this rectangle.
   * @param maxX The x-coordinate of the upper right point of this rectangle.
   * @param maxY The y-coordinate of the upper right point of this rectangle.
   */
  public Rectangle(float minX, float minY, float maxX, float maxY) {
    setMinX(minX);
    setMinY(minY);
    setMaxX(maxX);
    setMaxY(maxY);
  }

  /**
   * Creates a new rectangle from the union of the given rectangles.
   * 
   * @param rectangles The rectangles to process.
   */
  public Rectangle(Rectangle... rectangles) {
    for (Rectangle rect : rectangles) {
      if (rect.getMinX() < getMinX()) {
        setMinX(rect.getMinX());
      }

      if (rect.getMinY() < getMinY()) {
        setMinY(rect.getMinY());
      }

      if (rect.getMaxX() > getMaxX()) {
        setMaxX(rect.getMaxX());
      }

      if (rect.getMaxY() > getMaxY()) {
        setMaxY(rect.getMaxY());
      }
    }
  }

  /**
   * Creates a new rectangle that represents the bounding box around the given
   * elements that have a single position.
   * 
   * @param elements The elements to process.
   */
  public Rectangle(Iterable<? extends HasPosition> elements) {
    for (HasPosition element : elements) {
      Rectangle rectangle = element.getPosition().getRectangle();
      if (rectangle.getMinX() < getMinX()) {
        setMinX(rectangle.getMinX());
      }

      if (rectangle.getMinY() < getMinY()) {
        setMinY(rectangle.getMinY());
      }

      if (rectangle.getMaxX() > getMaxX()) {
        setMaxX(rectangle.getMaxX());
      }

      if (rectangle.getMaxY() > getMaxY()) {
        setMaxY(rectangle.getMaxY());
      }
    }
  }

  // ==============================================================================================

  /**
   * Returns the minimum x-coordinate of this rectangle.
   * 
   * @return The minimum x-coordinate of this rectangle.
   */
  public float getMinX() {
    return this.minX;
  }

  /**
   * Sets the minimum x-coordinate of this rectangle.
   * 
   * @param minX The minimum x-coordinate of this rectangle.
   */
  public void setMinX(float minX) {
    this.minX = minX;
  }

  // ==============================================================================================

  /**
   * Returns the minimum y-coordinate of this rectangle.
   * 
   * @return The minimum y-coordinate of this rectangle.
   */
  public float getMinY() {
    return this.minY;
  }

  /**
   * Sets the minimum y-coordinate of this rectangle.
   * 
   * @param minY The minimum y-coordinate of this rectangle.
   */
  public void setMinY(float minY) {
    this.minY = minY;
  }

  // ==============================================================================================

  /**
   * Returns the maximum x-coordinate of this rectangle.
   * 
   * @return The maximum x-coordinate of this rectangle.
   */
  public float getMaxX() {
    return this.maxX;
  }

  /**
   * Sets the maximum x-coordinate of this rectangle.
   * 
   * @param maxX The maximum x-coordinate of this rectangle.
   */
  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  // ==============================================================================================

  /**
   * Returns the maximum y-coordinate of this rectangle.
   * 
   * @return The maximum y-coordinate of this rectangle.
   */
  public float getMaxY() {
    return this.maxY;
  }

  /**
   * Sets the maximum y-coordinate of this rectangle.
   * 
   * @param maxY The maximum y-coordinate of this rectangle.
   */
  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  // ==============================================================================================

  /**
   * Returns the lower left point of this rectangle.
   * 
   * @return The lower left point of this rectangle.
   */
  public Point getLowerLeft() {
    return new Point(this.minX, this.minY);
  }

  /**
   * Returns the lower right point of this rectangle.
   * 
   * @return The lower right point of this rectangle.
   */
  public Point getLowerRight() {
    return new Point(this.maxX, this.minY);
  }

  /**
   * Returns the upper left point of this rectangle.
   * 
   * @return The upper left point of this rectangle.
   */
  public Point getUpperLeft() {
    return new Point(this.minX, this.maxY);
  }

  /**
   * Returns the upper right point of this rectangle.
   * 
   * @return The upper right point of this rectangle.
   */
  public Point getUpperRight() {
    return new Point(this.maxX, this.maxY);
  }

  // ==============================================================================================

  /**
   * Returns the midpoint of this rectangle.
   * 
   * @return The midpoint of this rectangle.
   */
  public Point getMidpoint() {
    return new Point(getXMidpoint(), getYMidpoint());
  }

  /**
   * Returns the midpoint of this rectangle in x-dimension.
   * 
   * @return The midpoint of this rectangle in x-dimension.
   */
  public float getXMidpoint() {
    return getMinX() + (getWidth() / 2f);
  }

  /**
   * Returns the midpoint of this rectangle in y-dimension.
   * 
   * @return The midpoint of this rectangle in y-dimension.
   */
  public float getYMidpoint() {
    return getMinY() + (getHeight() / 2f);
  }

  // ==============================================================================================

  /**
   * Returns the width of this rectangle.
   * 
   * @return The width of this rectangle.
   */
  public float getWidth() {
    return getMaxX() - getMinX();
  }

  /**
   * Returns the height of this rectangle.
   * 
   * @return The height of this rectangle.
   */
  public float getHeight() {
    return getMaxY() - getMinY();
  }

  // ==============================================================================================

  /**
   * Get the area of this rectangle.
   * 
   * @return The area of this rectangle.
   */
  public float getArea() {
    return getWidth() * getHeight();
  }

  // ==============================================================================================

  /**
   * Extends this rectangle by the rectangle of the given element.
   * 
   * @param rect The element with a rectangle to process.
   */
  public void extend(Rectangle rect) {
    if (rect == null) {
      return;
    }
    setMinX(Math.min(getMinX(), rect.getMinX()));
    setMaxX(Math.max(getMaxX(), rect.getMaxX()));
    setMinY(Math.min(getMinY(), rect.getMinY()));
    setMaxY(Math.max(getMaxY(), rect.getMaxY()));
  }

  /**
   * Merges this rectangle with rectangle of the given element. Returns the
   * minimum bounding box that contains both rectangles.
   * 
   * @param rect The element with a rectangle to process.
   * 
   * @return The minimum bounding box that contains both rectangles.
   */
  public Rectangle union(Rectangle rect) {
    if (rect == null) {
      return null;
    }

    float minX = Math.min(getMinX(), rect.getMinX());
    float maxX = Math.max(getMaxX(), rect.getMaxX());
    float minY = Math.min(getMinY(), rect.getMinY());
    float maxY = Math.max(getMaxY(), rect.getMaxY());

    return new Rectangle(minX, minY, maxX, maxY);
  }

  /**
   * Intersects this rectangle with the rectangle of the given element.
   * 
   * @param rect The element with a rectangle to process.
   * 
   * @return A rectangle that represents the intersection of both rectangles.
   */
  public Rectangle intersection(Rectangle rect) {
    if (rect == null) {
      return null;
    }

    float maxMinX = Math.max(getMinX(), rect.getMinX());
    float minMaxX = Math.min(getMaxX(), rect.getMaxX());
    float maxMinY = Math.max(getMinY(), rect.getMinY());
    float minMaxY = Math.min(getMaxY(), rect.getMaxY());

    if (minMaxX <= maxMinX) {
      return null;
    }

    if (minMaxY <= maxMinY) {
      return null;
    }

    return new Rectangle(maxMinX, maxMinY, minMaxX, minMaxY);
  }

  // ==============================================================================================

  /**
   * Returns true, when this rectangle completely contains the given geometric
   * rectangle.
   * 
   * @param rect The other geometric object.
   * 
   * @return True, if the bounding box of this geometric object completely
   *         contains the other geometric object.
   */
  public boolean contains(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    if (rect.getMinX() < getMinX()) {
      return false;
    }
    if (rect.getMaxX() > getMaxX()) {
      return false;
    }
    if (rect.getMinY() < getMinY()) {
      return false;
    }
    if (rect.getMaxY() > getMaxY()) {
      return false;
    }
    return true;
  }

  // ==============================================================================================

  /**
   * Returns true, if this rectangle overlaps the given rectangle horizontally and
   * vertically (if there is an area that share both objects).
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle.
   */
  public boolean overlaps(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return overlapsHorizontally(rect) && overlapsVertically(rect);
  }

  /**
   * Computes the overlap ratio of this rectangle with the given rectangle.
   *
   * @param rect The other geometric rectangle.
   * 
   * @return The overlap ratio of this rectangle with the given rectangle.
   */
  public float getOverlapRatio(Rectangle rect) {
    if (getArea() <= 0) {
      return 0;
    }
    float horizontalOverlapLength = getHorizontalOverlapLength(rect);
    float verticalOverlapLength = getVerticalOverlapLength(rect);
    return (horizontalOverlapLength * verticalOverlapLength) / getArea();
  }

  // ==============================================================================================

  /**
   * Returns true, if this rectangle overlaps the given rectangle horizontally.
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle horizontally.
   */
  public boolean overlapsHorizontally(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMaxX() >= rect.getMinX() && getMinX() <= rect.getMaxX();
  }

  /**
   * Computes the length of the horizontal overlap between this rectangle and the
   * given rectangle.
   * 
   * @param rect The other geometric object.
   * 
   * @return The length of the vertical overlap.
   */
  public float getHorizontalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxX = Math.min(getMaxX(), rect.getMaxX());
      float maxMinX = Math.max(getMinX(), rect.getMinX());
      return Math.max(0, minMaxX - maxMinX);
    }
    return 0;
  }

  // ==============================================================================================

  /**
   * Returns true, if this rectangle overlaps the given rectangle vertically.
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle vertically.
   */
  public boolean overlapsVertically(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMinY() <= rect.getMaxY() && getMaxY() >= rect.getMinY();
  }

  /**
   * Computes the length of the vertical overlap between this rectangle and the
   * given rectangle.
   * 
   * @param rect The other rectangle.
   * 
   * @return The length of the vertical overlap.
   */
  public float getVerticalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxY = Math.min(getMaxY(), rect.getMaxY());
      float maxMinY = Math.max(getMinY(), rect.getMinY());
      return Math.max(0, minMaxY - maxMinY);
    }
    return 0;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Rectangle(" + getMinX() + "," + getMinY() + "," + getMaxX() + "," + getMaxY() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Rectangle) {
      Rectangle otherRectangle = (Rectangle) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getMinX(), otherRectangle.getMinX());
      builder.append(getMinY(), otherRectangle.getMinY());
      builder.append(getMaxX(), otherRectangle.getMaxX());
      builder.append(getMaxY(), otherRectangle.getMaxY());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getMinX());
    builder.append(getMinY());
    builder.append(getMaxX());
    builder.append(getMaxY());
    return builder.hashCode();
  }
}