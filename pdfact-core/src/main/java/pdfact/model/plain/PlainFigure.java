package pdfact.model.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.model.Figure;
import pdfact.model.Position;

/**
 * A plain implementation of {@link Figure}.
 * 
 * @author Claudius Korzen
 */
public class PlainFigure extends PlainElement implements Figure {
  /**
   * The position of this figure.
   */
  protected Position position;

  // ==========================================================================

  @Override
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainFigure(pos: " + getPosition() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Figure) {
      Figure otherFigure = (Figure) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherFigure.getPosition());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    return builder.hashCode();
  }
}
