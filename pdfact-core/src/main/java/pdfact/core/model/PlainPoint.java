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
public class PlainPoint implements Point {
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
  }

  // ==========================================================================

  @Override
  public float getY() {
    return this.y;
  }

  @Override
  public void setY(float y) {
    this.y = y;
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
