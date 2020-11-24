package pdfact.cli.pipes.visualize;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.util.exception.PdfActSerializeException;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link VisualizePdfPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainVisualizePdfPipe implements VisualizePdfPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainVisualizePdfPipe.class);

  /**
   * The serialization target, given as a path.
   */
  protected Path targetPath;

  /**
   * The serialization target, given as a stream.
   */
  protected OutputStream targetStream;

  /**
   * The units to visualize.
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles filter.
   */
  protected Set<SemanticRole> roles;

  // ==============================================================================================

  @Override
  public Document execute(Document pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Visualizing the PDF document.");
    visualize(pdf);

    log.debug("Visualizing the PDF document done.");
    log.debug("text unit: " + this.extractionUnits);
    log.debug("semantic roles: " + this.roles);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");

    return pdf;
  }

  // ==============================================================================================

  /**
   * Visualizes the given PDF document.
   * 
   * @param pdf
   *        The PDf document to serialize.
   * 
   * @throws PdfActException
   *         If something went wrong while serializing the PDF document.
   */
  protected void visualize(Document pdf) throws PdfActException {
    // Create the visualizer.
    PdfVisualizer visualizer = new PlainPdfVisualizer(this.extractionUnits, this.roles);

    // Serialize the PDF document.
    byte[] visualization = visualizer.visualize(pdf);

    // If the target is given as a stream, write the serialization it.
    if (this.targetStream != null) {
      writeToStream(visualization, this.targetStream);
    }

    // If the target is given as a file, open it and write the serialization.
    if (this.targetPath != null) {
      writeToFile(visualization, this.targetPath);
    }
  }

  // ==============================================================================================

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
  protected void writeToFile(byte[] bytes, Path path)
      throws PdfActSerializeException {
    try (OutputStream os = Files.newOutputStream(path)) {
      os.write(bytes);
    } catch (IOException e) {
      throw new PdfActSerializeException("Couldn't write to file.");
    }
  }

  // ==============================================================================================

  @Override
  public Set<ExtractionUnit> getExtractionUnits() {
    return this.extractionUnits;
  }

  @Override
  public void setExtractionUnits(Set<ExtractionUnit> units) {
    this.extractionUnits = units;
  }

  // ==============================================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilters() {
    return this.roles;
  }

  @Override
  public void setSemanticRolesFilters(Set<SemanticRole> roles) {
    this.roles = roles;
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
