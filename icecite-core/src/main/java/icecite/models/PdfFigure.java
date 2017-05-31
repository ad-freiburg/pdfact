package icecite.models;

/**
 * A figure in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFigure extends PdfElement {
  /**
   * The factory to create instances of {@link PdfFigure}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfFigureFactory {
    /**
     * Creates a new PdfFigure.
     * 
     * @param page
     *        The page in which the figure is located.
     * 
     * @return An instance of {@link PdfFigure}.
     */
    PdfFigure create(PdfPage page);
  }
}
