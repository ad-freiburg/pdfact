package pdfact.cli;

import static org.apache.logging.log4j.Level.DEBUG;
import static org.apache.logging.log4j.Level.ERROR;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.apache.logging.log4j.core.config.Configurator;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializeFormat;
import pdfact.cli.pipes.PdfActServicePipe;
import pdfact.cli.pipes.PlainPdfActServicePipe;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * The main class for the command line interface of PdfAct.
 *
 * @author Claudius Korzen
 */
public class PdfAct {
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
   * The units to extract.
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles of the text units to extract.
   */
  protected Set<SemanticRole> semanticRoles;

  /**
   * The boolean flag indicating whether or not to print debug info about the parsed PDF operators.
   */
  protected boolean isDebugPdfOperators;

  /**
   * The boolean flag indicating whether or not to print debug info about the characters extraction.
   */
  protected boolean isDebugCharactersExtraction;

  /**
   * The boolean flag indicating whether or not to print debug info about the text line detection.
   */
  protected boolean isDebugTextLineDetection;

  /**
   * The boolean flag indicating whether or not to print debug info about the word detection.
   */
  protected boolean isDebugWordDetection;

  /**
   * The boolean flag indicating whether or not to print debug info about the text block detection.
   */
  protected boolean isDebugTextBlockDetection;

  /**
   * The boolean flag indicating whether or not this serializer should insert control characters,
   * i.e.: "^L" between two PDF elements in case a page break between the two elements occurs in the
   * PDF and "^A" in front of headings.
   */
  protected boolean withControlCharacters;

  // ==============================================================================================

  /**
   * Parses the PDF file given by a string path.
   *
   * @param pdfPath The path to the PDF file to parse.
   *
   * @return The parsed PDF document.
   *
   * @throws PdfActException If something went wrong on parsing the PDF.
   */
  public Document parse(String pdfPath) throws PdfActException {
    return parse(Paths.get(pdfPath));
  }

  /**
   * Parses the PDF file given by the path.
   *
   * @param pdfPath The path to the PDF file to parse.
   *
   * @return The parsed PDF document.
   *
   * @throws PdfActException If something went wrong on parsing the PDF.
   */
  public Document parse(Path pdfPath) throws PdfActException {
    // Create a service pipe.
    PdfActServicePipe service = new PlainPdfActServicePipe();

    // Set the debug levels according to the given debug flags.
    Configurator.setLevel("pdf-operators", this.isDebugPdfOperators ? DEBUG : ERROR);
    // Configurator.setLevel("pdf-parsing", this.isDebugPdfOperators ? Level.DEBUG : Level.ERROR);
    Configurator.setLevel("char-extraction", this.isDebugCharactersExtraction ? DEBUG : ERROR);
    Configurator.setLevel("line-detection", this.isDebugTextLineDetection ? DEBUG : ERROR);
    Configurator.setLevel("word-detection", this.isDebugWordDetection ? DEBUG : ERROR);
    Configurator.setLevel("block-detection", this.isDebugTextBlockDetection ? DEBUG : ERROR);
    // Configurator.setLevel("semantic-roles-detection", this.isDebugPdfOperators ? Level.DEBUG :
    // Level.ERROR);
    // Configurator.setLevel("paragraphs-detection", this.isDebugPdfOperators ? Level.DEBUG :
    // Level.ERROR);
    // Configurator.setLevel("word-dehyphenation", this.isDebugPdfOperators ? Level.DEBUG :
    // Level.ERROR);


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

    // Pass the chosen extraction units.
    if (this.extractionUnits != null) {
      service.setExtractionUnits(this.extractionUnits);
    }

    // Pass the semantic roles to include for serialization & visualization.
    if (this.semanticRoles != null) {
      service.setSemanticRolesToInclude(this.semanticRoles);
    }

    service.setWithControlCharacters(this.withControlCharacters);

    // Create the PDF document from the given path.
    Document pdf = new Document(pdfPath);

    // Run PdfAct.
    service.execute(pdf);

    return pdf;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to print debug info about the parsed PDF
   * operators.
   *
   * @return The boolean flag.
   */
  public boolean isDebugPdfOperators() {
    return isDebugPdfOperators;
  }

  /**
   * Sets the boolean flag indicating whether or not to print debug info about the parsed PDF
   * operators.
   *
   * @param debugPdfOperators The boolean flag.
   */
  public void setDebugPdfOperators(boolean debugPdfOperators) {
    this.isDebugPdfOperators = debugPdfOperators;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to print debug info about the characters
   * extraction.
   *
   * @return The boolean flag.
   */
  public boolean isDebugCharactersExtraction() {
    return isDebugCharactersExtraction;
  }

  /**
   * Sets the boolean flag indicating whether or not to print debug info about the characters
   * extraction.
   *
   * @param debugCharactersExtraction The boolean flag.
   */
  public void setDebugCharactersExtraction(boolean debugCharactersExtraction) {
    this.isDebugCharactersExtraction = debugCharactersExtraction;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to print debug info about the text line
   * detection.
   *
   * @return The boolean flag.
   */
  public boolean isDebugTextLineDetection() {
    return isDebugTextLineDetection;
  }

  /**
   * Sets the boolean flag indicating whether or not to print debug info about the text line
   * detection.
   *
   * @param isDebugTextLineDetection The boolean flag.
   */
  public void setDebugTextLineDetection(boolean isDebugTextLineDetection) {
    this.isDebugTextLineDetection = isDebugTextLineDetection;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to print debug info about the word
   * detection.
   *
   * @return The boolean flag.
   */
  public boolean isDebugWordDetection() {
    return isDebugWordDetection;
  }

  /**
   * Sets the boolean flag indicating whether or not to print debug info about the word detection.
   *
   * @param isDebugTextBlockDetection The boolean flag.
   */
  public void setDebugWordDetection(boolean isDebugWordDetection) {
    this.isDebugWordDetection = isDebugWordDetection;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to print debug info about the text block
   * detection.
   *
   * @return The boolean flag.
   */
  public boolean isDebugTextBlockDetection() {
    return isDebugTextBlockDetection;
  }

  /**
   * Sets the boolean flag indicating whether or not to print debug info about the text line
   * detection.
   *
   * @param isDebugTextBlockDetection The boolean flag.
   */
  public void setDebugTextBlockDetection(boolean isDebugTextBlockDetection) {
    this.isDebugTextBlockDetection = isDebugTextBlockDetection;
  }

  // ==============================================================================================

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
   * @param serializationFormat The serialization format.
   */
  public void setSerializationFormat(SerializeFormat serializationFormat) {
    this.serializationFormat = serializationFormat;
  }

  // ==============================================================================================

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
   * @param serializationPath The path to the serialization target.
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
   * @param serializationStream The path to the serialization stream.
   */
  public void setSerializationStream(OutputStream serializationStream) {
    this.serializationStream = serializationStream;
  }

  // ==============================================================================================

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
   * @param visualizationPath The path to the visualization target.
   */
  public void setVisualizationPath(Path visualizationPath) {
    this.visualizationPath = visualizationPath;
  }

  // ==============================================================================================

  /**
   * Returns the units to extract.
   *
   * @return The units to extract.
   */
  public Set<ExtractionUnit> getExtractionUnits() {
    return extractionUnits;
  }

  /**
   * Sets the units to extract.
   *
   * @param extractionUnits The units to extract.
   */
  public void setExtractionUnits(Set<ExtractionUnit> extractionUnits) {
    this.extractionUnits = extractionUnits;
  }

  // ==============================================================================================

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
   * @param semanticRoles The semantic roles of the text units to extract.
   */
  public void setSemanticRoles(Set<SemanticRole> semanticRoles) {
    this.semanticRoles = semanticRoles;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public boolean isWithControlCharacters() {
    return this.withControlCharacters;
  }

  /**
   * Sets the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public void setWithControlCharacters(boolean withControlCharacters) {
    this.withControlCharacters = withControlCharacters;
  }
}

