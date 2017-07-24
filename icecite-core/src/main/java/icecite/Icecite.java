package icecite;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
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
import icecite.visualize.PdfVisualizer;
import icecite.visualize.PdfVisualizer.PdfVisualizerFactory;

// TODO: Log output.

/**
 * The main entry point of Icecite. This class wires up all necessary steps 
 * in order to handle the extraction process(es) from PDF file(s).
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
   * The available classes of type PdfSerializer, per output format.
   */
  protected Map<String, Provider<PdfSerializer>> serializers;

  /**
   * The factory to create instances of PdfVisualizer.
   */
  protected PdfVisualizerFactory visualizerFactory;

  // ==========================================================================
  // The input arguments defined by the user.

  /**
   * The path to the input file.
   */
  protected Path inputFilePath;

  /**
   * The path to the output file for the serialization.
   */
  protected Path serializationFilePath;

  /**
   * The path to the output file for the visualization.
   */
  protected Path visualizationFilePath;

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
   * @param pageTokenizer
   *        The page tokenizer.
   * @param semanticizerFactory
   *        The factory to create instances of PdfTextSemanticizer.
   * @param documentTokenizer
   *        The document tokenizer.
   * @param serializers
   *        The map of all available serializers.
   * @param visualizerFactory
   *        The factory to create instances of PdfVisualizer.
   */
  @Inject
  public Icecite(PdfParserFactory parserFactory, PdfPageTokenizer pageTokenizer,
      PdfTextSemanticizerFactory semanticizerFactory,
      PdfDocumentTokenizer documentTokenizer,
      Map<String, Provider<PdfSerializer>> serializers,
      PdfVisualizerFactory visualizerFactory) {
    this.parserFactory = parserFactory;
    this.pageTokenizer = pageTokenizer;
    this.semanticizerFactory = semanticizerFactory;
    this.documentTokenizer = documentTokenizer;
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
    PdfDocument pdf = parse();
    serialize(pdf);
    visualize(pdf);
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
    // Check if a (validated) input path is given.
    if (this.inputFilePath == null) {
      throw new IceciteValidateException("No input file given.");
    }
    return this.parserFactory.create().parsePdf(this.inputFilePath);
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
    Path file = this.serializationFilePath;
    if (file == null) {
      // Abort, but don't throw an exception, because serialization is optional.
      return;
    }
    
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
    Path file = this.visualizationFilePath;
    if (file == null) {
      // Abort, but don't throw an exception, because visualization is optional.
      return;
    }
    
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
   * Sets the path to the input file to process.
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

    this.inputFilePath = file;
  }

  /**
   * Sets the path to the output file for the serialization.
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

    this.serializationFilePath = file;
  }

  /**
   * Sets the format to use on serialization.
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
   * Sets the path to the output file for the visualization.
   * 
   * @param path
   *        The path to the output file for the visualization.
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
    this.visualizationFilePath = file;
  }

  /**
   * Sets the features to extract.
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
   * Sets the roles to consider.
   * 
   * @param roles
   *        The names of roles to consider.
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
   * Returns the path to the input file.
   * 
   * @return The path to the input file.
   */
  public Path getInputFilePath() {
    return this.inputFilePath;
  }

  /**
   * Returns the path to the output file for the serialization.
   * 
   * @return The path to the output file for the serialization.
   */
  public Path getSerializationFilePath() {
    return this.serializationFilePath;
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
  public Path getVisualizationFilePath() {
    return this.visualizationFilePath;
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
