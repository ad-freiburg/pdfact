package pdfact.cli.pipes;

import static pdfact.cli.PdfActCLISettings.DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
import static pdfact.cli.PdfActCLISettings.DEFAULT_SERIALIZE_FORMAT;
import static pdfact.cli.PdfActCLISettings.DEFAULT_TEXT_UNIT;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.cli.pipes.serialize.PlainSerializePdfPipe;
import pdfact.cli.pipes.serialize.SerializePdfPipe;
import pdfact.cli.pipes.validate.PlainValidatePathToWritePipe;
import pdfact.cli.pipes.validate.ValidatePathToWritePipe;
import pdfact.cli.pipes.visualize.PlainVisualizePdfPipe;
import pdfact.cli.pipes.visualize.VisualizePdfPipe;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.pipes.PlainPdfActCorePipe;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.pipeline.Pipeline;
import pdfact.core.util.pipeline.PlainPipeline;

/**
 * A plain implementation of {@link PdfActServicePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfActServicePipe implements PdfActServicePipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainPdfActServicePipe.class);

  // ==============================================================================================

  /**
   * The serialization target, given as a file.
   */
  protected Path serializationPath;

  /**
   * The serialization target, given as a stream.
   */
  protected OutputStream serializationStream;

  // ==============================================================================================

  /**
   * The visualization target, given as a file.
   */
  protected Path visualizationPath;

  /**
   * The visualization target, given as a stream.
   */
  protected OutputStream visualizationStream;

  // ==============================================================================================

  /**
   * The serialization format.
   */
  protected SerializeFormat serializationFormat;

  /**
   * The text unit to use in serialization and visualization.
   */
  protected TextUnit textUnit;

  /**
   * The roles of text units to be included in serialization and visualization.
   */
  protected Set<SemanticRole> roles;

  // ==============================================================================================

  /**
   * The default constructor.
   */
  public PlainPdfActServicePipe() {
    this.serializationFormat = DEFAULT_SERIALIZE_FORMAT;
    this.textUnit = DEFAULT_TEXT_UNIT;
    this.roles = DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
  }

  // ==============================================================================================

  /**
   * Processes the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return The PDF document after processing.
   * 
   * @throws PdfActException
   *         If something went wrong on processing the PDF document.
   */
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Processing the service pipeline.");
    
    Pipeline pipeline = new PlainPipeline();

    // Parse the PDF document.
    pipeline.addPipe(new PlainPdfActCorePipe());

    // Validate the target path for the serialization if there is any given.
    if (this.serializationPath != null) {
      ValidatePathToWritePipe valPipe = new PlainValidatePathToWritePipe();
      valPipe.setPath(this.serializationPath);
      pipeline.addPipe(valPipe);
    }

    // Serialize if there is a target given for the serialization.
    if (this.serializationStream != null || this.serializationPath != null) {
      SerializePdfPipe serializePipe = new PlainSerializePdfPipe();
      serializePipe.setSerializationFormat(this.serializationFormat);
      serializePipe.setTextUnit(this.textUnit);
      serializePipe.setSemanticRolesFilters(this.roles);
      serializePipe.setTargetPath(this.serializationPath);
      serializePipe.setTargetStream(this.serializationStream);
      pipeline.addPipe(serializePipe);
    }

    // Validate the target path for the visualization if there is any given.
    if (this.visualizationPath != null) {
      ValidatePathToWritePipe valPipe = new PlainValidatePathToWritePipe();
      valPipe.setPath(this.visualizationPath);
      pipeline.addPipe(valPipe);
    }

    // Visualize if there is a target given for the visualization.
    if (this.visualizationStream != null || this.visualizationPath != null) {
      VisualizePdfPipe visualizePipe = new PlainVisualizePdfPipe();
      visualizePipe.setTextUnit(this.textUnit);
      visualizePipe.setSemanticRolesFilters(this.roles);
      visualizePipe.setTargetPath(this.visualizationPath);
      visualizePipe.setTargetStream(this.visualizationStream);
      pipeline.addPipe(visualizePipe);
    }

    log.debug("# pipes in the pipeline: " + pipeline.size());

    long start = System.currentTimeMillis();
    pipeline.process(pdf);
    long length = System.currentTimeMillis() - start;

    log.debug("Processing the service pipeline done.");
    log.debug("Time needed to process the service pipeline: " + length + "ms.");

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    
    return pdf;
  }

  // ==============================================================================================

  @Override
  public Path getSerializationPath() {
    return this.serializationPath;
  }

  @Override
  public void setSerializationPath(Path path) {
    this.serializationPath = path;
  }

  // ==============================================================================================

  @Override
  public OutputStream getSerializationStream() {
    return this.serializationStream;
  }

  @Override
  public void setSerializationStream(OutputStream stream) {
    this.serializationStream = stream;
  }

  // ==============================================================================================

  @Override
  public SerializeFormat getSerializationFormat() {
    return this.serializationFormat;
  }

  @Override
  public void setSerializationFormat(SerializeFormat format) {
    this.serializationFormat = format;
  }

  // ==============================================================================================

  @Override
  public Path getVisualizationPath() {
    return this.visualizationPath;
  }

  @Override
  public void setVisualizationPath(Path path) {
    this.visualizationPath = path;
  }

  // ==============================================================================================

  @Override
  public OutputStream getVisualizationStream() {
    return this.visualizationStream;
  }

  @Override
  public void setVisualizationStream(OutputStream stream) {
    this.visualizationStream = stream;
  }

  // ==============================================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilters() {
    return this.roles;
  }

  @Override
  public void setSemanticRolesFilters(Set<SemanticRole> filter) {
    this.roles = filter;
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
}