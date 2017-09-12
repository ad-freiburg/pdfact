package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * A plain implementation of {@link Point}.
 * 
 * @author Claudius Korzen
 */
public class PlainPoint extends Point {
  /**
   * The x-coordinate of this point.
   */
  protected float x;

  /**
   * The y-coordinate of this point.
   */
  protected float y;

  /**
   * The bounding box.
   */
  protected Rectangle rectangle;

  /**
   * Flag to indicate whether the bounding box needs an update.
   */
  protected boolean isRectangleOutdated;

  /**
   * Creates a new point with coordinates (0, 0).
   */
  @AssistedInject
  public PlainPoint() {
    this(0, 0);
  }

  /**
   * Creates a new point with coordinates (x, y).
   * 
   * @param x
   *        The x-coordinate.
   * @param y
   *        The y-coordinate.
   */
  @AssistedInject
  public PlainPoint(@Assisted("x") float x, @Assisted("y") float y) {
    setX(x);
    setY(y);
    // TODO: Use Guice here.
    this.rectangle = new PlainRectangle(x, y, x, y);
  }

  /**
   * Creates a new point with coordinates (x, y).
   * 
   * @param x
   *        The x value.
   * @param y
   *        The y value.
   */
  @AssistedInject
  public PlainPoint(@Assisted("x") double x, @Assisted("y") double y) {
    this((float) x, (float) y);
  }

  // ==========================================================================

  @Override
  public float getX() {
    return this.x;
  }

  @Override
  public void setX(float x) {
    this.x = x;
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public float getY() {
    return this.y;
  }

  @Override
  public void setY(float y) {
    this.y = y;
    this.isRectangleOutdated = true;
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    if (this.isRectangleOutdated) {
      this.rectangle.setMinX(getX());
      this.rectangle.setMinY(getY());
      this.rectangle.setMaxX(getX());
      this.rectangle.setMaxY(getY());
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
    return "Point(" + getX() + "," + getY() + ")";
  }

  // ==========================================================================

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
