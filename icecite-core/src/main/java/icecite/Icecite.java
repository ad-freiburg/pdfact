package icecite;

import java.nio.file.Path;

import icecite.models.PdfDocument;

// TODO: Validate the input file.
// TODO: Validate the output file.

/**
 * The central class of Icecite that can be used to handle and manage the
 * extraction process(es) from PDF file(s).
 * 
 * @author Claudius Korzen
 */
public class Icecite {
  /**
   * The path to the input file.
   */
  protected Path inputFile;

  /**
   * The path to the output file.
   */
  protected Path outputFile;

  /**
   * The output format.
   */
  protected String outputFormat;

  // ===========================================================================

  /**
   * The default constructor.
   */
  public Icecite() {

  }

  // ===========================================================================

  /**
   * Starts the extraction process.
   * 
   * @return The parsed PDF document.
   */
  public PdfDocument run() {
    System.out.println(this.inputFile + " " + this.outputFile + " "
        + getOutputFormat());
    return null;
  }

  // ===========================================================================
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
   * Sets the output file to process.
   * 
   * @param outputFile
   *        The output file to process.
   */
  public void setOutputFile(Path outputFile) {
    this.outputFile = outputFile;
  }

  /**
   * Sets the output format to use.
   * 
   * @param outputFormat
   *        The output format to use.
   */
  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }

  // ===========================================================================
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
   * Returns the output file.
   * 
   * @return The output file.
   */
  public Path getOutputFile() {
    return this.outputFile;
  }

  /**
   * Returns the output format.
   * 
   * @return The output format.
   */
  public String getOutputFormat() {
    return this.outputFormat;
  }

  // ===========================================================================

  // /**
  // * The input PDF file.
  // */
  // protected static Path inputPdf;
  //
  // /**
  // * The output file for serialization.
  // */
  // protected static Path outputSerialization;
  //
  // /**
  // * The output file for visualization.
  // */
  // protected static Path outputVisualization;
  //
  // /**
  // * The parser.
  // */
  // protected static PdfParser pdfParser;

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
