package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfType;

/**
 * A plain implementation of {@link PdfFigure}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFigure extends PlainPdfElement implements PdfFigure {
  /**
   * The page in which this figure is located.
   */
  protected PdfPage page;

  /**
   * Creates a new PdfFigure.
   * 
   * @param page
   *        The page in which this figure is located.
   */
  @AssistedInject
  public PlainPdfFigure(@Assisted PdfPage page) {
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
  public PdfType getType() {
    return PdfType.FIGURES;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfFigure(page: " + this.page.getPageNumber() + "rect: "
        + this.boundingBox + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfFigure) {
      PdfFigure otherFigure = (PdfFigure) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherFigure.getPage());
      builder.append(getRectangle(), otherFigure.getRectangle());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPage());
    builder.append(getRectangle());
    return builder.hashCode();
  }
}
