package icecite.models.plain;

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
}
