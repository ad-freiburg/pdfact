package icecite.models;

/**
 * Any (visual) element in a PDF document (character, image, shape, etc.).
 * 
 * @author Claudius Korzen
 */
public interface PdfElement extends HasBoundingBox {
  /**
   * Returns the page in which this PDF element is included.
   * 
   * @return The page in which this PDF element is included.
   */
  PdfPage getPage();

  /**
   * Sets the page in which this PDF element is included.
   * 
   * @param page
   *        The page of this PDF element.
   */
  void setPage(PdfPage page);
}
