package pdfact.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import pdfact.cli.guice.PdfActCliGuiceModule;
import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.cli.pipes.PdfActServicePipe;
import pdfact.cli.pipes.PdfActServicePipe.PdfActServicePipeFactory;
import pdfact.core.model.LogLevel;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.PdfDocument.PdfDocumentFactory;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.Log4JTypeListener;

/**
 * The main class for the command line interface of PdfAct.
 *
 * @author Claudius Korzen
 */
public class PdfAct {
  /**
   * The factory to create new PDF documents.
   */
  protected PdfDocumentFactory pdfDocumentFactory;

  /**
   * The factory to create service pipes of PdfAct.
   */
  protected PdfActServicePipeFactory serviceFactory;

  // ==========================================================================

  /**
   * The log level.
   */
  protected LogLevel logLevel = LogLevel.WARN;

  /**
   * The serialization format.
   */
  protected SerializeFormat serializationFormat;

  /**
   * The path to the serialization target.
   */
  protected Path serializationPath;

  /**
   * The serialization stream.
   */
  protected OutputStream serializationStream;

  /**
   * The path to the visualization target.
   */
  protected Path visualizationPath;

  /**
   * The text unit to extract.
   */
  protected TextUnit textUnit;

  /**
   * The semantic roles of the text units to extract.
   */
  protected Set<SemanticRole> semanticRoles;

  // ==========================================================================

  /**
   * Creates a new command line interface of PdfAct.
   */
  public PdfAct() {
    Injector injector = Guice.createInjector(new PdfActCliGuiceModule());
    this.pdfDocumentFactory = injector.getInstance(PdfDocumentFactory.class);
    this.serviceFactory = injector.getInstance(PdfActServicePipeFactory.class);
  }

  /**
   * Parses the PDF file given by a string path.
   *
   * @param pdfPath
   *     The path to the PDF file to parse.
   *
   * @return The parsed PDF document.
   *
   * @throws PdfActException
   *     If something went wrong on parsing the PDF.
   */
  public PdfDocument parse(String pdfPath) throws PdfActException {
    return parse(Paths.get(pdfPath));
  }

  /**
   * Parses the PDF file given by the path.
   *
   * @param pdfPath
   *     The path to the PDF file to parse.
   *
   * @return The parsed PDF document.
   *
   * @throws PdfActException
   *     If something went wrong on parsing the PDF.
   */
  public PdfDocument parse(Path pdfPath) throws PdfActException {
    // Create a service pipe.
    PdfActServicePipe service = this.serviceFactory.create();

    // Create the PDF document from the given path.
    PdfDocument pdf = this.pdfDocumentFactory.create(pdfPath);

    // Pass the log level.
    Log4JTypeListener.setLogLevel(this.logLevel);

    // Pass the serialization format if there is any.
    if (this.serializationFormat != null) {
      service.setSerializationFormat(this.serializationFormat);
    }

    // Pass the path to the serialization target.
    if (this.serializationPath != null) {
      service.setSerializationPath(this.serializationPath);
    }

    // Pass the serialization stream.
    if (this.serializationStream != null) {
      service.setSerializationStream(this.serializationStream);
    }

    // Pass the target of the visualization.
    if (this.visualizationPath != null) {
      service.setVisualizationPath(this.visualizationPath);
    }

    // Pass the chosen text unit.
    if (this.textUnit != null) {
      service.setTextUnit(this.textUnit);
    }

    // Pass the semantic roles filter for serialization & visualization.
    if (this.semanticRoles != null) {
      service.setSemanticRolesFilters(this.semanticRoles);
    }

    // Configurator.setLevel("class org.apache.pdfbox.pdmodel.font.PDSimpleFont", Level.OFF);
    // Configurator.setLevel("org.apache.pdfbox.pdmodel.font.PDSimpleFont", Level.OFF);

    // Run PdfAct.
    service.execute(pdf);

    return pdf;
  }

  // ==========================================================================

  /**
   * Returns the log level.
   *
   * @return The log level.
   */
  public LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Sets the log level.
   *
   * @param logLevel
   *     The log level.
   */
  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  // ==========================================================================

  /**
   * Returns the serialization format.
   *
   * @return The serialization format.
   */
  public SerializeFormat getSerializationFormat() {
    return serializationFormat;
  }

  /**
   * Sets the serialization format.
   *
   * @param serializationFormat
   *     The serialization format.
   */
  public void setSerializationFormat(SerializeFormat serializationFormat) {
    this.serializationFormat = serializationFormat;
  }

  // ==========================================================================

  /**
   * Returns the path to the serialization target.
   *
   * @return The path to the serialization target.
   */
  public Path getSerializationPath() {
    return serializationPath;
  }

  /**
   * Sets the path to the serialization target.
   *
   * @param serializationPath
   *     The path to the serialization target.
   */
  public void setSerializationPath(Path serializationPath) {
    this.serializationPath = serializationPath;
  }

  /**
   * Returns the serialization stream.
   *
   * @return The serialization stream.
   */
  public OutputStream getSerializationStream() {
    return serializationStream;
  }

  /**
   * Sets the serialization stream.
   *
   * @param serializationStream
   *     The path to the serialization stream.
   */
  public void setSerializationStream(OutputStream serializationStream) {
    this.serializationStream = serializationStream;
  }

  // ==========================================================================

  /**
   * Returns the path to the visualization target.
   *
   * @return The path to the visualization target.
   */
  public Path getVisualizationPath() {
    return visualizationPath;
  }

  /**
   * Sets the path to the visualization target.
   *
   * @param visualizationPath
   *     The path to the visualization target.
   */
  public void setVisualizationPath(Path visualizationPath) {
    this.visualizationPath = visualizationPath;
  }

  // ==========================================================================

  /**
   * Returns the text unit to extract.
   *
   * @return The text unit to extract.
   */
  public TextUnit getTextUnit() {
    return textUnit;
  }

  /**
   * Sets the text unit to extract.
   *
   * @param textUnit
   *     The text unit to extract.
   */
  public void setTextUnit(TextUnit textUnit) {
    this.textUnit = textUnit;
  }

  // ==========================================================================

  /**
   * Returns the semantic roles of the text units to extract.
   *
   * @return The semantic roles of the text units to extract.
   */
  public Set<SemanticRole> getSemanticRoles() {
    return semanticRoles;
  }

  /**
   * Sets the semantic roles of the text units to extract.
   *
   * @param semanticRoles
   *     The semantic roles of the text units to extract.
   */
  public void setSemanticRoles(Set<SemanticRole> semanticRoles) {
    this.semanticRoles = semanticRoles;
  }
}

