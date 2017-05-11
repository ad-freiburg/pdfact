package icecite.models;

/**
 * A figure in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFigure extends PdfElement {

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfFigure}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfFigureFactory {
    /**
     * Creates a new PdfFigure.
     * 
     * @return An instance odf PdfFigure.
     */
    PdfFigure create();
  }
}
