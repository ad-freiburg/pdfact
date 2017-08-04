package pdfact.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of available semantic roles.
 * 
 * @author Claudius Korzen
 */
public enum PdfRole {
  /**
   * The abstract.
   */
  ABSTRACT("abstract"),
  /**
   * The acknowledgments.
   */
  ACKNOWLEDGMENTS("acknowledgments"),
  /**
   * The affiliation.
   */
  AFFILIATION("affiliation"),
  /**
   * An appendix.
   */
  APPENDIX("appendix"),
  /**
   * The author(s).
   */
  AUTHORS("authors"),
  /**
   * A part of the body text.
   */
  BODY_TEXT("body"),
  /**
   * A caption.
   */
  CAPTION("caption"),
  /**
   * The categories.
   */
  CATEGORIES("categories"),
  /**
   * A figure.
   */
  FIGURE("figure"),
  /**
   * A footnote.
   */
  FOOTNOTE("footnote"),
  /**
   * A formula.
   */
  FORMULA("formula"),
  /**
   * General terms.
   */
  GENERAL_TERMS("general-terms"),
  /**
   * Heading.
   */
  HEADING("heading"),
  /**
   * Itemize.
   */
  ITEMIZE_ITEM("itemize-item"),
  /**
   * The keywords.
   */
  KEYWORDS("keywords"),
  /**
   * A page header.
   */
  PAGE_HEADER("header"),
  /**
   * A page footer.
   */
  PAGE_FOOTER("footer"),
  /**
   * A reference.
   */
  REFERENCE("reference"),
  /**
   * A table.
   */
  TABLE("table"),
  /**
   * The table of contents.
   */
  TABLE_OF_CONTENTS("toc"),
  /**
   * The title.
   */
  TITLE("title");

  // ==========================================================================

  /**
   * The identifier of this role.
   */
  protected String identifier;

  /**
   * Creates a new PDF role.
   * 
   * @param identifier
   *        The identifier of the role.
   */
  PdfRole(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Returns the identifier of this role.
   * 
   * @return The identifier of this role.
   */
  public String getIdentifier() {
    return this.identifier;
  }

  // ==========================================================================

  /**
   * The roles by identifier.
   */
  protected static Map<String, PdfRole> roles;

  static {
    roles = new HashMap<>();

    // Fill the map of roles per name.
    for (PdfRole role : values()) {
      roles.put(role.getIdentifier(), role);
    }
  }

  /**
   * Returns a set of the identifiers of all roles.
   * 
   * @return A set of the identifiers of all roles.
   */
  public static Set<String> getIdentifiers() {
    return roles.keySet();
  }

  /**
   * Checks if the given identifier is a valid identifier of an existing role.
   * 
   * @param identifier
   *        The identifier to check.
   *
   * @return True, if the given identifier is a valid name of an existing role.
   */
  public static boolean isValidRole(String identifier) {
    return roles.containsKey(identifier.toLowerCase());
  }

  /**
   * Returns the roles that are associated with the given identifierss.
   * 
   * @param identifiers
   *        The identifiers of the roles to fetch.
   * @return A set of the fetched roles.
   */
  public static Set<PdfRole> getRoles(String... identifiers) {
    if (identifiers == null || identifiers.length == 0) {
      return null;
    }

    Set<PdfRole> roles = new HashSet<>();
    for (String name : identifiers) {
      PdfRole role = getRole(name);
      if (role != null) {
        roles.add(role);
      }
    }
    return roles;
  }

  /**
   * Returns the role that is associated with the given identifier.
   * 
   * @param identifier
   *        The identifier of the role to fetch.
   * @return The role that is associated with the given identifier.
   */
  public static PdfRole getRole(String identifier) {
    if (!isValidRole(identifier)) {
      throw new IllegalArgumentException(identifier + " isn't a valid role.");
    }
    return roles.get(identifier.toLowerCase());
  }
}
