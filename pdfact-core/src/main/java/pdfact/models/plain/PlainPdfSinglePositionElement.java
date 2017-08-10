package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.models.PdfPosition;
import pdfact.models.PdfSinglePositionElement;
import pdfact.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfSinglePositionElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfSinglePositionElement extends PlainPdfElement
    implements PdfSinglePositionElement {
  /**
   * The position of this PDF element.
   */
  protected PdfPosition position;

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
    return "PlainPdfSinglePositionElement(pos: " + getPosition() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfSinglePositionElement) {
      PdfSinglePositionElement otherElement = (PdfSinglePositionElement) other;

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
