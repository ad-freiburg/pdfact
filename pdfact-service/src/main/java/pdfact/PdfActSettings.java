package pdfact;

import java.util.HashSet;
import java.util.Set;

import pdfact.model.SemanticRole;
import pdfact.model.SerializationFormat;
import pdfact.model.TextUnit;

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
  public static final SerializationFormat DEFAULT_SERIALIZATION_FORMAT =
      SerializationFormat.TXT;

  /**
   * The default text unit to include on serialization and visualization.
   */
  public static final TextUnit DEFAULT_TEXT_UNIT = TextUnit.PARAGRAPH;

  /**
   * The default semantic roles of text units to include on serialization and
   * visualization.
   */
  public static final Set<SemanticRole> DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
  static {
    DEFAULT_SEMANTIC_ROLES_TO_INCLUDE = new HashSet<>();
  }
}
