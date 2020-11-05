package pdfact.cli.pipes;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that wires up all necessary steps to identify the document structure
 * of PDF documents, serialize and visualize them to files.
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
   * @param path
   *        The path to the file where the serialization should be stored.
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
   * @param stream
   *        The path to the file where the serialization should be stored.
   */
  void setSerializationStream(OutputStream stream);

  // ==============================================================================================

  /**
   * Returns the target serialization format.
   * 
   * @return The target serialization format.
   */
  SerializeFormat getSerializationFormat();

  /**
   * Sets the target serialization format.
   * 
   * @param format
   *        The target serialization format.
   */
  void setSerializationFormat(SerializeFormat format);

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
   * @param path
   *        The path to the file where the visualization should be stored.
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
   * @param stream
   *        The path to the file where the visualization should be stored.
   */
  void setVisualizationStream(OutputStream stream);

  // ==============================================================================================

  /**
   * Returns the semantic roles filters.
   * 
   * @return The semantic roles filters.
   */
  Set<SemanticRole> getSemanticRolesFilters();

  /**
   * Sets the semantic roles filters.
   * 
   * @param filters
   *        The semantic roles filters.
   */
  void setSemanticRolesFilters(Set<SemanticRole> filters);

  // ==============================================================================================

  /**
   * Returns the text unit.
   * 
   * @return The text unit.
   */
  TextUnit getTextUnit();

  /**
   * Sets the text unit.
   * 
   * @param unit
   *        The text unit.
   */
  void setTextUnit(TextUnit unit);
}
