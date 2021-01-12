package pdfact.cli.pipes.visualize;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.pipeline.Pipe;

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

  // ==============================================================================================

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

  // ==============================================================================================

  /**
   * Returns the units to visualize.
   * 
   * @return The units to visualize.
   */
  Set<ExtractionUnit> getExtractionUnits();

  /**
   * Sets the units to visualize.
   * 
   * @param units
   *        The units to visualize.
   */
  void setExtractionUnits(Set<ExtractionUnit> units);

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
   * @param roles
   *        The semantic roles to include.
   */
  void setSemanticRolesToInclude(Set<SemanticRole> roles);
}
