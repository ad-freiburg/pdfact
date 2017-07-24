package icecite;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import icecite.exception.IceciteException;
import icecite.exception.IceciteSerializeException;
import icecite.exception.IceciteValidateException;
import icecite.exception.IceciteVisualizeException;
import icecite.join.PdfTextJoiner.PdfTextJoinerFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfFeature;
import icecite.models.PdfRole;
import icecite.parse.PdfParser.PdfParserFactory;
import icecite.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import icecite.serialize.PdfSerializer;
import icecite.tokenize.PdfTextTokenizer.PdfTextTokenizerFactory;
import icecite.visualize.PdfVisualizer;
import icecite.visualize.PdfVisualizer.PdfVisualizerFactory;

/**
 * The main entry point of Icecite. This class wires up all necessary steps in
 * order to handle the extraction process(es) from PDF file(s).
 * 
 * @author Claudius Korzen
 */
public class Icecite {
  /**
   * The factory to create instances of PdfParser.
   */
  protected PdfParserFactory parserFactory;

  /**
   * The factory to create instances of PdfTokenizer.
   */
  protected PdfTextTokenizerFactory tokenizerFactory;

  /**
   * The factory to create instances of PdfTextSemanticizer.
   */
  protected PdfTextSemanticizerFactory semanticizerFactory;

  /**
   * The factory to create instances of PdfTextJoiner.
   */
  protected PdfTextJoinerFactory textJoinerFactory;

  /**
   * The available classes of type PdfSerializer, per output format.
   */
  protected Map<String, Provider<PdfSerializer>> serializers;

  /**
   * The factory to create instances of PdfVisualizer.
   */
  protected PdfVisualizerFactory visualizerFactory;

  /**
   * The logger.
   */
  protected static Logger LOG = Logger.getLogger(Icecite.class);
  
  // ==========================================================================
  // The input arguments defined by the user.

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
   * The serialization format.
   */
  protected String serializationFormat = "xml";

  /**
   * The features to extract.
   */
  protected Set<PdfFeature> features;

  /**
   * The roles to consider on extraction.
   */
  protected Set<PdfRole> roles;

  // ==========================================================================

  /**
   * The default constructor.
   * 
   * @param parserFactory
   *        The factory to create instances of PdfParser.
   * @param tokenizerFactory
   *        The factory to create instances of PdfTokenizer.
   * @param semanticizerFactory
   *        The factory to create instances of PdfTextSemanticizer.
   * @param textJoinerFactory
   *        The factory to create instances of PdfTextJoinerFactory.
   * @param serializers
   *        The map of all available serializers.
   * @param visualizerFactory
   *        The factory to create instances of PdfVisualizer.
   */
  @Inject
  public Icecite(PdfParserFactory parserFactory,
      PdfTextTokenizerFactory tokenizerFactory,
      PdfTextSemanticizerFactory semanticizerFactory,
      PdfTextJoinerFactory textJoinerFactory,
      Map<String, Provider<PdfSerializer>> serializers,
      PdfVisualizerFactory visualizerFactory) {
    this.parserFactory = parserFactory;
    this.tokenizerFactory = tokenizerFactory;
    this.semanticizerFactory = semanticizerFactory;
    this.textJoinerFactory = textJoinerFactory;
    this.serializers = serializers;
    this.visualizerFactory = visualizerFactory;
  }

  /**
   * Starts the extraction process.
   * 
   * @return The parsed PDF document.
   * 
   * @throws IceciteException
   *         If something went wrong on the extraction process.
   */
  public PdfDocument run() throws IceciteException {
    long start = System.currentTimeMillis();
    LOG.info("Welcome to Icecite.");
    LOG.info("--------------------------------------------------------------");
    LOG.info("Arguments:");
    LOG.info("--------------------------------------------------------------");
    LOG.info("Input: " + this.inputFile);
    if (this.serializationFile != null) {
      LOG.info("Serialization target: " + this.serializationFile);
      LOG.info("Serialization format: " + this.serializationFormat);
    }
    if (this.visualizationFile != null) {
      LOG.info("Visualization target: " + this.visualizationFile);
    }
    LOG.info("Features: " + (this.features != null ? this.features : "none"));
    LOG.info("Roles: " + (this.roles != null ? this.roles : "none"));
    LOG.info("--------------------------------------------------------------");
    LOG.info("Progress:");
    LOG.info("--------------------------------------------------------------");
    
    // Parse the PDF "as it is".
    PdfDocument pdf = parse();
    // Tokenize the PDF into words, lines and text blocks.
    tokenize(pdf);
    // Identify the semantics of text blocks.
    semanticize(pdf);
    // Join the text blocks to paragraphs.
    join(pdf);
    // Serialize the PDF document to file.
    serialize(pdf);
    // Visualize the PDF document.
    visualize(pdf);
    
    long end = System.currentTimeMillis();
    LOG.info("Finished in " + (end - start) + " ms.");
    return pdf;
  }

  // ==========================================================================

  /**
   * Parses the given input PDF file.
   *
   * @return The parsed PDF document.
   * 
   * @throws IceciteException
   *         If something went wrong on parsing the input file.
   */
  protected PdfDocument parse() throws IceciteException {
    LOG.info("Parsing the PDF...");
    
    // Check if a (validated) input path is given.
    if (this.inputFile == null) {
      throw new IceciteValidateException("No input file given.");
    }
    return this.parserFactory.create().parsePdf(this.inputFile);
  }

  /**
   * Tokenizes the given PDF document into words, lines and text blocks.
   * 
   * @param pdf The PDF document to process.
   * 
   * @throws IceciteException
   *         If something went wrong on tokenizing the PDF.
   */
  protected void tokenize(PdfDocument pdf) throws IceciteException {
    LOG.info("Identifying words, text lines and text blocks...");
    this.tokenizerFactory.create().tokenize(pdf);
  }
  
  /**
   * Identifies the semantic of text blocks in the given PDF document.
   * 
   * @param pdf The PDF document to process.
   * 
   * @throws IceciteException
   *         If something went wrong on identifying the semantics.
   */
  protected void semanticize(PdfDocument pdf) throws IceciteException {
    LOG.info("Identifying the semantics of the text blocks...");
    this.semanticizerFactory.create(pdf).semanticize();
  }
  
  /**
   * Joins the text blocks in the given PDF document to paragraphs.
   * 
   * @param pdf The PDF document to process.
   * 
   * @throws IceciteException
   *         If something went wrong on joining the text blocks.
   */
  protected void join(PdfDocument pdf) throws IceciteException {
    LOG.info("Identifying the text paragraphs...");
    this.textJoinerFactory.create().join(pdf);
  }
  
  /**
   * Serializes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to serialize.
   *
   * @throws IceciteException
   *         If something went wrong on serializing the PDF document.
   */
  protected void serialize(PdfDocument pdf) throws IceciteException {
    // Check if a (validated) input path is given.
    Path file = this.serializationFile;
    if (file == null) {
      // Don't throw an exception, because serialization is optional.
      return;
    }

    LOG.info("Serializing...");
    // Check if a (validated) serialization format is given.
    String format = this.serializationFormat;
    if (format == null) {
      throw new IceciteValidateException("No serialization format given.");
    }

    // Obtain the serializer to use.
    Provider<PdfSerializer> serializerProvider = this.serializers.get(format);
    if (serializerProvider == null) {
      throw new IceciteSerializeException(
          "Couldn't find a serializer for the format '" + format + "'.");
    }

    // Serialize the PDF document.    
    try (OutputStream os = Files.newOutputStream(file)) {
      serializerProvider.get().serialize(pdf, os, this.features, this.roles);
    } catch (IOException e) {
      throw new IceciteSerializeException(
          "Couldn't open file '" + file.toAbsolutePath() + "'.", e);
    }
  }

  /**
   * Visualizes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to visualize.
   * 
   * @throws IceciteException
   *         If something went wrong on visualizing the PDF document.
   */
  protected void visualize(PdfDocument pdf) throws IceciteException {
    // Check if a (validated) input path is given.
    Path file = this.visualizationFile;
    if (file == null) {
      // Don't throw an exception, because visualization is optional.
      return;
    }
    
    LOG.info("Visualizing...");
    
    // Create the visualizer and visualize the PDF document.
    PdfVisualizer visualizer = this.visualizerFactory.create();
    try (OutputStream os = Files.newOutputStream(file)) {
      visualizer.visualize(pdf, os, this.features, this.roles);
    } catch (IOException e) {
      throw new IceciteVisualizeException(
          "Couldn't open file '" + file.toAbsolutePath() + "'.", e);
    }
  }

  // ==========================================================================
  // Setters methods.

  /**
   * Validates and sets the path to the input file to process.
   * 
   * @param path
   *        The path to the input file to process.
   * 
   * @throws IceciteException
   *         If the given path is not valid.
   */
  public void setInputFilePath(String path) throws IceciteException {
    // Check if a path is given.
    if (path == null) {
      throw new IceciteValidateException("No input file given.");
    }

    Path file = Paths.get(path).toAbsolutePath();
    // Check if the file exists.
    if (!Files.exists(file)) {
      throw new IceciteValidateException(
          "The input file '" + path + "' does not exist.");
    }

    // Check if the file exists.
    if (!Files.isRegularFile(file)) {
      throw new IceciteValidateException(
          "The input file '" + path + "' is not a regular file.");
    }

    // Check if the file exists.
    if (!Files.isReadable(file)) {
      throw new IceciteValidateException(
          "The input file '" + path + "' can't be read.");
    }

    this.inputFile = file;
  }

  /**
   * Validates and sets the path to the output file for the serialization.
   * 
   * @param path
   *        The path to the output file for the serialization.
   * 
   * @throws IceciteException
   *         If the given path is not valid.
   */
  public void setSerializationFilePath(String path) throws IceciteException {
    // Check if a path is given.
    if (path == null) {
      throw new IceciteValidateException("No serialization file given.");
    }

    // Check if the serialization file already exists.
    Path file = Paths.get(path);
    if (Files.exists(file)) {
      // Make sure that the existing serialization file is a regular file.
      if (!Files.isRegularFile(file)) {
        throw new IceciteValidateException(
            "The serialization file already exists, but is no regular file.");
      }

      // Make sure that the existing serialization file is writable.
      if (!Files.isWritable(file)) {
        throw new IceciteValidateException(
            "The serialization file already exists, but isn't writable.");
      }
    }
    this.serializationFile = file;
  }

  /**
   * Validates and sets the format to use on serialization.
   * 
   * @param format
   *        The format to use on serialization.
   * 
   * @throws IceciteException
   *         If the given format is not valid.
   */
  public void setSerializationFormat(String format) throws IceciteException {
    // Check if a format is given.
    if (format == null) {
      throw new IceciteValidateException("No serialization format given.");
    }

    // Check if the given format is valid.
    if (!this.serializers.containsKey(this.serializationFormat)) {
      throw new IceciteValidateException(
          "The serialization format '" + format + "' is not valid.");
    }
    this.serializationFormat = format;
  }

  /**
   * Validates and sets the output file for the visualization.
   * 
   * @param path
   *        The path to the output file for the visualization.
   *
   * @throws IceciteException
   *         If the given path is not valid.
   */
  public void setVisualizationFilePath(String path) throws IceciteException {
    // Check if a path is given.
    if (path == null) {
      throw new IceciteValidateException("No visualization file given.");
    }

    // Check if the visualization file already exists.
    Path file = Paths.get(path);
    if (Files.exists(file)) {
      // Make sure that the existing visualization file is a regular file.
      if (!Files.isRegularFile(file)) {
        throw new IceciteValidateException(
            "The visualization file already exists, but is no regular file.");
      }

      // Make sure that the existing visualization file is writable.
      if (!Files.isWritable(file)) {
        throw new IceciteValidateException(
            "The visualization file already exists, but isn't writable.");
      }
    }
    this.visualizationFile = file;
  }

  /**
   * Validates and sets the features to extract.
   * 
   * @param features
   *        The names of features to extract.
   *
   * @throws IceciteException
   *         If at least one of the given features is not valid.
   */
  public void setFeatures(String[] features) throws IceciteException {
    // Check if at least one feature is given.
    if (features == null || features.length == 0) {
      throw new IceciteValidateException("No features given.");
    }

    // Check if all given features are valid.
    Set<PdfFeature> featureSet = new HashSet<>();
    for (String feature : features) {
      if (!PdfFeature.isValidFeature(feature)) {
        throw new IceciteValidateException(
            "The feature '" + feature + "' is not valid.");
      }
      featureSet.add(PdfFeature.fromName(feature));
    }
    this.features = featureSet;
  }

  /**
   * Validates and sets the given roles to consider.
   * 
   * @param roles
   *        The names of roles to consider.
   *
   * @throws IceciteException
   *         If at least one of the given roles is not valid.
   */
  public void setRoles(String[] roles) throws IceciteException {
    // Check if at least one role is given.
    if (roles == null || roles.length == 0) {
      throw new IceciteValidateException("No roles given.");
    }

    // Check if all given roles are valid.
    Set<PdfRole> roleSet = new HashSet<>();
    for (String role : roles) {
      if (!PdfFeature.isValidFeature(role)) {
        throw new IceciteValidateException(
            "The role '" + role + "' is not valid.");
      }
      roleSet.add(PdfRole.fromName(role));
    }
    this.roles = roleSet;
  }

  // ==========================================================================
  // Getter methods.

  /**
   * Returns the input file.
   * 
   * @return The input file.
   */
  public Path getInputFile() {
    return this.inputFile;
  }

  /**
   * Returns the output file for the serialization.
   * 
   * @return The output file for the serialization.
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
   * Returns the output file for the visualization.
   * 
   * @return The output file for the visualization.
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
