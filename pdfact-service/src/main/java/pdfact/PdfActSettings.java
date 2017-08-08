package pdfact;

import java.util.HashSet;
import java.util.Set;

import pdfact.models.PdfRole;
import pdfact.models.PdfTextUnit;
import pdfact.serialize.PdfActSerializationFormat;

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
  public static final PdfActSerializationFormat DEFAULT_SERIALIZATION_FORMAT = 
      PdfActSerializationFormat.TXT;

  /**
   * The default text units to include on serialization and visualization.
   */
  public static final Set<PdfTextUnit> DEFAULT_TEXT_UNITS_TO_INCLUDE;
  static {
    DEFAULT_TEXT_UNITS_TO_INCLUDE = new HashSet<>();
    DEFAULT_TEXT_UNITS_TO_INCLUDE.add(PdfTextUnit.PARAGRAPH);
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
