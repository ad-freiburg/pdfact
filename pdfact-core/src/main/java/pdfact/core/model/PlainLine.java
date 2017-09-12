package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * A plain implementation of {@link Line}.
 * 
 * @author Claudius Korzen
 */
public class PlainLine extends Line {
  /**
   * The start point of this line.
   */
  protected Point startPoint;

  /**
   * The end point of this line.
   */
  protected Point endPoint;

  /**
   * The bounding box of this line.
   */
  protected Rectangle rectangle;

  /**
   * Flag to indicate, whether the bounding box needs an update.
   */
  protected boolean isRectangleOutdated;

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
   *        The x-coordinate of the start point.
   * @param startY
   *        The y-coordinate of the start point.
   * @param endX
   *        The x-coordinate of the end point.
   * @param endY
   *        The y-coordinate of the end point.
   */
  @AssistedInject
  public PlainLine(
      @Assisted("startX") float startX,
      @Assisted("startY") float startY,
      @Assisted("endX") float endX,
      @Assisted("endY") float endY) {
    // TODO: Use Guice here.
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
  public PlainLine(
      @Assisted("start") Point startPoint,
      @Assisted("end") Point endPoint) {
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    // TODO: Use Guice here.
    this.rectangle = new PlainRectangle();
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public Point getStartPoint() {
    return this.startPoint;
  }

  @Override
  public void setStartPoint(Point start) {
    this.startPoint = start;
  }

  // ==========================================================================

  @Override
  public float getStartX() {
    return this.startPoint.getX();
  }

  @Override
  public void setStartX(float x) {
    this.startPoint.setX(x);
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public float getStartY() {
    return this.startPoint.getY();
  }

  @Override
  public void setStartY(float y) {
    this.startPoint.setY(y);
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public Point getEndPoint() {
    return this.endPoint;
  }

  @Override
  public void setEndPoint(Point end) {
    this.endPoint = end;
  }

  // ==========================================================================

  @Override
  public float getEndX() {
    return this.endPoint.getX();
  }

  @Override
  public void setEndX(float x) {
    this.endPoint.setX(x);
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public float getEndY() {
    return this.endPoint.getY();
  }

  @Override
  public void setEndY(float y) {
    this.endPoint.setY(y);
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    if (this.isRectangleOutdated) {
      this.rectangle.setMinX(getStartX());
      this.rectangle.setMinY(getStartY());
      this.rectangle.setMaxX(getEndX());
      this.rectangle.setMaxY(getEndY());
      this.isRectangleOutdated = false;
    }
    return this.rectangle;
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    this.rectangle = boundingBox;
    this.isRectangleOutdated = false;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "Line(" + getStartPoint() + "," + getEndPoint() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Line) {
      Line otherLine = (Line) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getStartX(), otherLine.getStartX());
      builder.append(getStartY(), otherLine.getStartY());
      builder.append(getEndX(), otherLine.getEndX());
      builder.append(getEndY(), otherLine.getEndY());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getStartX());
    builder.append(getStartY());
    builder.append(getEndX());
    builder.append(getEndY());
    return builder.hashCode();
  }
}
