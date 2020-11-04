package pdfact.cli.pipes.serialize;

import static pdfact.cli.PdfActCLISettings.DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
import static pdfact.cli.PdfActCLISettings.DEFAULT_SERIALIZE_FORMAT;
import static pdfact.cli.PdfActCLISettings.DEFAULT_TEXT_UNIT;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.cli.pipes.serialize.PdfSerializer.SerializerFactory;
import pdfact.cli.util.exception.PdfActSerializeException;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link SerializePdfPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainSerializePdfPipe implements SerializePdfPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The available serializers.
   */
  protected Map<SerializeFormat, SerializerFactory> serializers;

  /**
   * The serialization format.
   */
  protected SerializeFormat format;

  /**
   * The serialization target, given as a file.
   */
  protected Path targetPath;

  /**
   * The serialization target, given as a stream.
   */
  protected OutputStream targetStream;

  /**
   * The text unit.
   */
  protected TextUnit textUnit;

  /**
   * The semantic roles filter.
   */
  protected Set<SemanticRole> roles;

  // ==============================================================================================

  /**
   * The default constructor.
   * 
   * @param serializers
   *        The factories of the available serializers.
   */
  @AssistedInject
  public PlainSerializePdfPipe(
      Map<SerializeFormat, SerializerFactory> serializers) {
    this.serializers = serializers;
    this.format = DEFAULT_SERIALIZE_FORMAT;
    this.textUnit = DEFAULT_TEXT_UNIT;
    this.roles = DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
  }

  // ==============================================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Serializing the PDF document.");
    serialize(pdf);

    log.debug("Serializing the PDF document done.");
    log.debug("serialization format: " + this.format);
    log.debug("text unit: " + this.textUnit);
    log.debug("semantic roles: " + this.roles);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");

    return pdf;
  }

  /**
   * Serializes the given PDF document.
   * 
   * @param pdf
   *        The PDf document to serialize.
   * @throws PdfActException
   *         If something went wrong while serializing the PDF document.
   */
  protected void serialize(PdfDocument pdf) throws PdfActException {
    // Obtain the serializer factory to use.
    SerializerFactory factory = this.serializers.get(this.format);
    if (factory == null) {
      throw new PdfActSerializeException(
          "Couldn't find a serializer for the format '" + this.format + "'.");
    }

    // Create the serializer.
    PdfSerializer serializer = factory.create(this.textUnit, this.roles);
    // Serialize the PDF document.
    byte[] serialization = serializer.serialize(pdf);

    // If the target is given as a stream, write the serialization it.
    if (this.targetStream != null) {
      writeToStream(serialization, this.targetStream);
    }

    // If the target is given as a file, open it and write the serialization.
    if (this.targetPath != null) {
      writeToPath(serialization, this.targetPath);
    }
  }

  /**
   * Writes the given bytes to the given output stream.
   * 
   * @param bytes
   *        The bytes to write.
   * @param stream
   *        The stream to write to.
   * @throws PdfActSerializeException
   *         If something went wrong while writing the bytes to the stream.
   */
  protected void writeToStream(byte[] bytes, OutputStream stream)
      throws PdfActSerializeException {
    try {
      stream.write(bytes);
    } catch (IOException e) {
      throw new PdfActSerializeException("Couldn't write to output stream.", e);
    }
  }

  /**
   * Writes the given bytes to the given file.
   * 
   * @param bytes
   *        The bytes to write.
   * @param path
   *        The file to write to.
   * @throws PdfActSerializeException
   *         If something went wrong while writing the bytes to the stream.
   */
  protected void writeToPath(byte[] bytes, Path path)
      throws PdfActSerializeException {
    try (OutputStream os = Files.newOutputStream(path)) {
      os.write(bytes);
    } catch (IOException e) {
      throw new PdfActSerializeException("Couldn't write to file.");
    }
  }

  // ==============================================================================================

  @Override
  public TextUnit getTextUnit() {
    return this.textUnit;
  }

  @Override
  public void setTextUnit(TextUnit textUnit) {
    this.textUnit = textUnit;
  }

  // ==============================================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilters() {
    return this.roles;
  }

  @Override
  public void setSemanticRolesFilters(Set<SemanticRole> filters) {
    this.roles = filters;
  }

  // ==============================================================================================

  @Override
  public SerializeFormat getSerializationFormat() {
    return this.format;
  }

  @Override
  public void setSerializationFormat(SerializeFormat format) {
    this.format = format;
  }

  // ==============================================================================================

  @Override
  public OutputStream getTargetStream() {
    return this.targetStream;
  }

  @Override
  public void setTargetStream(OutputStream stream) {
    this.targetStream = stream;
  }

  // ==============================================================================================

  @Override
  public Path getTargetPath() {
    return this.targetPath;
  }

  @Override
  public void setTargetPath(Path path) {
    this.targetPath = path;
  }
}
