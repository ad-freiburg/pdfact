package pdfact.cli;

import java.util.HashSet;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializationFormat;
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
  public static final SerializationFormat DEFAULT_SERIALIZE_FORMAT =
      SerializationFormat.TXT;

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
   * The default boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public static final boolean DEFAULT_WITH_CONTROL_CHARACTERS = false;
}
