package pdfact.cli.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of all available serialization formats.
 * 
 * @author Claudius Korzen
 */
public enum SerializationFormat {
  /**
   * The serialization format "XML".
   */
  XML("xml"),

  /**
   * The serialization format "TXT".
   */
  TXT("txt"),

  /**
   * The serialization format "JSON".
   */
  JSON("json");

  // ==============================================================================================

  /**
   * The name of this format.
   */
  protected String name;

  /**
   * The serialization formats per names.
   */
  protected static final Map<String, SerializationFormat> FORMATS;

  static {
    FORMATS = new HashMap<>();

    // Fill the map of serialization formats per name.
    for (SerializationFormat format : values()) {
      FORMATS.put(format.getName(), format);
    }
  }

  /**
   * Creates a new serialization format.
   * 
   * @param name The name of the format.
   */
  private SerializationFormat(String name) {
    this.name = name;
  }

  // ==============================================================================================

  /**
   * Returns the name of this serialization format.
   * 
   * @return The name of this serialization format.
   */
  protected String getName() {
    return this.name;
  }

  // ==============================================================================================

  /**
   * Returns the names of all available serialization formats.
   * 
   * @return The names of all available serialization formats as a set.
   */
  public static Set<String> getNames() {
    return FORMATS.keySet();
  }

  /**
   * Checks if the given name is a valid name of an existing serialization format.
   * 
   * @param name The name to check.
   *
   * @return True, if the given name is a valid name of an existing serialization
   *         format.
   */
  public static boolean isValidSerializeFormat(String name) {
    return FORMATS.containsKey(name.toLowerCase());
  }

  /**
   * Returns the serialization formats that are associated with the given names.
   * 
   * @param names The identifiers of the serialization formats to fetch.
   * 
   * @return A set of the fetched serialization formats.
   */
  public static Set<SerializationFormat> fromStrings(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    Set<SerializationFormat> formats = new HashSet<>();
    for (String name : names) {
      SerializationFormat format = fromString(name);
      if (format != null) {
        formats.add(format);
      }
    }
    return formats;
  }

  /**
   * Returns the serialization format that is associated with the given name.
   * 
   * @param name The name of the serialization format to fetch.
   * 
   * @return The serialization format that is associated with the given name.
   */
  public static SerializationFormat fromString(String name) {
    if (!isValidSerializeFormat(name)) {
      throw new IllegalArgumentException(name + " isn't a valid serialization format.");
    }
    return FORMATS.get(name.toLowerCase());
  }
}
