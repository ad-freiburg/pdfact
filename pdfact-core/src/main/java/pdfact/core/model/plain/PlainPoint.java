package pdfact.core.model.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Point;
import pdfact.core.model.Rectangle;

/**
 * A plain implementation of {@link Point}.
 * 
 * @author Claudius Korzen
 */
public class PlainPoint extends Point {
  /**
   * The x value.
   */
  protected float x;

  /**
   * The y value.
   */
  protected float y;

  /**
   * The bounding box.
   */
  protected Rectangle boundingBox;

  /**
   * Flag to indicate, that the bounding box needs an update.
   */
  protected boolean isBoundingBoxOutdated;

  /**
   * Creates a new point (0, 0).
   */
  @AssistedInject
  public PlainPoint() {
    this(0, 0);
  }

  /**
   * Creates a new point (x, y).
   * 
   * @param x
   *        The x value.
   * @param y
   *        The y value.
   */
  @AssistedInject
  public PlainPoint(@Assisted("x") float x, @Assisted("y") float y) {
    setX(x);
    setY(y);
    this.boundingBox = new PlainRectangle(x, y, x, y);
  }

  /**
   * Creates a new point (x,y).
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

  @Override
  public float getX() {
    return this.x;
  }

  @Override
  public void setX(float x) {
    this.x = x;
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public float getY() {
    return this.y;
  }

  @Override
  public void setY(float y) {
    this.y = y;
    this.isBoundingBoxOutdated = true;
  }

  @Override
  public Rectangle getRectangle() {
    if (this.isBoundingBoxOutdated) {
      this.boundingBox.setMinX(getX());
      this.boundingBox.setMinY(getY());
      this.boundingBox.setMaxX(getX());
      this.boundingBox.setMaxY(getY());
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
  public boolean equals(Object other) {
    if (other instanceof Point) {
      Point otherPoint = (Point) other;
      return getX() == otherPoint.getX() && getY() == otherPoint.getY();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Float.floatToIntBits(getX() + 2 * getY());
  }

  @Override
  public String toString() {
    return "PlainPoint(" + getX() + "," + getY() + ")";
  }
}
