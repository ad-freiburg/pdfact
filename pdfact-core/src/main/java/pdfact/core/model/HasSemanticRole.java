package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have a semantic role.
 *
 * @author Claudius Korzen
 */
public interface HasSemanticRole {
  /**
   * Returns the semantic role of the element.
   * 
   * @return The semantic role.
   */
  SemanticRole getSemanticRole();

  /**
   * Sets the semantic role of the element.
   * 
   * @param role
   *        The semantic role.
   */
  void setSemanticRole(SemanticRole role);

  // ==========================================================================

  /**
   * Returns the *secondary* role of this element. A secondary role could be any
   * supplementary role, e.g. a role that relates to the primary role. For
   * example, the primary role "section heading" could have a secondary role
   * that gives the role of the belonging section, e.g. "abstract".
   * 
   * @return The secondary role of the element.
   */
  SemanticRole getSecondaryRole();

  /**
   * Sets the secondary role of the element.
   * 
   * @param role
   *        The secondary role of the element.
   */
  void setSecondaryRole(SemanticRole role);
}
