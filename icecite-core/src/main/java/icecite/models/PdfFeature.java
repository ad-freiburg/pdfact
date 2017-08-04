package icecite.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The features that can be extracted from a PDF file.
 *
 * @author Claudius Korzen
 */
public enum PdfFeature {
  /**
   * The feature that describes paragraphs.
   */
  PARAGRAPH("paragraphs"),

  /**
   * The feature that describes text lines.
   */
  TEXT_LINE("lines"),

  /**
   * The feature that describes words.
   */
  WORD("words"),

  /**
   * The feature that describes characters.
   */
  CHARACTER("characters"),

  /**
   * The feature that describes figures.
   */
  FIGURE("figures"),

  /**
   * The feature that describes shapes.
   */
  SHAPE("shapes");

  /**
   * The name of this feature.
   */
  String name;

  /**
   * Creates a new feature.
   * 
   * @param name
   *        The name of the feature.
   */
  PdfFeature(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this feature.
   * 
   * @return The name of this feature.
   */
  public String getName() {
    return this.name;
  }

  /**
   * The features by name.
   */
  protected static Map<String, PdfFeature> features;

  static {
    features = new HashMap<>();

    // Fill the map of features per name.
    for (PdfFeature feature : values()) {
      features.put(feature.getName(), feature);
    }
  }

  /**
   * Returns a set of the names of all features.
   * 
   * @return A set of the names of all features.
   */
  public static Set<String> getNames() {
    return features.keySet();
  }

  /**
   * Checks if the given name is a name of a existing feature.
   * 
   * @param name
   *        The name to check.
   * 
   * @return True, if the given name is a name of an existing feature.
   */
  public static boolean isValidFeature(String name) {
    return features.containsKey(name.toLowerCase());
  }

  /**
   * Returns the features that are associated with the given names.
   * 
   * @param names
   *        The names of the features to fetch.
   *
   * @return A set of the fetched features.
   */
  public static Set<PdfFeature> getFeatures(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    Set<PdfFeature> features = new HashSet<>();
    for (String name : names) {
      PdfFeature feature = getFeature(name);
      if (feature != null) {
        features.add(feature);
      }
    }
    return features;
  }

  /**
   * Returns the feature that is associated with the given name.
   * 
   * @param name
   *        The name of the feature to fetch.
   *
   * @return The feature that is associated with the given name.
   */
  public static PdfFeature getFeature(String name) {
    if (!isValidFeature(name)) {
      throw new IllegalArgumentException(name + " isn't a valid feature.");
    }
    return features.get(name.toLowerCase());
  }
}