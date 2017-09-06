package pdfact.cli.pipes.visualize;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.google.inject.Inject;

import pdfact.cli.model.TextUnit;
import pdfact.cli.pipes.visualize.PdfVisualizer.PdfVisualizerFactory;
import pdfact.cli.util.exception.PdfActSerializeException;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link VisualizePdfPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainVisualizePdfPipe implements VisualizePdfPipe {
  /**
   * The factory to create instances of {@link PdfVisualizer}.
   */
  protected PdfVisualizerFactory factory;

  /**
   * The serialization target, given as a path.
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

  /**
   * The default constructor.
   * 
   * @param visualizerFactory
   *        The factory to create instances of {@link PdfVisualizer}.
   */
  @Inject
  public PlainVisualizePdfPipe(PdfVisualizerFactory visualizerFactory) {
    this.factory = visualizerFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    // Create the visualizer.
    PdfVisualizer visualizer = this.factory.create(this.textUnit, this.roles);

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

    return pdf;
  }

  // ==========================================================================

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

  // ==========================================================================

  @Override
  public TextUnit getTextUnit() {
    return this.textUnit;
  }

  @Override
  public void setTextUnit(TextUnit textUnit) {
    this.textUnit = textUnit;
  }

  // ==========================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilters() {
    return this.roles;
  }

  @Override
  public void setSemanticRolesFilters(Set<SemanticRole> roles) {
    this.roles = roles;
  }

  // ==========================================================================

  @Override
  public OutputStream getTargetStream() {
    return this.targetStream;
  }

  @Override
  public void setTargetStream(OutputStream stream) {
    this.targetStream = stream;
  }

  // ==========================================================================

  @Override
  public Path getTargetPath() {
    return this.targetPath;
  }

  @Override
  public void setTargetPath(Path path) {
    this.targetPath = path;
  }
}
