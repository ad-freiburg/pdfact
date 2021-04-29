package pdfact.cli.pipes;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializationFormat;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that wires up all necessary steps to identify the document structure of PDF documents,
 * serialize and visualize them to files.
 *
 * @author Claudius Korzen
 */
public interface PdfActServicePipe extends Pipe {
  /**
   * Returns the path to the file where the serialization should be stored.
   *
   * @return The path to the file where the serialization should be stored.
   */
  Path getSerializationPath();

  /**
   * Sets the path to the file where the serialization should be stored.
   *
   * @param path The path to the file where the serialization should be stored.
   */
  void setSerializationPath(Path path);

  // ==============================================================================================

  /**
   * Returns the stream where the serialization should be stored.
   *
   * @return The stream where the serialization should be stored.
   */
  OutputStream getSerializationStream();

  /**
   * Sets the stream where the serialization should be stored.
   *
   * @param stream The path to the file where the serialization should be stored.
   */
  void setSerializationStream(OutputStream stream);

  // ==============================================================================================

  /**
   * Returns the target serialization format.
   *
   * @return The target serialization format.
   */
  SerializationFormat getSerializationFormat();

  /**
   * Sets the target serialization format.
   *
   * @param format The target serialization format.
   */
  void setSerializationFormat(SerializationFormat format);

  // ==============================================================================================

  /**
   * Returns the path to the file where the visualization should be stored.
   *
   * @return The path to the file where the visualization should be stored.
   */
  Path getVisualizationPath();

  /**
   * Sets the path to the file where the visualization should be stored.
   *
   * @param path The path to the file where the visualization should be stored.
   */
  void setVisualizationPath(Path path);

  // ==============================================================================================

  /**
   * Returns the stream where the visualization should be stored.
   *
   * @return The stream where the visualization should be stored.
   */
  OutputStream getVisualizationStream();

  /**
   * Sets the stream where the visualization should be stored.
   *
   * @param stream The path to the file where the visualization should be stored.
   */
  void setVisualizationStream(OutputStream stream);

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
   * @param roles The semantic roles to include.
   */
  void setSemanticRolesToInclude(Set<SemanticRole> roles);

  // ==============================================================================================

  /**
   * Returns the unit to extract.
   *
   * @return The unit to extract.
   */
  Set<ExtractionUnit> getExtractionUnits();

  /**
   * Sets the units to extract.
   *
   * @param units The units to extract.
   */
  void setExtractionUnits(Set<ExtractionUnit> units);

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public boolean isWithControlCharacters();

  /**
   * Sets the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public void setInsertControlCharacters(boolean withControlCharacters);

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not the pdf.js mode is enabled.
   */
  public boolean isPdfJsMode();

  /**
   * Sets the boolean flag indicating whether or not the pdf.js mode is enabled.
   */
  public void setIsPdfJsMode(boolean isPdfJsMode);
}
