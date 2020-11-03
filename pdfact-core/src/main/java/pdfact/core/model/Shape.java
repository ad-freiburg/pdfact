package pdfact.core.model;

// TODO: Add some more properties to a shape.

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A shape in a document.
 * 
 * @author Claudius Korzen
 */
public class Shape extends Element implements HasPosition, HasColor {
  /**
   * The position of this shape.
   */
  protected Position position;

  /**
   * The color of this shape.
   */
  protected Color color;

  // ==============================================================================================

  @Override
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  // ==============================================================================================

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Shape(pos: " + getPosition() + ", color: " + getColor() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Shape) {
      Shape otherShape = (Shape) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherShape.getPosition());
      builder.append(getColor(), otherShape.getColor());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    builder.append(getColor());
    return builder.hashCode();
  }
}
