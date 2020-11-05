package pdfact.cli.pipes.serialize;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe to serialize PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface SerializePdfPipe extends Pipe {
  /**
   * Returns the serialization format.
   * 
   * @return The serialization format.
   */
  SerializeFormat getSerializationFormat();

  /**
   * Sets the serialization format.
   * 
   * @param format
   *        The serialization format.
   */
  void setSerializationFormat(SerializeFormat format);

  // ==============================================================================================
  
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
   * Returns the text unit.
   * 
   * @return The text unit.
   */
  TextUnit getTextUnit();

  /**
   * Sets the text unit.
   * 
   * @param textUnit
   *        The text unit.
   */
  void setTextUnit(TextUnit textUnit);

  // ==============================================================================================

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
}
