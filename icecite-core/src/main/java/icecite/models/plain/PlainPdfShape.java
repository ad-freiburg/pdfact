package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import icecite.models.PdfColor;
import icecite.models.PdfShape;
import icecite.models.PdfType;

/**
 * A plain implementation of {@link PdfShape}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfShape extends PlainPdfElement implements PdfShape {
  /**
   * The position of this shape in the extraction order of PDF elements.
   */
  protected int posInExtractionOrder;

  /**
   * The color of this shape.
   */
  protected PdfColor color;

  // ==========================================================================

  @Override
  public PdfColor getColor() {
    return this.color;
  }

  @Override
  public void setColor(PdfColor color) {
    this.color = color;
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return PdfType.SHAPES;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfShape(pos: " + getPosition() + ", color: " + this.color
        + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfShape) {
      PdfShape otherShape = (PdfShape) other;

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
