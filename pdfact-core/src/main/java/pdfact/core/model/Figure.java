package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A figure of a document.
 * 
 * @author Claudius Korzen
 */
public class Figure extends Element implements HasPosition {
  /**
   * The position of this figure.
   */
  protected Position position;

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
  public String toString() {
    return "Figure(pos: " + getPosition() + ")";
  }

  // ==============================================================================================

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
