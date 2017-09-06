package pdfact.cli.pipes.serialize;

import java.util.Set;

import pdfact.cli.model.TextUnit;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

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
   * Returns the text unit.
   * 
   * @return The text unit.
   */
  TextUnit getTextUnit();

  /**
   * Sets the text unit.
   * 
   * @param textUnit
   *        The text unit.
   */
  void setTextUnit(TextUnit textUnit);

  // ==========================================================================

  /**
   * Returns the semantic roles filter.
   * 
   * @return The semantic roles filter.
   */
  Set<SemanticRole> getSemanticRolesFilter();

  /**
   * Sets the semantic roles filter.
   * 
   * @param roles
   *        The semantic roles filter.
   */
  void setSemanticRolesFilter(Set<SemanticRole> roles);

  // ==========================================================================

  /**
   * The factory to create instances of PdfSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface SerializerFactory {
    /**
     * Creates a new PdfSerializer.
     * 
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create();

    /**
     * Creates a new PdfSerializer.
     * 
     * @param textUnit
     *        The text unit.
     * @param roles
     *        The semantic roles filter.
     * 
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create(TextUnit textUnit, Set<SemanticRole> roles);
  }
}
