package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The features that can be extracted from a pdf file.
 *
 * @author Claudius Korzen
 *
 */
public enum PdfFeature {
  /**
   * The feature "paragraphs".
   */
  paragraphs("paragraph", Color.RED, "\n\n"),

  /**
   * The feature "text lines".
   */
  lines("line", Color.BLUE, " "),

  /**
   * The feature "words".
   */
  words("word", Color.GREEN, " "),

  /**
   * The feature "characters".
   */
  characters("character", Color.GRAY, ""),

  /**
   * The feature "figures".
   */
  figures("figure", Color.ORANGE, ""),

  /**
   * The feature "shape".
   */
  shapes("shape", Color.MAGENTA, "");

  /**
   * The name of this feature.
   */
  String field;

  /**
   * The color to visualize this feature.
   */
  Color color;
  
  /**
   * The delimiter of this feature on serialization.
   */
  String txtDelimiter;
  
  /**
   * The default constructor.
   */
  private PdfFeature(String field, Color color, String delimiter) {
    this.field = field;
    this.color = color;
    this.txtDelimiter = delimiter;
  }

  /**
   * Returns the name of the field of this feature.
   */
  public String getField() {
    return field;
  }

  /**
   * Returns the color of this feature.
   */
  public Color getColor() {
    return color;
  }
  
  /**
   * Returns the delimiter of this feature.
   */
  public String getDelimiter() {
    return txtDelimiter;
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
   * Returns true, if there is a feature with the given name.
   */
  public static List<String> getNames() {
    return new ArrayList<>(features.keySet());
  }

  /**
   * Returns true, if there is a feature with the given name.
   */
  public static boolean isValidFeature(String name) {
    return features.containsKey(name.toLowerCase());
  }

  /**
   * Returns the features that is associated with the given names.
   */
  public static List<PdfFeature> fromNames(String... names) {
    if (names == null || names.length == 0) {
      return null;
    }

    List<PdfFeature> features = new ArrayList<>();
    for (String name : names) {
      if (!isValidFeature(name)) {
        throw new IllegalArgumentException(
            "\"" + name + "\" is not a valid feature");
      }

      PdfFeature feature = fromName(name);
      if (feature != null) {
        features.add(fromName(name));
      }
    }
    return features;
  }

  /**
   * Returns the feature that is associated with the given name.
   */
  public static PdfFeature fromName(String name) {
    if (name == null) {
      return null;
    }

    return features.get(name.toLowerCase());
  }
}
