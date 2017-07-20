package icecite;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import icecite.exception.IceciteException;
import icecite.exception.IceciteSerializeException;
import icecite.exception.IceciteValidateException;
import icecite.exception.IceciteVisualizeException;
import icecite.models.PdfDocument;
import icecite.parse.PdfParser.PdfParserFactory;
import icecite.serialize.PdfSerializer;
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
   * The factory to create instances of PdfParser.
   */
  protected PdfParserFactory parserFactory;

  /**
   * The factory to create instances of PdfSerializer.
   */
  protected Map<String, Provider<PdfSerializer>> serializers;

  /**
   * The factory to create instances of PdfVisualizer.
   */
  protected PdfVisualizerFactory visualizerFactory;
  
  // ==========================================================================

  /**
   * The default constructor.
   * 
   * @param parserFactory
   *        The factory to create instances of PdfParser.
   * @param serializerBindings
   *        The map of available serializers.
   * @param visualizerFactory
   *        The factory to create instances of PdfVisualizer.
   */
  @Inject
  public Icecite(PdfParserFactory parserFactory,
      Map<String, Provider<PdfSerializer>> serializerBindings,
      PdfVisualizerFactory visualizerFactory) {
    this.parserFactory = parserFactory;
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
      this.serializers.get(this.serializationFormat).get().serialize(pdf, os);
    } catch (IOException e) {
      throw new IceciteSerializeException("Couldn't open file '" + target + "'.", e);
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
      throw new IceciteVisualizeException("Couldn't open file '" + target + "'.", e);
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

  // ==========================================================================

  // /**
  // * The main method.
  // *
  // * @param args
  // * The command line arguments.
  // * @throws ParseException
  // * If parsing the command line argument failed.
  // * @throws IOException
  // * If processing the PDF file failed.
  // */
  // public static void main(String[] args) throws ParseException, IOException
  // {
  // if (args.length == 0) {
  // throw new IllegalArgumentException("There is no PDF file given.");
  // }
  //
  // Path inputPdf = Paths.get(args[0]).toAbsolutePath();
  //
  // // Check if the PDf file exists.
  // if (!Files.isRegularFile(inputPdf)) {
  // throw new IllegalArgumentException("The PDF file does not exist.");
  // }
  //
  // // Check if the PDF file is readable.
  // if (!Files.isReadable(inputPdf)) {
  // throw new IllegalArgumentException("The PDF file can't be read.");
  // }
  //
  // /*
  // * Guice.createInjector() takes your Modules, and returns a new Injector
  // * instance. Most applications will call this method exactly once, in their
  // * main() method.
  // */
  // Injector injector = Guice.createInjector(new IceciteCoreModule(),
  // new OperatorProcessorModule(), new IceciteServiceModule());
  //
  // // Create an instance of PdfParser.
  // PdfParserFactory factory = injector.getInstance(PdfParserFactory.class);
  // PdfVisualizerFactory visualizerFactory = injector
  // .getInstance(PdfVisualizerFactory.class);
  // PdfSerializer serializer = injector
  // .getInstance(Key.get(PdfSerializer.class, Names.named("txt")));
  // PdfParser pdfParser = factory.create();
  // PdfPageTokenizer tokenizer = injector.getInstance(PdfPageTokenizer.class);
  // PdfDocumentTokenizer tokenizer2 =
  // injector.getInstance(PdfDocumentTokenizer.class);
  // PdfTextSemanticizerFactory semanticizerFactory = injector
  // .getInstance(PdfTextSemanticizerFactory.class);
  //
  // PdfVisualizer visualizer = visualizerFactory.create();
  //
  // PdfDocument document = pdfParser.parsePdf(inputPdf);
  //
  // tokenizer.tokenizePdfPages(document);
  //
  // PdfTextSemanticizer semanticizer = semanticizerFactory.create(document);
  // semanticizer.semanticize();
  //
  // tokenizer2.tokenizePdfDocument(document);
  //
  // Path vis = Paths.get("/home/korzen/Downloads/zzz.pdf");
  // try (OutputStream stream = Files.newOutputStream(vis)) {
  // visualizer.visualize(document, stream);
  // }
  //
  // Path vis2 = Paths.get("/home/korzen/Downloads/xxx.txt");
  // try (OutputStream stream = Files.newOutputStream(vis2)) {
  // serializer.serialize(document, stream);
  // }
  //
  // for (PdfParagraph para : document.getParagraphs()) {
  // System.out.println(para.getText() + "\n");
  // }
  // }
}
