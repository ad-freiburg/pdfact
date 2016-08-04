package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The roles of a pdf document.
 * 
 * @author Claudius Korzen
 */
public enum PdfRole {
  ABSTRACT("abstract"),
  ABSTRACT_HEADING("abstract-heading"),
  BODY_TEXT("body-text"),
  FIGURE("figure"),
  FIGURE_CAPTION("figure-caption"),
  FORMULA("formula"),
  KEYWORDS("keywords"),
  PAGE_HEADER("page-header"),
  PAGE_FOOTER("page-footer"),
  FOOTNOTE("footnote"),
  REFERENCE("reference"),
  REFERENCES_HEADING("reference-heading"),
  SECTION_HEADING("section-heading"),
  TABLE("table"),
  TABLE_CAPTION("table-caption"),
  TITLE("title"),
  ITEMIZE_ITEM("itemize-item"),
  // Role for all elements that belongs to the header (but we don't know the 
  // actual role).
  HEADER_OTHER("header-other"),
  UNKNOWN("unknown");

  public String name;
  
  private PdfRole(String name) {
    this.name = name;
  }
  
  /**
   * The roles by name.
   */
  protected static Map<String, PdfRole> roles;

  static {
    roles = new HashMap<>();

    for (PdfRole role : values()) {
      roles.put(role.name, role);
    }
  }

  /**
   * Returns the roles that are associated with the given names.
   */
  public static List<PdfRole> fromNames(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    List<PdfRole> roles = new ArrayList<>();
    for (String name : names) {
      PdfRole role = fromName(name);
      if (role != null) {
        roles.add(role);
      }
    }
    return roles;
  }
  
  /**
   * Returns the role that is associated with the given name.
   */
  public static PdfRole fromName(String name) {
    if (name == null) {
      return null;
    }

    if (!isValidRole(name)) {
      throw new IllegalArgumentException(
          "\"" + name + "\" is not a valid role");
    }

    return roles.get(name.toLowerCase());
  }

  public static List<String> getNames() {
    return new ArrayList<>(roles.keySet());
  }

  /**
   * Returns true, if there is a role with the given name.
   */
  public static boolean isValidRole(String name) {
    return roles.containsKey(name.toLowerCase());
  }

  public static List<PdfRole> valuesAsList() {
    return Arrays.asList(values());
  }

  public static Set<PdfRole> valuesAsSet() {
    return new HashSet<PdfRole>(valuesAsList());
  }
}
