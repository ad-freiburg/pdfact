package pdfact.model.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.HasPosition;
import pdfact.model.HasPositions;
import pdfact.model.Point;
import pdfact.model.PdfDocument.PdfDocumentFactory;
import pdfact.model.Position;
import pdfact.model.Rectangle;

/**
 * A plain implementation of {@link Rectangle} where (0,0) is in the lower left
 * and increasing coordinates to the upper right.
 * 
 * @author Claudius Korzen
 * 
 */
public class PlainRectangle extends Rectangle {
  /**
   * The x value of the lower left.
   */
  protected float minX = Float.MAX_VALUE;

  /**
   * The y value of the lower left.
   */
  protected float minY = Float.MAX_VALUE;

  /**
   * The x value of the upper right.
   */
  protected float maxX = -Float.MAX_VALUE;

  /**
   * The y value of the upper right.
   */
  protected float maxY = -Float.MAX_VALUE;

  /**
   * Creates a new rectangle that is spanned by the points (0, 0) and (0, 0).
   */
  @AssistedInject
  public PlainRectangle() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a new rectangle with the same position and dimensions of the given
   * rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(@Assisted Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle with the same position and dimensions of the given
   * rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(@Assisted java.awt.Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle that is spanned by point1 and point2.
   * 
   * @param point1
   *        The lower left point.
   * @param point2
   *        The upper right point.
   */
  @AssistedInject
  public PlainRectangle(
      @Assisted("point1") Point point1,
      @Assisted("point2") Point point2) {
    this(point1.getX(), point1.getY(), point2.getX(), point2.getY());
  }

  /**
   * Creates a new rectangle with the given coordinates.
   * 
   * @param minX
   *        The x coordinate of the lower left vertex.
   * @param minY
   *        The y coordinate of the lower left vertex.
   * @param maxX
   *        The x coordinate of the upper right vertex.
   * @param maxY
   *        The y coordinate of the upper right vertex.
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
   * Creates a new rectangle that is spanned by the points (minX, minY) and
   * (maxX, maxY).
   * 
   * @param minX
   *        The minimum x value.
   * @param minY
   *        The minimum y value.
   * @param maxX
   *        The maximum x value.
   * @param maxY
   *        The maximum y value.
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
   * @param rectangles The rectangles to process.
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
   * Creates a rectangle that represents the bounding box around the given
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
   * Creates a rectangle that represents the bounding box around the given
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
   * Creates a new rectangle from the 2 given vertices. The vertices need not be
   * in any special order.
   *
   * @param point1
   *        The first vertex.
   * @param point2
   *        The second vertex.
   * @return The constructed rectangle.
   */
  public static Rectangle from2Vertices(Point point1, Point point2) {
    float x1 = point1.getX();
    float y1 = point1.getY();
    float x2 = point2.getX();
    float y2 = point2.getY();

    float minX = Math.min(x1, x2);
    float minY = Math.min(y1, y2);
    float maxX = Math.max(x1, x2);
    float maxY = Math.max(y1, y2);

    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new rectangles that results from the union of the two given
   * rectangles.
   * 
   * @param hb1
   *        The first rectangle.
   * @param hb2
   *        The second rectangle.
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

    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new rectangle from the 4 given vertices. The vertices need not be
   * in any special order.
   *
   * @param point1
   *        The first vertex.
   * @param point2
   *        The second vertex.
   * @param point3
   *        The third vertex.
   * @param point4
   *        The fourth vertex.
   * @return The constructed rectangle.
   */
  public static Rectangle from4Vertices(Point point1, Point point2,
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

    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  /**
   * Returns the bounding box of all given objects, that is the smallest
   * possible rectangle that includes all the given objects.
   * 
   * @param rects
   *        The objects to include.
   * @return The bounding box.
   */
  public static Rectangle fromBoundingBoxOf(HasPosition... rects) {
    return fromBoundingBoxOf(new ArrayList<>(Arrays.asList(rects)));
  }

  /**
   * Returns the bounding box of all given objects, that is the smallest
   * possible rectangle that includes all the given objects.
   * 
   * @param objects
   *        The objects to include.
   * @return The bounding box.
   */
  public static Rectangle fromBoundingBoxOf(
      Iterable<? extends HasPosition> objects) {
    if (objects == null) {
      return null;
    }

    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;

    for (HasPosition object : objects) {
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
    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  @Override
  public float getMinX() {
    return this.minX;
  }

  @Override
  public void setMinX(float minX) {
    this.minX = minX;
  }

  @Override
  public float getMinY() {
    return this.minY;
  }

  @Override
  public void setMinY(float minY) {
    this.minY = minY;
  }

  @Override
  public float getMaxX() {
    return this.maxX;
  }

  @Override
  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  @Override
  public float getMaxY() {
    return this.maxY;
  }

  @Override
  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  @Override
  public Point getLowerLeft() {
    return new PlainPoint(this.minX, this.minY);
  }

  @Override
  public Point getUpperLeft() {
    return new PlainPoint(this.minX, this.maxY);
  }

  @Override
  public Point getLowerRight() {
    return new PlainPoint(this.maxX, this.minY);
  }

  @Override
  public Point getUpperRight() {
    return new PlainPoint(this.maxX, this.maxY);
  }

  @Override
  public float getWidth() {
    return this.maxX - this.minX;
  }

  @Override
  public float getHeight() {
    return this.maxY - this.minY;
  }

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

  /**
   * Returns true if the given point (x,y) lies inside this rectangle.
   * 
   * @param x
   *        The x-coordinate of point to test.
   * @param y
   *        The y-coordinate of point to test.
   * 
   * @return True if the point is inside this rectangle.
   */
  public boolean contains(float x, float y) { // TODO: Use Geometric.
    return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
  }

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

  @Override
  public float getArea() {
    return getWidth() * getHeight();
  }

  @Override
  public float getOverlapRatio(Geometric geom) {
    return computeOverlapArea(geom) / getArea();
  }

  @Override
  public void extend(HasPosition rect) {
    setMinX(Math.min(getMinX(), rect.getPosition().getRectangle().getMinX()));
    setMaxX(Math.max(getMaxX(), rect.getPosition().getRectangle().getMaxX()));
    setMinY(Math.min(getMinY(), rect.getPosition().getRectangle().getMinY()));
    setMaxY(Math.max(getMaxY(), rect.getPosition().getRectangle().getMaxY()));
  }

  @Override
  public Rectangle union(Rectangle rect) {
    float minX = Math.min(getMinX(), rect.getMinX());
    float maxX = Math.max(getMaxX(), rect.getMaxX());
    float minY = Math.min(getMinY(), rect.getMinY());
    float maxY = Math.max(getMaxY(), rect.getMaxY());

    return new PlainRectangle(minX, minY, maxX, maxY);
  }

  @Override
  public Rectangle intersection(Rectangle rect) {
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

    return new PlainRectangle(maxMinX, maxMinY, minMaxX, minMaxY);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Rectangle)) {
      return false;
    }
    Rectangle rect = (Rectangle) object;
    if (rect.getMinX() != getMinX()) {
      return false;
    }

    if (rect.getMinY() != getMinY()) {
      return false;
    }

    if (rect.getMaxX() != getMaxX()) {
      return false;
    }

    if (rect.getMaxY() != getMaxY()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Float.floatToIntBits(
        this.minX + 2 * this.minY + 3 * this.maxX + 4 * this.maxY);
  }

  @Override
  public String toString() {
    return "PlainRectangle(" + getMinX() + "," + getMinY() + "," + getMaxX()
        + "," + getMaxY() + ")";
  }
}
