package pdfact;

import static pdfact.PdfActSettings.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Provider;

import pdfact.exception.PdfActException;
import pdfact.exception.PdfActSerializeException;
import pdfact.exception.PdfActValidateException;
import pdfact.exception.PdfActVisualizeException;
import pdfact.models.PdfDocument;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextUnit;
import pdfact.parse.PdfParser.PdfParserFactory;
import pdfact.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import pdfact.serialize.PdfActSerializationFormat;
import pdfact.serialize.PdfSerializer;
import pdfact.tokenize.PdfTextTokenizer.PdfTextTokenizerFactory;
import pdfact.tokenize.paragraphs.PdfParagraphTokenizer.PdfParagraphTokenizerFactory;
import pdfact.visualize.PdfVisualizer;
import pdfact.visualize.PdfVisualizer.PdfVisualizerFactory;

/**
 * The main entry point to PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfAct extends PdfActCore {
  /**
   * The available serializers, per serialization formats.
   */
  protected Map<PdfActSerializationFormat, Provider<PdfSerializer>> serializers;

  /**
   * The factory to create instances of {@link PdfVisualizer}.
   */
  protected PdfVisualizerFactory visualizerFactory;

  /**
   * The boolean flag to indicate if the PDF document should be serialized.
   */
  protected boolean isSerializationScheduled;
  
  /**
   * The serialization target, given as a file.
   */
  protected Path serializationFile;

  /**
   * The serialization target, given as a stream.
   */
  protected OutputStream serializationStream;

  /**
   * The serialization format.
   */
  protected PdfActSerializationFormat serializationFormat;

  /**
   * The boolean flag to indicate if the PDF document should be visualized.
   */
  protected boolean isVisualizationScheduled;
  
  /**
   * The visualization target, given as a file.
   */
  protected Path visualizationFile;

  /**
   * The visualization target, given as a stream.
   */
  protected OutputStream visualizationStream;

  /**
   * The text units to be included in serialization and visualization.
   */
  protected Set<PdfTextUnit> textUnitFilters;

  /**
   * The roles of text units to be included in serialization and visualization.
   */
  protected Set<PdfRole> roleFilters;

  // ==========================================================================

  /**
   * The default constructor.
   * 
   * @param parserFactory
   *        The factory to create instances of PdfParser.
   * @param tokenizerFactory
   *        The factory to create instances of PdfTextTokenizer.
   * @param semanticizerFactory
   *        The factory to create instances of PdfTextSemanticizer.
   * @param paragraphTokenizerFactory
   *        The factory to create instances of PdfParagraphTokenizer.
   * @param serializers
   *        The available serializers, per serialization formats.
   * @param visualizerFactory
   *        The factory to create instances of {@link PdfVisualizer}.
   */
  @Inject
  public PdfAct(
      PdfParserFactory parserFactory,
      PdfTextTokenizerFactory tokenizerFactory,
      PdfTextSemanticizerFactory semanticizerFactory,
      PdfParagraphTokenizerFactory paragraphTokenizerFactory,
      Map<PdfActSerializationFormat, Provider<PdfSerializer>> serializers,
      PdfVisualizerFactory visualizerFactory) {
    super(parserFactory, tokenizerFactory, semanticizerFactory,
        paragraphTokenizerFactory);
    this.serializers = serializers;
    this.visualizerFactory = visualizerFactory;
    this.serializationFormat = DEFAULT_SERIALIZATION_FORMAT;
    this.textUnitFilters = DEFAULT_TEXT_UNITS_TO_INCLUDE;
    this.roleFilters = DEFAULT_SEMANTIC_ROLES_TO_INCLUDE;
  }

  @Override
  public PdfDocument run() throws PdfActException {
    PdfDocument pdf = super.run();

    if (isSerializationScheduled()) {
      serialize(pdf);
    }
    
    if (isVisualizationScheduled()) {
      visualize(pdf);
    }

    return pdf;
  }

  // ==========================================================================
  
  /**
   * Serializes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * 
   * @throws PdfActException
   *         If the serialization has failed.
   */
  protected void serialize(PdfDocument pdf) throws PdfActException {
    LOG.info("Serializing...");

    // Check if either a serialization file or -stream is given.
    if (this.serializationFile == null && this.serializationStream == null) {
      throw new PdfActValidateException("No serialization target given.");
    }

    // Check if a (validated) serialization format is given.
    PdfActSerializationFormat format = this.serializationFormat;
    if (format == null) {
      throw new PdfActValidateException("No serialization format given.");
    }

    // Obtain the serializer to use.
    Provider<PdfSerializer> serializerProvider = this.serializers.get(format);
    if (serializerProvider == null) {
      throw new PdfActSerializeException(
          "Couldn't find a serializer for the format '" + format + "'.");
    }

    PdfSerializer serializer = serializerProvider.get();
    serializer.setTextUnits(this.textUnitFilters);
    serializer.setRoles(this.roleFilters);
    byte[] serialization = serializer.serialize(pdf);

    // If the serialization target is given as a stream, write to it.
    if (this.serializationStream != null) {
      try {
        this.serializationStream.write(serialization);
      } catch (IOException e) {
        throw new PdfActSerializeException(
            "Couldn't write to serialization target stream.", e);
      }
    }

    // If the serialization target is given as a file, open and write to it.
    if (this.serializationFile != null) {
      try (OutputStream os = Files.newOutputStream(this.serializationFile)) {
        os.write(serialization);
      } catch (IOException e) {
        throw new PdfActSerializeException(
            "Couldn't open file '" + this.serializationFile + "'.", e);
      }
    }
  }

  /**
   * Visualizes the given PDF document to the given target file.
   * 
   * @param pdf
   *        The PDF document to visualize.
   * 
   * @throws PdfActException
   *         If something went wrong on visualizing the PDF document.
   */
  protected void visualize(PdfDocument pdf) throws PdfActException {
    LOG.info("Visualizing...");

    // Check if either a visualization file or -stream is given.
    if (this.visualizationFile == null && this.visualizationStream == null) {
      throw new PdfActVisualizeException("No visualization target given.");
    }

    // Create the visualizer and visualize the PDF document.
    PdfVisualizer visualizer = this.visualizerFactory.create();

    // If the visualization target is given as a stream, write to it.
    if (this.visualizationStream != null) {
      try {
        visualizer.visualize(pdf, this.visualizationStream, this.textUnitFilters,
            this.roleFilters);
      } catch (IOException e) {
        throw new PdfActVisualizeException(
            "Couldn't write to visualization target stream.", e);
      }
    }

    // If the visualization target is given as a file, open and write to it.
    if (this.visualizationFile != null) {
      try (OutputStream os = Files.newOutputStream(this.visualizationFile)) {
        visualizer.visualize(pdf, os, this.textUnitFilters, this.roleFilters);
      } catch (IOException e) {
        throw new PdfActSerializeException(
            "Couldn't open file '" + this.visualizationFile + "'.", e);
      }
    }
  }

  // ==========================================================================
  
  /**
   * Checks if the PDF document should be serialized.
   * 
   * @return True, if the PDF document should be serialized, false otherwise.
   */
  protected boolean isSerializationScheduled() {
    return this.isSerializationScheduled;
  }
  
  /**
   * Returns the target file for the serialization.
   * 
   * @return The target file for the serialization.
   */
  public Path getSerializationTargetFile() {
    return this.serializationFile;
  }

  /**
   * Returns the target stream for the serialization.
   * 
   * @return The target stream for the serialization.
   */
  public OutputStream getSerializationTargetStream() {
    return this.serializationStream;
  }

  /**
   * Validates and sets the path to the output file for the serialization.
   * 
   * @param path
   *        The path to the output file for the serialization.
   * 
   * @throws PdfActException
   *         If the given path is not valid.
   */
  public void setSerializationTarget(String path) throws PdfActException {
    // Check if a path is given.
    if (path == null) {
      throw new PdfActValidateException("No serialization target given.");
    }

    // Check if the serialization file already exists.
    Path file = Paths.get(path);
    if (Files.exists(file)) {
      // Make sure that the existing serialization file is a regular file.
      if (!Files.isRegularFile(file)) {
        throw new PdfActValidateException(
            "The serialization file already exists, but is no regular file.");
      }

      // Make sure that the existing serialization file is writable.
      if (!Files.isWritable(file)) {
        throw new PdfActValidateException(
            "The serialization file already exists, but isn't writable.");
      }
    }
    this.serializationFile = file;
    this.isSerializationScheduled = true;
  }

  /**
   * Validates and sets the output stream for the serialization.
   * 
   * @param os
   *        The output stream for the serialization.
   * 
   * @throws PdfActException
   *         If the given path is not valid.
   */
  public void setSerializationTarget(OutputStream os) throws PdfActException {
    if (os == null) {
      throw new PdfActValidateException("The serialization target given.");
    }
    this.serializationStream = os;
    this.isSerializationScheduled = true;
  }

  // ==========================================================================

  /**
   * Returns the format to use on the serialization.
   * 
   * @return The format to use on the serialization.
   */
  public PdfActSerializationFormat getSerializationFormat() {
    return this.serializationFormat;
  }

  /**
   * Validates and sets the format to use on serialization.
   * 
   * @param format
   *        The format to use on serialization.
   * 
   * @throws PdfActException
   *         If the given format is not valid.
   */
  public void setSerializationFormat(String format) throws PdfActException {
    // Check if a format is given.
    if (format == null) {
      throw new PdfActValidateException("No serialization format given.");
    }

    // Check if the given format is valid.
    if (!this.serializers.containsKey(this.serializationFormat)) {
      throw new PdfActValidateException(
          "The serialization format '" + format + "' is not valid.");
    }
    this.serializationFormat = PdfActSerializationFormat
        .getSerializationFormat(format);
  }

  // ==========================================================================

  /**
   * Checks if the PDF document should be visualized.
   * 
   * @return True, if the PDF document should be visualized, false otherwise.
   */
  protected boolean isVisualizationScheduled() {
    return this.isVisualizationScheduled;
  }
  
  /**
   * Returns the target file for the visualization.
   * 
   * @return The target file for the visualization.
   */
  public Path getVisualizationTargetFile() {
    return this.visualizationFile;
  }

  /**
   * Returns the target stream for the visualization.
   * 
   * @return The target stream for the visualization.
   */
  public OutputStream getVisualizationTargetStream() {
    return this.visualizationStream;
  }

  /**
   * Validates and sets the output file for the visualization.
   * 
   * @param path
   *        The path to the output file for the visualization.
   *
   * @throws PdfActException
   *         If the given path is not valid.
   */
  public void setVisualizationTarget(String path) throws PdfActException {
    // Check if a path is given.
    if (path == null) {
      throw new PdfActValidateException("No visualization file given.");
    }

    // Check if the visualization file already exists.
    Path file = Paths.get(path);
    if (Files.exists(file)) {
      // Make sure that the existing visualization file is a regular file.
      if (!Files.isRegularFile(file)) {
        throw new PdfActValidateException(
            "The visualization file already exists, but is no regular file.");
      }

      // Make sure that the existing visualization file is writable.
      if (!Files.isWritable(file)) {
        throw new PdfActValidateException(
            "The visualization file already exists, but isn't writable.");
      }
    }
    this.visualizationFile = file;
    this.isVisualizationScheduled = true;
  }

  /**
   * Validates and sets the output stream for the visualization.
   * 
   * @param os
   *        The output stream for the visualization.
   *
   * @throws PdfActException
   *         If the given path is not valid.
   */
  public void setVisualizationTarget(OutputStream os) throws PdfActException {
    if (os == null) {
      throw new PdfActValidateException("No visualization target given.");
    }
    this.visualizationStream = os;
    this.isVisualizationScheduled = true;
  }

  // ==========================================================================

  /**
   * Returns the features to extract.
   * 
   * @return The list of features to extract.
   */
  public Set<PdfTextUnit> getFeatures() {
    return this.textUnitFilters;
  }

  /**
   * Validates and sets the features to extract.
   * 
   * @param features
   *        The names of features to extract.
   *
   * @throws PdfActException
   *         If at least one of the given features is not valid.
   */
  public void setTextUnitFilters(List<String> features) throws PdfActException {
    // Check if at least one feature is given.
    if (features == null || features.size() == 0) {
      throw new PdfActValidateException("No features given.");
    }

    // Check if all given features are valid.
    Set<PdfTextUnit> featureSet = new HashSet<>();
    for (String feature : features) {
      if (!PdfTextUnit.isValidFeature(feature)) {
        throw new PdfActValidateException(
            "The feature '" + feature + "' is not valid.");
      }
      featureSet.add(PdfTextUnit.getFeature(feature));
    }
    this.textUnitFilters = featureSet;
  }

  // ==========================================================================

  /**
   * Returns the roles to consider.
   * 
   * @return The list of roles to consider.
   */
  public Set<PdfRole> getRoles() {
    return this.roleFilters;
  }

  /**
   * Validates and sets the given roles to consider.
   * 
   * @param roles
   *        The names of roles to consider.
   *
   * @throws PdfActException
   *         If at least one of the given roles is not valid.
   */
  public void setRoleFilters(List<String> roles) throws PdfActException {
    // Check if at least one role is given.
    if (roles == null || roles.size() == 0) {
      throw new PdfActValidateException("No roles given.");
    }

    // Check if all given roles are valid.
    Set<PdfRole> roleSet = new HashSet<>();
    for (String r : roles) {
      if (!PdfRole.isValidRole(r)) {
        throw new PdfActValidateException("The role '" + r + "' isn't valid.");
      }
      roleSet.add(PdfRole.getRole(r));
    }
    this.roleFilters = roleSet;
  }
}