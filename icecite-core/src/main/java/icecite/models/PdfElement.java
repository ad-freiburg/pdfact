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
}
