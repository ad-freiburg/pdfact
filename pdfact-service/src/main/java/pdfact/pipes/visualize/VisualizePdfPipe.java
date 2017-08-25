package pdfact.pipes.visualize;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import pdfact.model.ElementType;
import pdfact.model.SemanticRole;
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
   * Returns the element types filter.
   * 
   * @return The element types filter.
   */
  Set<ElementType> getElementTypesFilters();

  /**
   * Sets the element types filter to include on visualization.
   * 
   * @param types
   *        The element types filter to include on visualization.
   */
  void setElementTypesFilters(Set<ElementType> types);

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
