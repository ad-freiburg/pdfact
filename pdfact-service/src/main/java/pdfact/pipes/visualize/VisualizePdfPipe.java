package pdfact.pipes.visualize;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import pdfact.model.SemanticRole;
import pdfact.model.TextUnit;
import pdfact.util.pipeline.Pipe;

/**
 * A pipe to visualize PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface VisualizePdfPipe extends Pipe {
  /**
   * Returns the target stream.
   * 
   * @return The target stream.
   */
  OutputStream getTargetStream();

  /**
   * Sets the target stream.
   * 
   * @param stream
   *        The target stream.
   */
  void setTargetStream(OutputStream stream);

  // ==========================================================================

  /**
   * Returns the target path.
   * 
   * @return The target path.
   */
  Path getTargetPath();

  /**
   * Sets the target path.
   * 
   * @param path
   *        The target path.
   */
  void setTargetPath(Path path);

  // ==========================================================================

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

  // ==========================================================================

  /**
   * Returns the semantic roles filter.
   * 
   * @return The semantic roles filter.
   */
  Set<SemanticRole> getSemanticRolesFilters();

  /**
   * Sets the semantic roles filter.
   * 
   * @param roles
   *        The semantic roles filter.
   */
  void setSemanticRolesFilters(Set<SemanticRole> roles);

  // ==========================================================================

  /**
   * The factory to create instances of {@link VisualizePdfPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface VisualizePdfPipeFactory {
    /**
     * Creates a new VisualizePdfPipe.
     * 
     * @return An instance of {@link VisualizePdfPipe}.
     */
    VisualizePdfPipe create();
  }
}
