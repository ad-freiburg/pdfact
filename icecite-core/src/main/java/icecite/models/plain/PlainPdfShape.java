package icecite.models.plain;

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
   * The page in which this shape is located.
   */
  protected PdfPage page;

  /**
   * The extraction order number.
   */
  protected int extractionOrderNumber;

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
}
