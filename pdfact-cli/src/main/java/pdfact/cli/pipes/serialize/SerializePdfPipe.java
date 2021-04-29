package pdfact.cli.pipes.serialize;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializationFormat;
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
  SerializationFormat getSerializationFormat();

  /**
   * Sets the serialization format.
   *
   * @param format The serialization format.
   */
  void setSerializationFormat(SerializationFormat format);

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
   * @param stream The target stream.
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
   * @param path The target path.
   */
  void setTargetPath(Path path);

  // ==============================================================================================

  /**
   * Returns the units to extract.
   *
   * @return The units to extract.
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
  public void setWithControlCharacters(boolean withControlCharacters);

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
