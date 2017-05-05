package icecite.models;

/**
 * The factory to create instances of {@link PdfShape}.
 * 
 * @author Claudius Korzen
 */
public interface PdfShapeFactory {
  /**
   * Creates a new PdfShape.
   * 
   * @return An instance of {@link PdfShape}.
   */
  PdfShape create();
}
