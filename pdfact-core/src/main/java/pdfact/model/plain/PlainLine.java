package pdfact.model.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.Line;
import pdfact.model.Point;
import pdfact.model.Rectangle;

/**
 * A plain implementation of {@link Line}.
 * 
 * @author Claudius Korzen
 */
public class PlainLine extends Line {
  /**
   * The x value of the lower left of this line.
   */
  protected Point startPoint;

  /**
   * The y value of the lower left of this line.
   */
  protected Point endPoint;

  /**
   * The bounding box of this line.
   */
  protected Rectangle boundingBox;

  /**
   * Flag to indicate, whether the bounding box needs an update.
   */
  protected boolean isBoundingBoxOutdated;

  /**
   * Creates a new line with start point (0,0) and end point (0,0).
   */
  @AssistedInject
  public PlainLine() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a new line with start point (startX, startY) and end point (endY,
   * endY).
   * 
   * @param startX
   *        The x value of the start point.
   * @param startY
   *        The y value of the start point.
   * @param endX
   *        The x value of the end point.
   * @param endY
   *        The y value of the end point.
   */
  @AssistedInject
  public PlainLine(@Assisted("startX") float startX,
      @Assisted("startY") float startY, @Assisted("endX") float endX,
      @Assisted("endY") float endY) {
    this(new PlainPoint(startX, startY), new PlainPoint(endX, endY));
  }

  /**
   * Creates a new line which is defined by the given start point and the given
   * end point.
   * 
   * @param startPoint
   *        The start point of this line.
   * @param endPoint
   *        The end point of this line.
   */
  @AssistedInject
  public PlainLine(@Assisted("start") Point startPoint,
      @Assisted("end") Point endPoint) {
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.boundingBox = new PlainRectangle();
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public Point getStartPoint() {
    return this.startPoint;
  }

  @Override
  public void setStartPoint(Point start) {
    this.startPoint = start;
  }

  @Override
  public float getStartX() {
    return this.startPoint.getX();
  }

  @Override
  public void setStartX(float x) {
    this.startPoint.setX(x);
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public float getStartY() {
    return this.startPoint.getY();
  }

  @Override
  public void setStartY(float y) {
    this.startPoint.setY(y);
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public Point getEndPoint() {
    return this.endPoint;
  }

  @Override
  public void setEndPoint(Point end) {
    this.endPoint = end;
  }

  @Override
  public float getEndX() {
    return this.endPoint.getX();
  }

  @Override
  public void setEndX(float x) {
    this.endPoint.setX(x);
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public float getEndY() {
    return this.endPoint.getY();
  }

  @Override
  public void setEndY(float y) {
    this.endPoint.setY(y);
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public Rectangle getRectangle() {
    if (this.isBoundingBoxOutdated) {
      this.boundingBox.setMinX(getStartX());
      this.boundingBox.setMinY(getStartY());
      this.boundingBox.setMaxX(getEndX());
      this.boundingBox.setMaxY(getEndY());
      this.isBoundingBoxOutdated = false;
    }
    return this.boundingBox;
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    this.boundingBox = boundingBox;
    this.isBoundingBoxOutdated = false;
  }

  @Override
  public String toString() {
    return "PlainLine(" + getStartPoint() + "," + getEndPoint() + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Line) {
      Line otherLine = (Line) other;
      return getStartX() == otherLine.getStartX()
          && getStartY() == otherLine.getStartY()
          && getEndX() == otherLine.getEndX()
          && getEndY() == otherLine.getEndY();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Float.floatToIntBits(
        getStartX() + 2 * getStartY() + 3 * getEndX() + 4 * getEndY());
  }
}
