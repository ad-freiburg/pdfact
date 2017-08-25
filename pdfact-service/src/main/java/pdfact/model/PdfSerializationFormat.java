package pdfact.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of serialization formats.
 * 
 * @author Claudius Korzen
 */
public enum PdfSerializationFormat {
  /**
   * The serialization format xml.
   */
  XML("xml"),
  /**
   * The serialization format txt.
   */
  TXT("txt"),
  /**
   * The serialization format json.
   */
  JSON("json");

  /**
   * The name of this format.
   */
  protected String name;

  /**
   * Creates a new serialization format.
   * 
   * @param name
   *        The name of the format.
   */
  PdfSerializationFormat(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this serialization format.
   * 
   * @return The name of this serialization format.
   */
  protected String getName() {
    return this.name;
  }

  // ==========================================================================

  /**
   * The formats.
   */
  protected static final Map<String, PdfSerializationFormat> FORMATS;

  static {
    FORMATS = new HashMap<>();

    // Fill the map of roles per name.
    for (PdfSerializationFormat format : values()) {
      FORMATS.put(format.getName(), format);
    }
  }

  /**
   * Returns a set of the names of all serialization formats.
   * 
   * @return A set of the names of all serialization roles.
   */
  public static Set<String> getNames() {
    return FORMATS.keySet();
  }

  /**
   * Checks if the given name is a valid name of an existing serialization
   * format.
   * 
   * @param name
   *        The name to check.
   *
   * @return True, if the given name is a valid name of an existing
   *         serialization format.
   */
  public static boolean isValidSerializationFormat(String name) {
    return FORMATS.containsKey(name.toLowerCase());
  }

  /**
   * Returns the serialization formats that are associated with the given names.
   * 
   * @param names
   *        The identifiers of the serialization formats to fetch.
   * @return A set of the fetched serialization formats.
   */
  public static Set<PdfSerializationFormat> fromStrings(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    Set<PdfSerializationFormat> formats = new HashSet<>();
    for (String name : names) {
      PdfSerializationFormat format = fromString(name);
      if (format != null) {
        formats.add(format);
      }
    }
    return formats;
  }

  /**
   * Returns the serialization format that is associated with the given name.
   * 
   * @param name
   *        The name of the serialization format to fetch.
   * @return The serialization format that is associated with the given name.
   */
  public static PdfSerializationFormat fromString(String name) {
    if (!isValidSerializationFormat(name)) {
      throw new IllegalArgumentException(
          name + " isn't a valid serialization format.");
    }
    return FORMATS.get(name.toLowerCase());
  }
}
