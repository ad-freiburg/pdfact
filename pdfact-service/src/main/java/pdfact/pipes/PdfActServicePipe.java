package pdfact.pipes;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import pdfact.model.ElementType;
import pdfact.model.LogLevel;
import pdfact.model.SemanticRole;
import pdfact.util.pipeline.Pipe;
import pdfact.model.PdfSerializationFormat;

/**
 * A pipe that wires up all necessary steps to identify the document structure
 * of PDF documents, serialize and visualize them to files
 * 
 * @author Claudius Korzen
 */
public interface PdfActServicePipe extends Pipe {
  /**
   * Returns the log level.
   * 
   * @return The log level.
   */
  LogLevel getLogLevel();

  /**
   * Returns true, if the log level of this pipe is at least the given log
   * level.
   * 
   * @param level The level to check.
   * @return True, if the log level of this pipe is at least the given log
   *         level.
   */
  boolean hasLogLevel(LogLevel level);

  /**
   * Sets the log level.
   * 
   * @param logLevel
   *        The log level.
   */
  void setLogLevel(LogLevel logLevel);

  // ==========================================================================

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

  // ==========================================================================

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

  // ==========================================================================

  /**
   * Returns the target serialization format.
   * 
   * @return The target serialization format.
   */
  PdfSerializationFormat getSerializationFormat();

  /**
   * Sets the target serialization format.
   * 
   * @param format
   *        The target serialization format.
   */
  void setSerializationFormat(PdfSerializationFormat format);

  // ==========================================================================

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

  // ==========================================================================

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

  // ==========================================================================

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

  // ==========================================================================

  /**
   * Returns the element types filter.
   * 
   * @return The element types filter.
   */
  Set<ElementType> getElementTypesFilters();

  /**
   * Sets the element types filters.
   * 
   * @param filter
   *        The element types filters.
   */
  void setElementTypesFilters(Set<ElementType> filter);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfActServicePipe}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfActServicePipeFactory {
    /**
     * Creates a new PdfActServicePipe.
     * 
     * @return An instance of {@link PdfActServicePipe}.
     */
    PdfActServicePipe create();
  }
}
