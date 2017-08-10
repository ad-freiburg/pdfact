package pdfact.serialize;

import java.util.Set;

import pdfact.exception.PdfActException;
import pdfact.models.PdfDocument;
import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

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
   * @return The serialization.
   * @throws PdfActException
   *         If the serilization failed.
   */
  byte[] serialize(PdfDocument pdf) throws PdfActException;

  // ==========================================================================

  /**
   * Returns the text units to include on serialization.
   * 
   * @return The text units to include on serialization.
   */
  Set<PdfElementType> getElementTypeFilters();

  /**
   * Sets the text units to include on serialization.
   * 
   * @param units
   *        The text units to include on serialization.
   */
  void setElementTypeFilters(Set<PdfElementType> units);

  // ==========================================================================

  /**
   * Returns the semantic roles of text units to include on serialization.
   * 
   * @return The semantic roles of text units to include on serialization.
   */
  Set<PdfRole> getElementRoleFilters();

  /**
   * Sets the semantic roles of text units to include on serialization.
   * 
   * @param roles
   *        The semantic roles of text units to include on serialization.
   */
  void setElementRoleFilters(Set<PdfRole> roles);

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
    PdfSerializer create(Set<PdfElementType> units, Set<PdfRole> roles);
  }
}
