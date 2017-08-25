package pdfact.model;

/**
 * Any resource in a PDF document (font, color, etc.).
 * 
 * @author Claudius Korzen
 */
public interface Resource {
  /**
   * Returns the id of this resource (used for referencing on serialization).
   * 
   * @return The id.
   */
  String getId();

  /**
   * Sets the id of this resource (used for referencing on serialization).
   * 
   * @param id
   *        The id.
   */
  void setId(String id);
}
