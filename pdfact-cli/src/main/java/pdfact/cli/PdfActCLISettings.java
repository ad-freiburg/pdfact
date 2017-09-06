package pdfact.cli;

import java.util.HashSet;
import java.util.Set;

import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.core.model.SemanticRole;

/**
 * Some global settings to control the behavior of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCLISettings {
  /**
   * The default serialization format.
   */
  public static final SerializeFormat DEFAULT_SERIALIZE_FORMAT =
      SerializeFormat.TXT;

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
