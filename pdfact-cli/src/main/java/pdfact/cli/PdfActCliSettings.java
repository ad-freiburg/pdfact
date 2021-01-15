package pdfact.cli;

import java.util.HashSet;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializeFormat;
import pdfact.core.model.SemanticRole;

/**
 * Some global settings to control the behavior of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCliSettings {
  /**
   * The default serialization format.
   */
  public static final SerializeFormat DEFAULT_SERIALIZE_FORMAT =
      SerializeFormat.TXT;

  /**
   * The default text unit to include on serialization and visualization.
   */
  public static final Set<ExtractionUnit> DEFAULT_EXTRACTION_UNITS;
  
  static {
    DEFAULT_EXTRACTION_UNITS = new HashSet<>();
    DEFAULT_EXTRACTION_UNITS.add(ExtractionUnit.PARAGRAPH);
  } 

  /**
   * The default semantic roles of text units to include on serialization and
   * visualization.
   */
  public static final Set<SemanticRole> DEFAULT_SEMANTIC_ROLES;
  
  static {
    DEFAULT_SEMANTIC_ROLES = new HashSet<>();
  }


  /**
   * The default boolean flag indicating whether or not the TXT serializer should insert a page
   * break marker between two PDF elements in case a page break between the two elements occurs in
   * the PDF.
   */
  public static final boolean DEFAULT_INSERT_PAGE_BREAK_MARKERS = false;
}
