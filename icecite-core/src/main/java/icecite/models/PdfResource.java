package icecite.models;

/**
 * Any resource in a PDF document (font, color, etc.).
 * 
 * @author Claudius Korzen
 */
public interface PdfResource {
  /**
   * Returns the id of this resource (needed for referencing on serialization).
   * 
   * @return The id of this resource.
   */
  String getId();

  /**
   * Sets the id of this resource (needed for referencing on serialization).
   * 
   * @param id
   *        The id of this resource.
   */
  void setId(String id);
}
