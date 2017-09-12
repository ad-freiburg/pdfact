package pdfact.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.PdfDocument.PdfDocumentFactory;

/**
 * A plain implementation of {@link Rectangle}.
 * 
 * @author Claudius Korzen
 */
public class PlainRectangle extends Rectangle {
  /**
   * The x-coordinate of the lower left point of the rectangle.
   */
  protected float minX = Float.MAX_VALUE;

  /**
   * The y-coordinate of the lower left point of the rectangle.
   */
  protected float minY = Float.MAX_VALUE;

  /**
   * The x-coordinate of the upper right point of the rectangle.
   */
  protected float maxX = -Float.MAX_VALUE;

  /**
   * The y-coordinate of the upper right point of the rectangle.
   */
  protected float maxY = -Float.MAX_VALUE;

  /**
   * Creates a new rectangle with the lower left point (0, 0) and the upper
   * right point (0, 0).
   */
  @AssistedInject
  public PlainRectangle() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(@Assisted Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(@Assisted java.awt.Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle with the given lower left point and the given upper
   * right point.
   * 
   * @param ll
   *        The lower left point.
   * @param ur
   *        The upper right point.
   */
  @AssistedInject
  public PlainRectangle(
      @Assisted("lowerLeft") Point ll,
      @Assisted("upperRight") Point ur) {
    this(ll.getX(), ll.getY(), ur.getX(), ur.getY());
  }

  /**
   * Creates a new rectangle with the given coordinates.
   * 
   * @param minX
   *        The x-coordinate of the lower left point of the rectangle.
   * @param minY
   *        The y-coordinate of the lower left point of the rectangle.
   * @param maxX
   *        The x-coordinate of the upper right point of the rectangle.
   * @param maxY
   *        The y-coordinate of the upper right point of the rectangle.
   */
  @AssistedInject
  public PlainRectangle(
      @Assisted("minX") double minX,
      @Assisted("minY") double minY,
      @Assisted("maxX") double maxX,
      @Assisted("maxY") double maxY) {
    this((float) minX, (float) minY, (float) maxX, (float) maxY);
  }

  /**
   * Creates a new rectangle with the lower left point (minX, minY) and the
   * upper right (maxX, maxY).
   * 
   * @param minX
   *        The minimum x-coordinate.
   * @param minY
   *        The minimum y-coordinate.
   * @param maxX
   *        The maximum x-coordinate.
   * @param maxY
   *        The maximum y-coordinate.
   */
  @AssistedInject
  public PlainRectangle(
      @Assisted("minX") float minX,
      @Assisted("minY") float minY,
      @Assisted("maxX") float maxX,
      @Assisted("maxY") float maxY) {
    setMinX(minX);
    setMinY(minY);
    setMaxX(maxX);
    setMaxY(maxY);
  }

  /**
   * Creates a new rectangle from the union of the given rectangles.
   * 
   * @param rectangles
   *        The rectangles to process.
   */
  @AssistedInject
  public PlainRectangle(@Assisted Rectangle... rectangles) {
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
   * @param elements
   *        The elements to process.
   */
  @AssistedInject
  public PlainRectangle(
      @Assisted("hasPosition") Iterable<? extends HasPosition> elements) {
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

  /**
   * Creates a new rectangle that represents the bounding box around the given
   * elements that have multiple positions.
   * 
   * @param xxx
   *        Only there to get a constructor with unique erasure.
   * @param elements
   *        The elements to process.
   */
  @AssistedInject
  // TODO: PdfDocumentFactory is only injected to get an constructor with
  // another erasure than the previous constructor. Find out how to deal with
  // constructors with same erasure correctly.
  public PlainRectangle(PdfDocumentFactory xxx,
      @Assisted("hasPositions") Iterable<? extends HasPositions> elements) {
    for (HasPositions element : elements) {
      List<Position> positions = element.getPositions();
      for (Position position : positions) {
        Rectangle rectangle = position.getRectangle();
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
  }

  // ==========================================================================

  /**
   * Creates a new rectangle from the two given points. The points need not to
   * be in any special order.
   *
   * @param point1
   *        The first point.
   * @param point2
   *        The second point.
   * 
   * @return A rectangle that is spanned by point1 and point2.
   */
  public static Rectangle from2Points(Point point1, Point point2) {
    float x1 = point1.getX();
    float y1 = point1.getY();
    float x2 = point2.getX();
    float y2 = point2.getY();

    float minX = Math.min(x1, x2);
    float minY = Math.min(y1, y2);
    float maxX = Math.max(x1, x2);
    float maxY = Math.max(y1, y2);

    // TODO: Use Guice here.
    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new rectangle from the four given points. The points need not to
   * be in any special order.
   *
   * @param point1
   *        The first point.
   * @param point2
   *        The second point.
   * @param point3
   *        The third point.
   * @param point4
   *        The fourth point.
   * 
   * @return A rectangle that is spanned by point1, point2, point3 and point4.
   */
  public static Rectangle from4Points(Point point1, Point point2,
      Point point3, Point point4) {
    float x1 = point1.getX();
    float y1 = point1.getY();
    float x2 = point2.getX();
    float y2 = point2.getY();
    float x3 = point3.getX();
    float y3 = point3.getY();
    float x4 = point4.getX();
    float y4 = point4.getY();

    float minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
    float minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
    float maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
    float maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

    // TODO: Use Guice here.
    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  // ==========================================================================

  /**
   * Creates a new rectangle from the union of the two given elements that have
   * a rectangle.
   * 
   * @param hb1
   *        The first element that has a rectangle.
   * @param hb2
   *        The second element that has a rectangle.
   * 
   * @return A rectangle that represents the union of the two rectangles.
   */
  public static Rectangle fromUnion(HasPosition hb1, HasPosition hb2) {
    Rectangle rect1 = hb1.getPosition().getRectangle();
    Rectangle rect2 = hb2.getPosition().getRectangle();

    float minX = Math.min(rect1.getMinX(), rect2.getMinX());
    float maxX = Math.max(rect1.getMaxX(), rect2.getMaxX());
    float minY = Math.min(rect1.getMinY(), rect2.getMinY());
    float maxY = Math.max(rect1.getMaxY(), rect2.getMaxY());

    // TODO: Use Guice here.
    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  // ==========================================================================

  /**
   * Creates a new rectangle from the bounding box around all given elements.
   * 
   * @param elements
   *        The elements to process.
   * 
   * @return The bounding box.
   */
  public static Rectangle fromBoundingBoxOf(HasPosition... elements) {
    return fromBoundingBoxOf(new ArrayList<>(Arrays.asList(elements)));
  }

  /**
   * Creates a new rectangle from the bounding box around all given elements.
   * 
   * @param elements
   *        The elements to process.
   * 
   * @return The bounding box.
   */
  public static Rectangle fromBoundingBoxOf(
      Iterable<? extends HasPosition> elements) {
    if (elements == null) {
      return null;
    }

    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;

    for (HasPosition object : elements) {
      Rectangle rectangle = object.getPosition().getRectangle();
      if (rectangle.getMinX() < minX) {
        minX = rectangle.getMinX();
      }

      if (rectangle.getMinY() < minY) {
        minY = rectangle.getMinY();
      }

      if (rectangle.getMaxX() > maxX) {
        maxX = rectangle.getMaxX();
      }

      if (rectangle.getMaxY() > maxY) {
        maxY = rectangle.getMaxY();
      }
    }
    // TODO: Use Guice here.
    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this;
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    setMinX(boundingBox.getMinX());
    setMinY(boundingBox.getMinY());
    setMaxX(boundingBox.getMaxX());
    setMaxY(boundingBox.getMaxY());
  }

  // ==========================================================================

  @Override
  public float getMinX() {
    return this.minX;
  }

  @Override
  public void setMinX(float minX) {
    this.minX = minX;
  }

  // ==========================================================================

  @Override
  public float getMinY() {
    return this.minY;
  }

  @Override
  public void setMinY(float minY) {
    this.minY = minY;
  }

  // ==========================================================================

  @Override
  public float getMaxX() {
    return this.maxX;
  }

  @Override
  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  // ==========================================================================

  @Override
  public float getMaxY() {
    return this.maxY;
  }

  @Override
  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  // ==========================================================================

  @Override
  public Point getLowerLeft() {
    // TODO: Use Guice here.
    return new PlainPoint(this.minX, this.minY);
  }

  @Override
  public Point getUpperLeft() {
    // TODO: Use Guice here.
    return new PlainPoint(this.minX, this.maxY);
  }

  @Override
  public Point getLowerRight() {
    // TODO: Use Guice here.
    return new PlainPoint(this.maxX, this.minY);
  }

  @Override
  public Point getUpperRight() {
    // TODO: Use Guice here.
    return new PlainPoint(this.maxX, this.maxY);
  }

  // ==========================================================================

  @Override
  public Point getMidpoint() {
    return new PlainPoint(getXMidpoint(), getYMidpoint());
  }

  @Override
  public float getXMidpoint() {
    return this.minX + (getWidth() / 2f);
  }

  @Override
  public float getYMidpoint() {
    return this.minY + (getHeight() / 2f);
  }

  // ==========================================================================

  @Override
  public float getWidth() {
    return this.maxX - this.minX;
  }

  @Override
  public float getHeight() {
    return this.maxY - this.minY;
  }

  // ==========================================================================

  @Override
  public float getArea() {
    return getWidth() * getHeight();
  }

  // ==========================================================================

  @Override
  public float getOverlapRatio(PlainGeometric geom) {
    return computeOverlapArea(geom) / getArea();
  }

  // ==========================================================================

  @Override
  public void extend(HasPosition rect) {
    setMinX(Math.min(getMinX(), rect.getPosition().getRectangle().getMinX()));
    setMaxX(Math.max(getMaxX(), rect.getPosition().getRectangle().getMaxX()));
    setMinY(Math.min(getMinY(), rect.getPosition().getRectangle().getMinY()));
    setMaxY(Math.max(getMaxY(), rect.getPosition().getRectangle().getMaxY()));
  }

  @Override
  public Rectangle union(HasRectangle rect) {
    float minX = Math.min(getMinX(), rect.getRectangle().getMinX());
    float maxX = Math.max(getMaxX(), rect.getRectangle().getMaxX());
    float minY = Math.min(getMinY(), rect.getRectangle().getMinY());
    float maxY = Math.max(getMaxY(), rect.getRectangle().getMaxY());

    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  @Override
  public Rectangle intersection(HasRectangle rect) {
    float maxMinX = Math.max(getMinX(), rect.getRectangle().getMinX());
    float minMaxX = Math.min(getMaxX(), rect.getRectangle().getMaxX());
    float maxMinY = Math.max(getMinY(), rect.getRectangle().getMinY());
    float minMaxY = Math.min(getMaxY(), rect.getRectangle().getMaxY());

    if (minMaxX <= maxMinX) {
      return null;
    }

    if (minMaxY <= maxMinY) {
      return null;
    }

    return new PlainRectangle(maxMinX, maxMinY, minMaxX, minMaxY);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "Rectangle(" + getMinX() + "," + getMinY() + "," + getMaxX()
        + "," + getMaxY() + ")";
  }

  // ==========================================================================

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
