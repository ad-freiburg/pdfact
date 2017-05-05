package icecite.models;

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
