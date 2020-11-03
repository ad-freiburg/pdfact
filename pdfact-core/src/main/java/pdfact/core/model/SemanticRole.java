package pdfact.core.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of all available semantic roles.
 * 
 * @author Claudius Korzen
 */
public enum SemanticRole {
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

  // ==============================================================================================

  /**
   * The name of this semantic role.
   */
  protected String name;

  /**
   * Creates a new semantic role.
   * 
   * @param name The name of the role.
   */
  SemanticRole(String name) {
    this.name = name;
  }

  // ==============================================================================================

  /**
   * Returns the name of this role.
   * 
   * @return The name of this role.
   */
  public String getName() {
    return this.name;
  }

  // ==============================================================================================

  /**
   * The semantic roles by names.
   */
  protected static final Map<String, SemanticRole> ROLES;

  static {
    ROLES = new HashMap<>();

    // Fill the map of roles per name.
    for (SemanticRole role : values()) {
      ROLES.put(role.getName(), role);
    }
  }

  /**
   * Returns the names of all available semantic roles.
   * 
   * @return The names of all available semantic roles as a set.
   */
  public static Set<String> getNames() {
    return ROLES.keySet();
  }

  /**
   * Checks if there exists a semantic role with the given name.
   * 
   * @param name The name to check.
   *
   * @return True, if there exists a semantic role with the given name; false
   *         otherwise.
   */
  public static boolean isValidSemanticRole(String name) {
    return ROLES.containsKey(name.toLowerCase());
  }

  /**
   * Returns all semantic roles that relate to the given names.
   * 
   * @param names The names of the roles to fetch.
   * 
   * @return A set of the roles that relate to the given names.
   */
  public static Set<SemanticRole> fromStrings(String... names) {
    return fromStrings(Arrays.asList(names));
  }

  /**
   * Returns all semantic roles that relate to the given names.
   * 
   * @param names The names of the roles to fetch.
   * 
   * @return A set of the roles that relate to the given names.
   */
  public static Set<SemanticRole> fromStrings(List<String> names) {
    Set<SemanticRole> roles = new HashSet<>();
    if (names != null) {
      for (String name : names) {
        SemanticRole role = fromString(name);
        if (role != null) {
          roles.add(role);
        }
      }
    }
    return roles;
  }

  /**
   * Returns the semantic role that relates to the given name.
   * 
   * @param name The name of the role to fetch.
   * 
   * @return The semantic role that relates to the given name.
   */
  public static SemanticRole fromString(String name) {
    return ROLES.get(name);
  }
}
