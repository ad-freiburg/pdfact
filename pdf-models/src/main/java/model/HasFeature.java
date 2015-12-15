package model;

/**
 * Interface to indicate that an object represents an feature. 
 *
 * @author Claudius Korzen
 *
 */
public interface HasFeature extends Serializable {
  /**
   * Returns the represented feature.
   */
  public PdfFeature getFeature();
}
