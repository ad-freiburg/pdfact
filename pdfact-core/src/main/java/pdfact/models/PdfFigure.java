package pdfact.models;

// TODO: Add some more properties to a figure.

/**
 * A figure in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFigure extends PdfSinglePositionElement {
  /**
   * The factory to create instances of {@link PdfFigure}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfFigureFactory {
    /**
     * Creates a new instance of PdfFigure.
     * 
     * @return A new instance of {@link PdfFigure}.
     */
    PdfFigure create();
  }
}
