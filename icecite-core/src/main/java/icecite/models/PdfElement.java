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
  
  // ==========================================================================
  
  /**
   * Returns the type of this PDF element.
   * 
   * @return The type of this PDF element.
   */
  PdfType getType();
  
  // ==========================================================================
  
  /**
   * Returns the role of this PDF element.
   * 
   * @return The role of this PDF element.
   */
  PdfRole getRole();

  /**
   * Sets the role of this PDF element.
   * 
   * @param role
   *        The role of this PDF element.
   */
  void setRole(PdfRole role);
}
