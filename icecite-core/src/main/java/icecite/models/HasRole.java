package icecite.models;

/**
 * An interface that declares that the implementing object has a semantic role.
 *
 * @author Claudius Korzen
 */
public interface HasRole {
  /**
   * Returns the role of this object.
   * 
   * @return The role of this object.
   */
  PdfRole getRole();

  /**
   * Sets the role of this object.
   * 
   * @param role
   *        The role of this object.
   */
  void setRole(PdfRole role);

  // ==========================================================================

  /**
   * Returns the secondary role of this object. A secondary role is a role, to
   * which the primary role relates. For example, the role "section heading"
   * can relate to the role "abstract", indicating to be the section heading
   * for the abstract.
   * 
   * @return The secondary role of this PDF element.
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
