package icecite.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
  PARAGRAPH("paragraph"),

  /**
   * The feature that describes text lines.
   */
  TEXT_LINE("line"),

  /**
   * The feature that describes words.
   */
  WORD("word"),

  /**
   * The feature that describes characters.
   */
  CHARACTER("character"),

  /**
   * The feature that describes figures.
   */
  FIGURE("figure"),

  /**
   * The feature that describes shapes.
   */
  SHAPE("shape");

  /**
   * The name of this feature.
   */
  String name;

  /**
   * The default constructor.
   * 
   * @param name
   *        The name of the feature.
   */
  private PdfFeature(String name) {
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

    for (PdfFeature feature : values()) {
      features.put(feature.name(), feature);
    }
  }

  /**
   * Returns a list with the names of all features.
   * 
   * @return A list with the names of all features.
   */
  public static List<String> getNames() {
    return new ArrayList<>(features.keySet());
  }

  /**
   * Returns true, if the given name is a valid name of a existing feature.
   * 
   * @param name
   *        The name to check.
   * @return True, if the given name is a valid name of a existing feature.
   */
  public static boolean isValidFeature(String name) {
    return features.containsKey(name.toLowerCase());
  }

  /**
   * Returns the features that is associated with the given names.
   * 
   * @param names
   *        The names of features to fetch.
   * @return The list of fetched features.
   */
  public static List<PdfFeature> fromNames(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    List<PdfFeature> features = new ArrayList<>();
    for (String name : names) {
      PdfFeature feature = fromName(name);
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
   * @return The feature that is associated with the given name.
   */
  public static PdfFeature fromName(String name) {
    if (!isValidFeature(name)) {
      throw new IllegalArgumentException(
          "\"" + name + "\" is not a valid feature");
    }

    return features.get(name.toLowerCase());
  }

  /**
   * Returns all features in a set.
   * 
   * @return all features in a set.
   */
  public static Set<PdfFeature> valuesAsSet() {
    return new HashSet<PdfFeature>(Arrays.asList(values()));
  }
}