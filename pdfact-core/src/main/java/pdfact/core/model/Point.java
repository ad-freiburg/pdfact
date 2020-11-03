package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A single, two-dimensional point.
 * 
 * @author Claudius Korzen.
 */
public class Point {
  /**
   * The x-coordinate of this point.
   */
  protected float x;

  /**
   * The y-coordinate of this point.
   */
  protected float y;

  /**
   * Creates a new point with coordinates (0, 0).
   */
  public Point() {
    this(0, 0);
  }

  /**
   * Creates a new point with coordinates (x, y).
   * 
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public Point(float x, float y) {
    setX(x);
    setY(y);
  }

  /**
   * Creates a new point with coordinates (x, y).
   * 
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public Point(double x, double y) {
    this((float) x, (float) y);
  }

  // ==============================================================================================

  /**
   * Returns the x-coordinate of this point.
   * 
   * @return The x-coordinate of this point.
   */
  public float getX() {
    return this.x;
  }

  /**
   * Sets the x-coordinate of this point.
   * 
   * @param x The x-coordinate of this point.
   */
  public void setX(float x) {
    this.x = x;
  }

  // ==============================================================================================

  /**
   * Returns the y-coordinate of this point.
   * 
   * @return The y-coordinate of this point.
   */
  public float getY() {
    return this.y;
  }

  /**
   * Sets the y-coordinate of this point.
   * 
   * @param y The y-coordinate of this point.
   */
  public void setY(float y) {
    this.y = y;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Point(" + getX() + "," + getY() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Point) {
      Point otherPoint = (Point) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getX(), otherPoint.getX());
      builder.append(getY(), otherPoint.getY());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getX());
    builder.append(getY());
    return builder.hashCode();
  }
}
