package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A simple (geometric) line (not to be confused with a text line).
 * 
 * @author Claudius Korzen.
 */
public class Line {
  /**
   * The start point of this line.
   */
  protected Point startPoint;

  /**
   * The end point of this line.
   */
  protected Point endPoint;

  /**
   * Creates a new line with start point (0,0) and end point (0,0).
   */
  public Line() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a new line with start point (startX, startY) and end point (endY,
   * endY).
   * 
   * @param startX The x-coordinate of the start point.
   * @param startY The y-coordinate of the start point.
   * @param endX   The x-coordinate of the end point.
   * @param endY   The y-coordinate of the end point.
   */
  public Line(float startX, float startY, float endX, float endY) {
    this(new Point(startX, startY), new Point(endX, endY));
  }

  /**
   * Creates a new line which is defined by the given start point and the given
   * end point.
   * 
   * @param startPoint The start point of this line.
   * @param endPoint   The end point of this line.
   */
  public Line(Point startPoint, Point endPoint) {
    this.startPoint = startPoint;
    this.endPoint = endPoint;
  }

  // ==============================================================================================

  /**
   * Returns the start point of this line.
   * 
   * @return The start point of this line.
   */
  public Point getStartPoint() {
    return this.startPoint;
  }

  /**
   * Sets the start point of this line.
   * 
   * @param start The start point of this line.
   */
  public void setStartPoint(Point start) {
    this.startPoint = start;
  }

  // ==============================================================================================

  /**
   * Returns the x-coordinate of the start point.
   * 
   * @return The x-coordinate of the start point.
   */
  public float getStartX() {
    return this.startPoint.getX();
  }

  /**
   * Sets the x-coordinate of the start point.
   * 
   * @param x The x-coordinate of the start point.
   */
  public void setStartX(float x) {
    this.startPoint.setX(x);
  }

  // ==============================================================================================

  /**
   * Returns the y-coordinate of the start point.
   * 
   * @return The y-coordinate of the start point.
   */
  public float getStartY() {
    return this.startPoint.getY();
  }

  /**
   * Sets the y-coordinate of the start point.
   * 
   * @param y The y-coordinate of the start point.
   */
  public void setStartY(float y) {
    this.startPoint.setY(y);
  }

  // ==============================================================================================

  /**
   * Returns the end point of this line.
   * 
   * @return The end point of this line.
   */
  public Point getEndPoint() {
    return this.endPoint;
  }

  /**
   * Sets the end point of this line.
   * 
   * @param end The end point of this line.
   */
  public void setEndPoint(Point end) {
    this.endPoint = end;
  }

  // ==============================================================================================

  /**
   * Returns the x-coordinate of the end point.
   * 
   * @return The x-coordinate of the end point.
   */
  public float getEndX() {
    return this.endPoint.getX();
  }

  /**
   * Sets the x-coordinate of the end point.
   * 
   * @param x The x-coordinate of the end point.
   */
  public void setEndX(float x) {
    this.endPoint.setX(x);
  }

  // ==============================================================================================

  /**
   * Returns the y-coordinate of the end point.
   * 
   * @return The y-coordinate of the end point.
   */
  public float getEndY() {
    return this.endPoint.getY();
  }

  /**
   * Sets the y-coordinate of the end point.
   * 
   * @param y The y-coordinate of the end point.
   */
  public void setEndY(float y) {
    this.endPoint.setY(y);
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Line(" + getStartPoint() + "," + getEndPoint() + ")";
  }

  // ==============================================================================================

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
