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
   * The name of this role.
   */
  protected String name;

  /**
   * Creates a new PDF role.
   * 
   * @param name
   *        The name of the role.
   */
  PdfRole(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this role.
   * 
   * @return The name of this role.
   */
  public String getName() {
    return this.name;
  }

  // ==========================================================================

  /**
   * The roles by names.
   */
  protected static Map<String, PdfRole> roles;

  static {
    roles = new HashMap<>();

    // Fill the map of roles per name.
    for (PdfRole role : values()) {
      roles.put(role.getName(), role);
    }
  }

  /**
   * Returns a set of the names of all roles.
   * 
   * @return A set of the names of all roles.
   */
  public static Set<String> getNames() {
    return roles.keySet();
  }

  /**
   * Checks if the given name is a valid name of an existing role.
   * 
   * @param name
   *        The name to check.
   *
   * @return True, if the given name is a valid name of an existing role.
   */
  public static boolean isValidRole(String name) {
    return roles.containsKey(name.toLowerCase());
  }

  /**
   * Returns the roles that are associated with the given names.
   * 
   * @param names
   *        The names of the roles to fetch.
   * @return A set of the fetched roles.
   */
  public static Set<PdfRole> getRoles(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    Set<PdfRole> roles = new HashSet<>();
    for (String name : names) {
      PdfRole role = getRole(name);
      if (role != null) {
        roles.add(role);
      }
    }
    return roles;
  }

  /**
   * Returns the role that is associated with the given name.
   * 
   * @param name
   *        The name of the role to fetch.
   * @return The role that is associated with the given name.
   */
  public static PdfRole getRole(String name) {
    if (!isValidRole(name)) {
      throw new IllegalArgumentException(name + " isn't a valid role.");
    }
    return roles.get(name.toLowerCase());
  }
}
