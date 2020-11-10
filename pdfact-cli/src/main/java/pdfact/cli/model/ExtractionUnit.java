package pdfact.cli.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of all available units that can be extracted from a document.
 *
 * @author Claudius Korzen
 */
public enum ExtractionUnit {
  /**
   * The unit "paragraphs".
   */
  PARAGRAPH("paragraphs"),

  /**
   * The unit "text blocks".
   */
  TEXT_BLOCK("blocks"),

  /**
   * The unit "text lines".
   */
  TEXT_LINE("lines"),

  /**
   * The unit "words".
   */
  WORD("words"),

  /**
   * The unit "characters".
   */
  CHARACTER("characters"),

  /**
   * The unit "text areas".
   */
  TEXT_AREA("areas"),

  /**
   * The unit "figures".
   */
  FIGURE("figures"),

  /**
   * The unit "shapes".
   */
  SHAPE("shapes"),

  /**
   * The unit "page".
   */
  PAGE("pages");

  // ==============================================================================================

  /**
   * The plural name of this unit.
   */
  protected String pluralName;

  /**
   * The units per plural names.
   */
  protected static Map<String, ExtractionUnit> index;

  static {
    index = new HashMap<>();

    // Fill the map of types per group name.
    for (ExtractionUnit type : values()) {
      index.put(type.getPluralName(), type);
    }
  }
  
  /**
   * Creates a new unit.
   * 
   * @param pluralName
   *        The plural name of this unit.
   */
  private ExtractionUnit(String pluralName) {
    this.pluralName = pluralName;
  }

  // ==============================================================================================
  
  /**
   * Returns the plural name of this unit.
   * 
   * @return The plural name of this unit.
   */
  public String getPluralName() {
    return this.pluralName;
  }

  // ==============================================================================================
  
  /**
   * Returns a set of the plural names of all units.
   * 
   * @return A set of the plural names of all units.
   */
  public static Set<String> getPluralNames() {
    return index.keySet();
  }

  /**
   * Checks if the given name is a plural name of an existing unit.
   * 
   * @param name
   *        The name to check.
   * 
   * @return True, if the given name is a plural name of an existing type.
   */
  public static boolean isValidType(String name) {
    return index.containsKey(name.toLowerCase());
  }

  /**
   * Returns the types that are associated with the given plural names.
   * 
   * @param names
   *        The names of the  units to fetch.
   *
   * @return A set of the fetched units.
   */
  public static Set<ExtractionUnit> fromStrings(String... names) {
    return fromStrings(Arrays.asList(names));
  }

  /**
   * Returns the units that are associated with the given names.
   * 
   * @param names
   *        The names of the units to fetch.
   *
   * @return A set of the fetched units.
   */
  public static Set<ExtractionUnit> fromStrings(List<String> names) {
    if (names == null || names.isEmpty()) {
      return null;
    }

    Set<ExtractionUnit> types = new HashSet<>();
    for (String name : names) {
      ExtractionUnit type = fromString(name);
      if (type != null) {
        types.add(type);
      }
    }
    return types;
  }

  /**
   * Returns the unit that is associated with the given name.
   * 
   * @param name
   *        The plural name of the type to fetch.
   *
   * @return The type that is associated with the given plural name.
   */
  public static ExtractionUnit fromString(String name) {
    if (!isValidType(name)) {
      throw new IllegalArgumentException(name + " isn't a valid type.");
    }
    return index.get(name.toLowerCase());
  }
}