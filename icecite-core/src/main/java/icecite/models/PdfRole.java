package icecite.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of semantic roles for paragraphs of PDF documents.
 * 
 * @author Claudius Korzen
 */
public enum PdfRole {
  /**
   * The abstract.
   */
  ABSTRACT("abstract"),
  /**
   * The heading of the abstract.
   */
  ABSTRACT_HEADING("abstract heading"),
  /**
   * The acknowledgments.
   */
  ACKNOWLEDGMENTS("acknowledgments"),
  /**
   * The heading of the acknowledgments.
   */
  ACKNOWLEDGMENTS_HEADING("acknowledgments heading"),
  /**
   * The affiliation.
   */
  AFFILIATION("affiliation"),  
  /**
   * An appendix.
   */
  APPENDIX("appendix"),
  /**
   * A heading of an appendix.
   */
  APPENDIX_HEADING("appendix heading"),
  /**
   * The authors.
   */
  AUTHORS("authors"),
  /**
   * A part of the body text.
   */
  BODY_TEXT("body text"),
  /**
   * A heading of a body text section.
   */
  BODY_TEXT_HEADING("body text heading"),
  /**
   * A caption.
   */
  CAPTION("caption"),
  /**
   * A caption of a figure.
   */
  FIGURE_CAPTION("figure caption"),
  /**
   * A footnote.
   */
  FOOTNOTE("footnote"),
  /**
   * A formula.
   */
  FORMULA("formula"),
  /**
   * An item of a list.
   */
  LIST_ITEM("list item"),
  /**
   * A page header.
   */
  PAGE_HEADER("page header"),
  /**
   * A page footer.
   */
  PAGE_FOOTER("page footer"),
  /**
   * The title.
   */
  TITLE("title"),
  /**
   * A reference.
   */
  REFERENCE("reference"),
  /**
   * The heading of the references section.
   */
  REFERENCES_HEADING("references heading"),
  /**
   * A table.
   */
  TABLE("table"),
  /**
   * A caption of a table.
   */
  TABLE_CAPTION("table caption");
  
  
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
   * The roles by name.
   */
  protected static Map<String, PdfRole> roles;

  static {
    roles = new HashMap<>();

    for (PdfRole role : values()) {
      roles.put(role.getName(), role);
    }
  }
  
  /**
   * Returns a list with the names of all roles.
   * 
   * @return A list with the names of all roles.
   */
  public static List<String> getNames() {
    return new ArrayList<>(roles.keySet());
  }
  
  /**
   * Returns true, if the given name is a valid name of an existing role.
   * 
   * @param name
   *        The name to check.
   * @return True, if the given name is a valid name of an existing role.
   */
  public static boolean isValidRole(String name) {
    return roles.containsKey(name.toLowerCase());
  }
  
  /**
   * Returns the roles that are associated with the given names.
   * 
   * @param names
   *        The names of roles to fetch.
   * @return A set of fetched roles.
   */
  public static Set<PdfRole> fromNames(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    Set<PdfRole> roles = new HashSet<>();
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
   * 
   * @param name
   *        The name of the role to fetch.
   * @return The role that is associated with the given name.
   */
  public static PdfRole fromName(String name) {
    if (!isValidRole(name)) {
      throw new IllegalArgumentException(
          "\"" + name + "\" is not a valid feature");
    }

    return roles.get(name.toLowerCase());
  }
  
  /**
   * Returns all roles in a set.
   * 
   * @return all roles in a set.
   */
  public static Set<PdfRole> valuesAsSet() {
    return new HashSet<PdfRole>(Arrays.asList(values()));
  }
}
