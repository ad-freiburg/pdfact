package pdfact.cli.pipes.serialize;

import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
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

  // ==============================================================================================

  /**
   * Returns the units to extract.
   * 
   * @return The units to extract.
   */
  Set<ExtractionUnit> getExtractionUnits();

  /**
   * Sets the units to extract.
   * 
   * @param extractionUnits
   *        The extraction units.
   */
  void setExtractionUnits(Set<ExtractionUnit> extractionUnits);

  // ==============================================================================================

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
}
