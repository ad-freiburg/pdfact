package pdfact.models;

// TODO: Add some more properties to a shape.

/**
 * A shape in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfShape extends PdfSinglePositionElement, HasColor {
  /**
   * The factory to create instances of {@link PdfShape}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfShapeFactory {
    /**
     * Creates a new instance of PdfShape.
     * 
     * @return A new instance of {@link PdfShape}.
     */
    PdfShape create();
  }
}
