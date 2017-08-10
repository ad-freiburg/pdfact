package pdfact;

import java.util.HashSet;
import java.util.Set;

import pdfact.model.PdfSerializationFormat;
import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

/**
 * Some global settings to control the behavior of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActSettings {
  // ==========================================================================
  // Serialization settings.

  /**
   * The default serialization format.
   */
  public static final PdfSerializationFormat DEFAULT_SERIALIZATION_FORMAT = 
      PdfSerializationFormat.TXT;

  /**
   * The default text units to include on serialization and visualization.
   */
  public static final Set<PdfElementType> DEFAULT_TEXT_UNITS_TO_INCLUDE;
  static {
    DEFAULT_TEXT_UNITS_TO_INCLUDE = new HashSet<>();
    DEFAULT_TEXT_UNITS_TO_INCLUDE.add(PdfElementType.PARAGRAPH);
  }

  /**
   * The default semantic roles of text units to include on serialization and
   * visualization.
   */
  public static final Set<PdfRole> DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
  static {
    DEFAULT_SEMANTIC_ROLES_TO_INCLUDE = new HashSet<>();
  }
}
