package icecite.models;

/**
 * Any (visual) element in a PDF document (character, image, shape, etc.).
 * 
 * @author Claudius Korzen
 */
public interface PdfElement extends HasPosition {
  /**
   * Returns the type of this PDF element.
   * 
   * @return The type of this PDF element.
   */
  PdfFeature getFeature();

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
  
  // ==========================================================================
  
  /**
   * Returns the secondary role of this PDF element. An relation role is a role,
   * to which the primary role relates. For example,the role "section heading"
   * can relate to abstract, references, body, etc.
   * 
   * @return The role of this PDF element.
   */
  PdfRole getSecondaryRole();

  /**
   * Sets the secondary role of this PDF element.
   * 
   * @param role
   *        The secondary role of this PDF element.
   */
  void setSecondaryRole(PdfRole role);
}
