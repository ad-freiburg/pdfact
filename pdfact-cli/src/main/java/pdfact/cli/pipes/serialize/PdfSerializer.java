package pdfact.cli.pipes.serialize;

import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.core.model.Document;
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
  byte[] serialize(Document pdf) throws PdfActException;

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
   * Returns the semantic roles to include.
   * 
   * @return The semantic roles to include.
   */
  Set<SemanticRole> getSemanticRolesToInclude();

  /**
   * Sets the semantic roles to include.
   * 
   * @param roles
   *        The semantic roles to include.
   */
  void setSemanticRolesToInclude(Set<SemanticRole> roles);
}
