package icecite;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Provider;

import icecite.exception.IceciteException;
import icecite.exception.IceciteSerializeException;
import icecite.exception.IceciteValidateException;
import icecite.exception.IceciteVisualizeException;
import icecite.models.PdfDocument;
import icecite.models.PdfFeature;
import icecite.models.PdfRole;
import icecite.parse.PdfParser.PdfParserFactory;
import icecite.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import icecite.serialize.PdfSerializer;
import icecite.tokenize.PdfDocumentTokenizer;
import icecite.tokenize.PdfPageTokenizer;
import icecite.visualize.PdfVisualizer.PdfVisualizerFactory;

/**
 * The main entry point of Icecite. This class can be used to wiring up all
 * steps needed to handle and manage the extraction process(es) from PDF
 * file(s).
 * 
 * @author Claudius Korzen
 */
public class Icecite {
  /**
   * The factory to create instances of PdfParser.
   */
  protected PdfParserFactory parserFactory;

  /**
   * The tokenizer.
   */
  protected PdfPageTokenizer pageTokenizer; // TODO: Create a factory.

  /**
   * The factory to create instances of PdfTextSemanticizer.
   */
  protected PdfTextSemanticizerFactory semanticizerFactory;

  /**
   * The document tokenizer.
   */
  protected PdfDocumentTokenizer documentTokenizer; // TODO: Create a factory.

  /**
   * The factory to create instances of PdfSerializer.
   */
  protected Map<String, Provider<PdfSerializer>> serializers;

  /**
   * The factory to create instances of PdfVisualizer.
   */
  protected PdfVisualizerFactory visualizerFactory;

  /**
   * The path to the input file.
   */
  protected Path inputFile;

  /**
   * The path to the output file for the serialization.
   */
  protected Path serializationFile;

  /**
   * The path to the output file for the visualization.
   */
  protected Path visualizationFile;

  /**
   * The output format.
   */
  protected String serializationFormat = "xml";

  /**
   * The features to extract.
   */
  protected Set<PdfFeature> features = PdfFeature.valuesAsSet();

  /**
   * The roles to consider.
   */
  protected Set<PdfRole> roles = PdfRole.valuesAsSet();
  
  // ==========================================================================

  /**
   * The default constructor.
   * 
   * @param parserFactory
   *        The factory to create instances of PdfParser.
   * @param pageTokenizer
   *        The page tokenizer.
   * @param semanticizerFactory
   *        The factory to create instances of PdfTextSemanticizer.
   * @param documentTokenizer
   *        The document tokenizer.
   * @param serializerBindings
   *        The map of available serializers.
   * @param visualizerFactory
   *        The factory to create instances of PdfVisualizer.
   */
  @Inject
  public Icecite(PdfParserFactory parserFactory, PdfPageTokenizer pageTokenizer,
      PdfTextSemanticizerFactory semanticizerFactory,
      PdfDocumentTokenizer documentTokenizer,
      Map<String, Provider<PdfSerializer>> serializerBindings,
      PdfVisualizerFactory visualizerFactory) {
    this.parserFactory = parserFactory;
    this.pageTokenizer = pageTokenizer;
    this.semanticizerFactory = semanticizerFactory;
    this.documentTokenizer = documentTokenizer;
    this.serializers = serializerBindings;
    this.visualizerFactory = visualizerFactory;
  }

  // ==========================================================================

  /**
   * Starts the extraction process.
   * 
   * @return The parsed PDF document.
   * 
   * @throws IceciteException
   *         If something went wrong on the extraction process.
   */
  public PdfDocument run() throws IceciteException {
    PdfDocument document = parse(this.inputFile);
    tokenize(document);
//    semanticize(document);
//    tokenize2(document);

    if (this.serializationFile != null) {
      serialize(document, this.serializationFile);
    }
    if (this.visualizationFile != null) {
      visualize(document, this.visualizationFile);
    }
    return document;
  }

  /**
   * Parses the given input PDF file.
   *
   * @param file
   *        The input file to parse.
   *
   * @return The parsed PDF document.
   * 
   * @throws IceciteException
   *         If something went wrong on parsing the file.
   */
  protected PdfDocument parse(Path file) throws IceciteException {
    // Check if there is an input file given.
    if (file == null) {
      throw new IceciteValidateException("No input file given.");
    }

    // Check if the file exists.
    if (!Files.isRegularFile(file)) {
      throw new IceciteValidateException("The input file doesn't exist.");
    }

    // Check if the file exists.
    if (!Files.isReadable(file)) {
      throw new IceciteValidateException("The input file can't be read.");
    }

    return this.parserFactory.create().parsePdf(file);
  }

  /**
   * Tokenizes the given input PDF file.
   *
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws IceciteException
   *         If something went wrong on tokenizing the file.
   */
  protected void tokenize(PdfDocument pdf) throws IceciteException {
    this.pageTokenizer.tokenizePdfPages(pdf);
  }

  /**
   * Serializes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param target
   *        The path to the target file for the serialization.
   *
   * @throws IceciteException
   *         If something went wrong on serializing the PDF document.
   */
  protected void serialize(PdfDocument pdf, Path target)
      throws IceciteException {
    
    // Check if the serialization file already exists.
    if (Files.exists(target)) {
      // Make sure that the existing serialization file is a regular file.
      if (!Files.isRegularFile(target)) {
        throw new IceciteValidateException(
            "The serialization file already exists, but is no regular file.");
      }

      // Make sure that the existing serialization file is writable.
      if (!Files.isWritable(target)) {
        throw new IceciteValidateException(
            "The serialization file already exists, but isn't writable.");
      }
    }

    // Check if the serialization format is valid.
    if (!this.serializers.containsKey(this.serializationFormat)) {
      throw new IceciteValidateException("The serialization format '"
          + this.serializationFormat + "' is not valid.");
    }

    // Serialize the PDF document.
    try (OutputStream os = Files.newOutputStream(target)) {
      PdfSerializer serializer = this.serializers.get(this.serializationFormat).get();
      serializer.serialize(pdf, os, this.features, this.roles);
    } catch (IOException e) {
      throw new IceciteSerializeException(
          "Couldn't open file '" + target + "'.", e);
    }
  }

  /**
   * Visualizes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to visualize.
   * @param target
   *        The path to the target file for the visualization.
   *
   * @throws IceciteException
   *         If something went wrong on visualizing the PDF document.
   */
  protected void visualize(PdfDocument pdf, Path target)
      throws IceciteException {
    // Check if the visualization file already exists.
    if (Files.exists(target)) {
      // Make sure that the existing visualization file is a regular file.
      if (!Files.isRegularFile(target)) {
        throw new IceciteValidateException(
            "The visualization file already exists, but is no regular file.");
      }

      // Make sure that the existing visualization file is writable.
      if (!Files.isWritable(target)) {
        throw new IceciteValidateException(
            "The visualization file already exists, but isn't writable.");
      }
    }

    // Serialize the PDF document.
    try (OutputStream os = Files.newOutputStream(target)) {
      this.visualizerFactory.create().visualize(pdf, os);
    } catch (IOException e) {
      throw new IceciteVisualizeException(
          "Couldn't open file '" + target + "'.", e);
    }
  }

  // ==========================================================================
  // Setters methods.

  /**
   * Sets the input file to process.
   * 
   * @param inputFile
   *        The input file to process.
   */
  public void setInputFile(Path inputFile) {
    this.inputFile = inputFile;
  }

  /**
   * Sets the path to the output file for the serialization.
   * 
   * @param serializationFile
   *        The path to the output file for the serialization.
   */
  public void setSerializationFile(Path serializationFile) {
    this.serializationFile = serializationFile;
  }

  /**
   * Sets the format to use on serialization.
   * 
   * @param serializationFormat
   *        The format to use on serialization.
   */
  public void setSerializationFormat(String serializationFormat) {
    this.serializationFormat = serializationFormat;
  }

  /**
   * Sets the path to the output file for the visualization.
   * 
   * @param visualizationFile
   *        The path to the output file for the visualization.
   */
  public void setVisualizationFile(Path visualizationFile) {
    this.visualizationFile = visualizationFile;
  }

  /**
   * Sets the features to extract.
   * 
   * @param features
   *        The list of features to extract.
   */
  public void setFeatures(Set<PdfFeature> features) {
    this.features = features;
  }

  /**
   * Sets the roles to consider.
   * 
   * @param roles
   *        The list of roles to extract.
   */
  public void setRoles(Set<PdfRole> roles) {
    this.roles = roles;
  }
  
  // ==========================================================================
  // Getter methods.

  /**
   * Returns the path to the input file.
   * 
   * @return The path to the input file.
   */
  public Path getInputFile() {
    return this.inputFile;
  }

  /**
   * Returns the path to the output file for the serialization.
   * 
   * @return The path to the output file for the serialization.
   */
  public Path getSerializationFile() {
    return this.serializationFile;
  }

  /**
   * Returns the format to use on the serialization.
   * 
   * @return The format to use on the serialization.
   */
  public String getSerializationFormat() {
    return this.serializationFormat;
  }

  /**
   * Returns the path to the output file for the visualization.
   * 
   * @return The path to the output file for the visualization.
   */
  public Path getVisualizationFile() {
    return this.visualizationFile;
  }

  /**
   * Returns the features to extract.
   * 
   * @return The list of features to extract.
   */
  public Set<PdfFeature> getFeatures() {
    return this.features;
  }

  /**
   * Returns the roles to consider.
   * 
   * @return The list of roles to consider.
   */
  public Set<PdfRole> getRoles() {
    return this.roles;
  }
}
