package icecite.models;

/**
 * A shape in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfShape extends PdfElement, HasPage, HasColor {
  /**
   * The factory to create instances of {@link PdfShape}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfShapeFactory {
    /**
     * Creates a new PdfShape.
     * 
     * @param page
     *        The page in which the shape is located.
     * 
     * @return An instance of {@link PdfShape}.
     */
    PdfShape create(PdfPage page);
  }
}
