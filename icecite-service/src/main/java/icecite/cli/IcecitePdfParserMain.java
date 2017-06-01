package icecite.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import icecite.guice.IceciteBaseModule;
import icecite.guice.IceciteServiceModule;
import icecite.models.PdfDocument;
import icecite.parse.PdfParser;
import icecite.parse.PdfParser.PdfParserFactory;
import icecite.parse.stream.pdfbox.guice.OperatorProcessorModule;
import icecite.semanticize.PdfTextSemanticizer;
import icecite.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import icecite.serializer.PdfSerializer;
import icecite.tokenize.PdfTextTokenizer;
import icecite.visualizer.PdfVisualizer;
import icecite.visualizer.PdfVisualizer.PdfVisualizerFactory;

// TODO: Write a usable main method.

/**
 * The main class to manage the Icecite parser from the command line.
 * 
 * @author Claudius Korzen
 */
public class IcecitePdfParserMain {
  /**
   * The input PDF file.
   */
  protected static Path inputPdf;

  /**
   * The output file for serialization.
   */
  protected static Path outputSerialization;

  /**
   * The output file for visualization.
   */
  protected static Path outputVisualization;

  /**
   * The parser.
   */
  protected static PdfParser pdfParser;

  /**
   * The main method.
   * 
   * @param args
   *        The command line arguments.
   * @throws ParseException
   *         If parsing the command line argument failed.
   * @throws IOException
   *         If processing the PDF file failed.
   */
  public static void main(String[] args) throws ParseException, IOException {
    if (args.length == 0) {
      throw new IllegalArgumentException("There is no PDF file given.");
    }

    Path inputPdf = Paths.get(args[0]).toAbsolutePath();

    // Check if the PDf file exists.
    if (!Files.isRegularFile(inputPdf)) {
      throw new IllegalArgumentException("The PDF file does not exist.");
    }

    // Check if the PDF file is readable.
    if (!Files.isReadable(inputPdf)) {
      throw new IllegalArgumentException("The PDF file can't be read.");
    }

    /*
     * Guice.createInjector() takes your Modules, and returns a new Injector
     * instance. Most applications will call this method exactly once, in their
     * main() method.
     */
    Injector injector = Guice.createInjector(new IceciteBaseModule(),
        new OperatorProcessorModule(), new IceciteServiceModule());

    // Create an instance of PdfParser.
    PdfParserFactory factory = injector.getInstance(PdfParserFactory.class);
    PdfVisualizerFactory visualizerFactory = injector
        .getInstance(PdfVisualizerFactory.class);
    PdfSerializer serializer = injector
        .getInstance(Key.get(PdfSerializer.class, Names.named("txt")));
    PdfParser pdfParser = factory.create();
    PdfTextTokenizer tokenizer = injector.getInstance(PdfTextTokenizer.class);
    PdfTextSemanticizerFactory semanticizerFactory = injector
        .getInstance(PdfTextSemanticizerFactory.class);

    PdfVisualizer visualizer = visualizerFactory.create();

    PdfDocument document = pdfParser.parsePdf(inputPdf);
    
    tokenizer.tokenizePdfDocument(document);
    
    PdfTextSemanticizer semanticizer = semanticizerFactory.create(document);
    semanticizer.semanticize();
    
    Path vis = Paths.get("/home/korzen/Downloads/zzz.pdf");
    try (OutputStream stream = Files.newOutputStream(vis)) {
      visualizer.visualize(document, stream);
    }

    Path vis2 = Paths.get("/home/korzen/Downloads/xxx.txt");
    try (OutputStream stream = Files.newOutputStream(vis2)) {
      serializer.serialize(document, stream);
    }
  }
}
