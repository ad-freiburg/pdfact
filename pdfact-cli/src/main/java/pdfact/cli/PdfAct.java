package pdfact.cli;

import static org.apache.logging.log4j.Level.DEBUG;
import static org.apache.logging.log4j.Level.ERROR;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.apache.logging.log4j.core.config.Configurator;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializationFormat;
import pdfact.cli.pipes.PdfActServicePipe;
import pdfact.cli.pipes.PlainPdfActServicePipe;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * The main class of PdfAct.
 *
 * @author Claudius Korzen
 */
public class PdfAct {
  /**
   * The serialization format.
   */
  protected SerializationFormat serializationFormat;

  /**
   * The path to file to which the serialization should be written.
   */
  protected Path serializationPath;

  /**
   * The stream (e.g., System.out) to which the serialization should be written.
   */
  protected OutputStream serializationStream;

  /**
   * The path to the file to which the visualization PDF should be written.
   */
  protected Path visualizationPath;

  /**
   * The units of text to extract (e.g., "characters", "words", etc.).
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles of the text units to extract (e.g., "title", "author", etc.)
   */
  protected Set<SemanticRole> semanticRoles;

  /**
   * A boolean flag indicating whether or not to print debug info about the PDF parsing step.
   */
  protected boolean isDebugPdfParsing;

  /**
   * A boolean flag indicating whether or not to print debug info about the extracted characters.
   */
  protected boolean isDebugCharacterExtraction;

  /**
   * A boolean flag indicating whether or not to print debug info about the text line detection.
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
   * The boolean flag indicating whether or not to print debug info about the roles detection.
   */
  protected boolean isDebugRoleDetection;

  /**
   * The boolean flag indicating whether or not to print debug info about the paragraphs detection.
   */
  protected boolean isDebugParagraphDetection;

  /**
   * The boolean flag indicating whether or not to print debug info about the word dehyphenation.
   */
  protected boolean isDebugWordDehyphenation;

  /**
   * A boolean flag indicating whether or not to insert certain control characters into the TXT
   * serialization output, for example: (1) the character "^L" ("form feed"), representing a page
   * break between two PDF elements; or (2) the character "^A" ("start of heading"), identifying the
   * headings of a document.
   */
  protected boolean insertControlCharacters;

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
    // Set the different debug levels according to the given debug flags.
    Configurator.setLevel("pdf-parsing", this.isDebugPdfParsing ? DEBUG : ERROR);
    Configurator.setLevel("char-extraction", this.isDebugCharacterExtraction ? DEBUG : ERROR);
    Configurator.setLevel("line-detection", this.isDebugTextLineDetection ? DEBUG : ERROR);
    Configurator.setLevel("word-detection", this.isDebugWordDetection ? DEBUG : ERROR);
    Configurator.setLevel("block-detection", this.isDebugTextBlockDetection ? DEBUG : ERROR);
    Configurator.setLevel("role-detection", this.isDebugRoleDetection ? DEBUG : ERROR);
    Configurator.setLevel("paragraph-detection", this.isDebugParagraphDetection ? DEBUG : ERROR);
    Configurator.setLevel("word-dehyphenation", this.isDebugWordDehyphenation ? DEBUG : ERROR);

    // Create a service pipe.
    PdfActServicePipe service = new PlainPdfActServicePipe();

    // Pass the serialization format, if there is any.
    if (this.serializationFormat != null) {
      service.setSerializationFormat(this.serializationFormat);
    }

    // Pass the path to the serialization file, if there is any.
    if (this.serializationPath != null) {
      service.setSerializationPath(this.serializationPath);
    }

    // Pass the serialization stream, if there is any.
    if (this.serializationStream != null) {
      service.setSerializationStream(this.serializationStream);
    }

    // Pass the path to the visualization file.
    if (this.visualizationPath != null) {
      service.setVisualizationPath(this.visualizationPath);
    }

    // Pass the units of text to extract.
    if (this.extractionUnits != null) {
      service.setExtractionUnits(this.extractionUnits);
    }

    // Pass the semantic roles to include for serialization & visualization.
    if (this.semanticRoles != null) {
      service.setSemanticRolesToInclude(this.semanticRoles);
    }

    service.setInsertControlCharacters(this.insertControlCharacters);

    // Create the PDF document from the given path.
    Document pdf = new Document(pdfPath);

    // Run PdfAct.
    service.execute(pdf);

    return pdf;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the PDF parsing step should be printed, false otherwise.
   */
  public boolean isDebugPdfParsing() {
    return isDebugPdfParsing;
  }

  /**
   * Sets whether or not debug info about the PDF parsing step should be printed.
   */
  public void setDebugPdfParsing(boolean debugPdfParsing) {
    this.isDebugPdfParsing = debugPdfParsing;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the character extraction step should be printed, false
   * otherwise.
   */
  public boolean isDebugCharacterExtraction() {
    return isDebugCharacterExtraction;
  }

  /**
   * Sets whether or not debug info about the characters extraction step should be printed.
   */
  public void setDebugCharacterExtraction(boolean debugCharacterExtraction) {
    this.isDebugCharacterExtraction = debugCharacterExtraction;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the text line detection step should be printed, false
   * otherwise.
   */
  public boolean isDebugTextLineDetection() {
    return isDebugTextLineDetection;
  }

  /**
   * Sets whether or not debug info about the text line detection step should be printed.
   */
  public void setDebugTextLineDetection(boolean isDebugTextLineDetection) {
    this.isDebugTextLineDetection = isDebugTextLineDetection;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the word detection step should be printed, false otherwise.
   */
  public boolean isDebugWordDetection() {
    return isDebugWordDetection;
  }

  /**
   * Sets whether or not debug info about the word detection step should be printed.
   */
  public void setDebugWordDetection(boolean isDebugWordDetection) {
    this.isDebugWordDetection = isDebugWordDetection;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the block detection step should be printed, false otherwise.
   */
  public boolean isDebugTextBlockDetection() {
    return isDebugTextBlockDetection;
  }

  /**
   * Sets whether or not debug info about the block detection step should be printed.
   */
  public void setDebugTextBlockDetection(boolean isDebugTextBlockDetection) {
    this.isDebugTextBlockDetection = isDebugTextBlockDetection;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the roles detection step should be printed, false otherwise.
   */
  public boolean isDebugRoleDetection() {
    return isDebugRoleDetection;
  }

  /**
   * Sets whether or not debug info about the roles detection step should be printed.
   */
  public void setDebugRoleDetection(boolean isDebugRoleDetection) {
    this.isDebugRoleDetection = isDebugRoleDetection;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the paragraphs detection step should be printed, false
   * otherwise.
   */
  public boolean isDebugParagraphDetection() {
    return isDebugParagraphDetection;
  }

  /**
   * Sets whether or not debug info about the paragraphs detection step should be printed.
   */
  public void setDebugParagraphDetection(boolean isDebugParagraphDetection) {
    this.isDebugParagraphDetection = isDebugParagraphDetection;
  }

  // ==============================================================================================

  /**
   * Returns true if debug info about the word dehyphenation step should be printed, false
   * otherwise.
   */
  public boolean isDebugWordDehyphenation() {
    return isDebugWordDehyphenation;
  }

  /**
   * Sets whether or not debug info about the word dehyphenation step should be printed.
   */
  public void setDebugWordDehyphenation(boolean isDebugWordDehyphenation) {
    this.isDebugWordDehyphenation = isDebugWordDehyphenation;
  }

  // ==============================================================================================

  /**
   * Returns the serialization format.
   *
   * @return The serialization format.
   */
  public SerializationFormat getSerializationFormat() {
    return serializationFormat;
  }

  /**
   * Sets the serialization format.
   *
   * @param serializationFormat The serialization format.
   */
  public void setSerializationFormat(SerializationFormat serializationFormat) {
    this.serializationFormat = serializationFormat;
  }

  // ==============================================================================================

  /**
   * Returns the path to the file to which the serialization output should be written.
   */
  public Path getSerializationPath() {
    return serializationPath;
  }

  /**
   * Sets the path to the file to which the serialization output should be written.
   */
  public void setSerializationPath(Path serializationPath) {
    this.serializationPath = serializationPath;
  }

  /**
   * Returns the stream to which the serialization output should be written.
   */
  public OutputStream getSerializationStream() {
    return serializationStream;
  }

  /**
   * Sets the stream to which the serialization output should be written.
   */
  public void setSerializationStream(OutputStream serializationStream) {
    this.serializationStream = serializationStream;
  }

  // ==============================================================================================

  /**
   * Returns the path to the file to which the visualization should be written.
   */
  public Path getVisualizationPath() {
    return visualizationPath;
  }

  /**
   * Sets the path to the file to which the visualization should be written.
   */
  public void setVisualizationPath(Path visualizationPath) {
    this.visualizationPath = visualizationPath;
  }

  // ==============================================================================================

  /**
   * Returns the set of text units to extract.
   */
  public Set<ExtractionUnit> getExtractionUnits() {
    return extractionUnits;
  }

  /**
   * Sets the text units to extract.
   */
  public void setExtractionUnits(Set<ExtractionUnit> extractionUnits) {
    this.extractionUnits = extractionUnits;
  }

  // ==============================================================================================

  /**
   * Returns the set of semantic roles of the text units to extract.
   */
  public Set<SemanticRole> getSemanticRoles() {
    return semanticRoles;
  }

  /**
   * Sets the semantic roles of the text units to extract.
   */
  public void setSemanticRoles(Set<SemanticRole> semanticRoles) {
    this.semanticRoles = semanticRoles;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not to insert certain control characters into
   * the TXT serialization output, for example: (1) the character "^L" ("form feed"), representing a
   * page break between two PDF elements; or (2) the character "^A" ("start of heading"),
   * identifying the headings of a document.
   */
  public boolean isInsertControlCharacters() {
    return this.insertControlCharacters;
  }

  /**
   * Sets the boolean flag indicating whether or not to insert certain control characters into the
   * TXT serialization output, for example: (1) the character "^L" ("form feed"), representing a
   * page break between two PDF elements; or (2) the character "^A" ("start of heading"),
   * identifying the headings of a document.
   */
  public void setInsertControlCharacters(boolean insertControlCharacters) {
    this.insertControlCharacters = insertControlCharacters;
  }
}

