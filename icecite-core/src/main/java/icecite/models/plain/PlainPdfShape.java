package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfColor;
import icecite.models.PdfPage;
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
  // Constructors.

  /**
   * Creates a new PdfShape.
   * 
   * @param page
   *        The page in which this shape is located.
   */
  @AssistedInject
  public PlainPdfShape(@Assisted PdfPage page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public void setPage(PdfPage page) {
    this.page = page;
  }

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
    return "PlainPdfShape(page: " + this.page.getPageNumber() + "rect: "
        + this.boundingBox + ", color: " + this.color + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfShape) {
      PdfShape otherShape = (PdfShape) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherShape.getPage());
      builder.append(getRectangle(), otherShape.getRectangle());
      builder.append(getColor(), otherShape.getColor());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPage());
    builder.append(getRectangle());
    builder.append(getColor());
    return builder.hashCode();
  }
}
