package pdfact.core.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.PdfDocument.PdfDocumentFactory;
import pdfact.core.model.Point.PointFactory;

/**
 * A plain implementation of {@link Rectangle}.
 * 
 * @author Claudius Korzen
 */
public class PlainRectangle implements Rectangle {
  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

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
   * Creates a new rectangle with the lower left point (0, 0) and the upper
   * right point (0, 0).
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   */
  @AssistedInject
  public PlainRectangle(RectangleFactory rectangleFactory,
      PointFactory pointFactory) {
    this(rectangleFactory, pointFactory, 0, 0, 0, 0);
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(RectangleFactory rectangleFactory,
      PointFactory pointFactory, @Assisted Rectangle rect) {
    this(rectangleFactory, pointFactory, rect.getMinX(), rect.getMinY(),
        rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param rect
   *        The rectangle to copy.
   */
  @AssistedInject
  public PlainRectangle(RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted java.awt.Rectangle rect) {
    this(rectangleFactory, pointFactory, rect.getMinX(), rect.getMinY(),
        rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle that is spanned by the given lower left point and
   * the given upper right point.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param ll
   *        The lower left point.
   * @param ur
   *        The upper right point.
   */
  @AssistedInject
  public PlainRectangle(
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted("lowerLeft") Point ll,
      @Assisted("upperRight") Point ur) {
    this(rectangleFactory, pointFactory, ll.getX(), ll.getY(), ur.getX(),
        ur.getY());
  }

  /**
   * Creates a new rectangle with the given coordinates.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param minX
   *        The x-coordinate of the lower left point of this rectangle.
   * @param minY
   *        The y-coordinate of the lower left point of this rectangle.
   * @param maxX
   *        The x-coordinate of the upper right point of this rectangle.
   * @param maxY
   *        The y-coordinate of the upper right point of this rectangle.
   */
  @AssistedInject
  public PlainRectangle(
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted("minX") double minX,
      @Assisted("minY") double minY,
      @Assisted("maxX") double maxX,
      @Assisted("maxY") double maxY) {
    this(rectangleFactory, pointFactory, (float) minX, (float) minY,
        (float) maxX, (float) maxY);
  }

  /**
   * Creates a new rectangle with the lower left point (minX, minY) and the
   * upper right (maxX, maxY).
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param minX
   *        The x-coordinate of the lower left point of this rectangle.
   * @param minY
   *        The y-coordinate of the lower left point of this rectangle.
   * @param maxX
   *        The x-coordinate of the upper right point of this rectangle.
   * @param maxY
   *        The y-coordinate of the upper right point of this rectangle.
   */
  @AssistedInject
  public PlainRectangle(
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted("minX") float minX,
      @Assisted("minY") float minY,
      @Assisted("maxX") float maxX,
      @Assisted("maxY") float maxY) {
    this.rectangleFactory = rectangleFactory;
    this.pointFactory = pointFactory;
    setMinX(minX);
    setMinY(minY);
    setMaxX(maxX);
    setMaxY(maxY);
  }

  /**
   * Creates a new rectangle from the union of the given rectangles.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param rectangles
   *        The rectangles to process.
   */
  @AssistedInject
  public PlainRectangle(
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted Rectangle... rectangles) {
    this.rectangleFactory = rectangleFactory;
    this.pointFactory = pointFactory;
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
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param elements
   *        The elements to process.
   */
  @AssistedInject
  public PlainRectangle(
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted("hasPosition") Iterable<? extends HasPosition> elements) {
    this.rectangleFactory = rectangleFactory;
    this.pointFactory = pointFactory;
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
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param elements
   *        The elements to process.
   */
  @AssistedInject
  // TODO: PdfDocumentFactory is only injected to get an constructor with
  // another erasure than the previous constructor. Find out how to deal with
  // constructors with same erasure correctly.
  public PlainRectangle(
      PdfDocumentFactory xxx,
      RectangleFactory rectangleFactory,
      PointFactory pointFactory,
      @Assisted("hasPositions") Iterable<? extends HasPositions> elements) {
    this.rectangleFactory = rectangleFactory;
    this.pointFactory = pointFactory;
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
    return this.pointFactory.create(this.minX, this.minY);
  }

  @Override
  public Point getLowerRight() {
    return this.pointFactory.create(this.maxX, this.minY);
  }
  
  @Override
  public Point getUpperLeft() {
    return this.pointFactory.create(this.minX, this.maxY);
  }

  @Override
  public Point getUpperRight() {
    return this.pointFactory.create(this.maxX, this.maxY);
  }

  // ==========================================================================

  @Override
  public Point getMidpoint() {
    return this.pointFactory.create(getXMidpoint(), getYMidpoint());
  }

  @Override
  public float getXMidpoint() {
    return getMinX() + (getWidth() / 2f);
  }

  @Override
  public float getYMidpoint() {
    return getMinY() + (getHeight() / 2f);
  }

  // ==========================================================================

  @Override
  public float getWidth() {
    return getMaxX() - getMinX();
  }

  @Override
  public float getHeight() {
    return getMaxY() - getMinY();
  }

  // ==========================================================================

  @Override
  public float getArea() {
    return getWidth() * getHeight();
  }

  // ==========================================================================

  @Override
  public void extend(Rectangle rect) {
    if (rect == null) {
      return;
    }
    setMinX(Math.min(getMinX(), rect.getMinX()));
    setMaxX(Math.max(getMaxX(), rect.getMaxX()));
    setMinY(Math.min(getMinY(), rect.getMinY()));
    setMaxY(Math.max(getMaxY(), rect.getMaxY()));
  }

  @Override
  public Rectangle union(Rectangle rect) {
    if (rect == null) {
      return null;
    }
    
    float minX = Math.min(getMinX(), rect.getMinX());
    float maxX = Math.max(getMaxX(), rect.getMaxX());
    float minY = Math.min(getMinY(), rect.getMinY());
    float maxY = Math.max(getMaxY(), rect.getMaxY());

    return this.rectangleFactory.create(minX, minY, maxX, maxY);
  }

  @Override
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

    return this.rectangleFactory.create(maxMinX, maxMinY, minMaxX, minMaxY);
  }

  // ==========================================================================

  @Override
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

  // ==========================================================================

  @Override
  public boolean overlaps(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return overlapsHorizontally(rect) && overlapsVertically(rect);
  }

  @Override
  public float getOverlapRatio(Rectangle rect) {
    if (getArea() <= 0) {
      return 0;
    }
    float horizontalOverlapLength = getHorizontalOverlapLength(rect);
    float verticalOverlapLength = getVerticalOverlapLength(rect);
    return (horizontalOverlapLength * verticalOverlapLength) / getArea();
  }

  // ==========================================================================

  @Override
  public boolean overlapsHorizontally(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMaxX() >= rect.getMinX() && getMinX() <= rect.getMaxX();
  }

  @Override
  public float getHorizontalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxX = Math.min(getMaxX(), rect.getMaxX());
      float maxMinX = Math.max(getMinX(), rect.getMinX());
      return Math.max(0, minMaxX - maxMinX);
    }
    return 0;
  }

  // ==========================================================================

  @Override
  public boolean overlapsVertically(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMinY() <= rect.getMaxY() && getMaxY() >= rect.getMinY();
  }

  @Override
  public float getVerticalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxY = Math.min(getMaxY(), rect.getMaxY());
      float maxMinY = Math.max(getMinY(), rect.getMinY());
      return Math.max(0, minMaxY - maxMinY);
    }
    return 0;
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
