package pdfact.serialize;

import java.util.Set;

import pdfact.models.PdfDocument;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextUnit;

/**
 * A serializer to serialize a PDF document in a specific format.
 *
 * @author Claudius Korzen
 */
public interface PdfSerializer {
  /**
   * Serializes the given PDF document.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @return The serialized PDF document.
   */
  byte[] serialize(PdfDocument pdf);

  // ==========================================================================

  /**
   * Returns the text units to include on serialization.
   * 
   * @return The text units to include on serialization.
   */
  Set<PdfTextUnit> getTextUnits();

  /**
   * Sets the text units to include on serialization.
   * 
   * @param units
   *        The text units to include on serialization.
   */
  void setTextUnits(Set<PdfTextUnit> units);

  // ==========================================================================

  /**
   * Returns the semantic roles of text units to include on serialization.
   * 
   * @return The semantic roles of text units to include on serialization.
   */
  Set<PdfRole> getRoles();

  /**
   * Sets the semantic roles of text units to include on serialization.
   * 
   * @param roles
   *        The semantic roles of text units to include on serialization.
   */
  void setRoles(Set<PdfRole> roles);

  // ==========================================================================

  /**
   * The factory to create instances of PdfSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfSerializerFactory {
    /**
     * Creates a new PdfSerializer.
     * 
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create();

    /**
     * Creates a new PdfSerializer.
     * 
     * @param units
     *        The text units to extract.
     * @param roles
     *        The semantic roles of text units to extract.
     * 
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create(Set<PdfTextUnit> units, Set<PdfRole> roles);
  }
}
