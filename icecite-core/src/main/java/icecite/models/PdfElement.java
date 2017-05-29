package icecite.models;

import icecite.utils.geometric.HasRectangle;

/**
 * Any (visual) element in a PDF document (character, image, shape, etc.).
 * 
 * @author Claudius Korzen
 */
public interface PdfElement extends HasRectangle, HasPage {
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
