package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import icecite.models.PdfElement;
import icecite.models.PdfFeature;
import icecite.models.PdfPosition;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElement implements PdfElement {
  /**
   * The position of this PDF element.
   */
  protected PdfPosition position;

  // ==========================================================================
  
  @Override
  public PdfFeature getFeature() {
    return null;
  }
  
  // ==========================================================================

  @Override
  public PdfPosition getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(PdfPosition position) {
    this.position = position;
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this.position != null ? this.position.getRectangle() : null;
  }

  @Override
  public void setRectangle(Rectangle rectangle) {
    if (this.position != null) {
      this.position.setRectangle(rectangle);
    }
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfElement(pos: " + getPosition() + ")";
  }

  // ==========================================================================
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfElement) {
      PdfElement otherElement = (PdfElement) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherElement.getPosition());
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
