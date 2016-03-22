package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.freiburg.iif.path.PathUtils;
import drawer.PdfDrawer;
import model.PdfDocument;
import parser.PdfParser;
import parser.pdfbox.PdfBoxParser;
import serializer.JsonPdfSerializer;
import serializer.PdfSerializer;

/**
 * The main class to start the pdf parser.
 * 
 * @author Claudius Korzen
 */
public class PdfParserMain {
  /** 
   * The available parsers.
   */
  protected static Map<String, PdfParser> pdfParsers;

  /**
   * The available serializers.
   */
  protected static Map<String, PdfSerializer> pdfSerializers;
  
  /** 
   * The name of the default pdf parser.
   */
  protected static String defaultPdfParserName; 

  /** 
   * The name of the default pdf serializer.
   */
  protected static String defaultPdfSerializerName; 
      
  /**
   * The selected pdf parser.
   */
  protected PdfParser pdfParser;

  /**
   * The selected output formatter.
   */
  protected PdfSerializer pdfSerializer;
  
  /**
   * The visualizer.
   */
  protected PdfDrawer pdfVisualizer;
  
  /**
   * The path to the input file or directory.
   */
  protected String input;

  /**
   * The input file or directory.
   */
  protected Path inputFileOrDirectory;
  
  /**
   * The path to the output directory.
   */
  protected String output;
  
  /**
   * The output directory.
   */
  protected Path outputDirectory;
  
  /**
   * The stack which caches the files which still have to be parsed.
   */
  protected List<Path> pdfsToProcess;

  /**
   * The current number of files that were already parsed.
   */
  protected int numOfProcessedPdfs;
  
  /**
   * Flag that is true, if the program shouldn't write to console (for example, 
   * in case of tests).
   */
  protected boolean preventConsoleOutput;
  
  // ___________________________________________________________________________

  static {
    // Define the available output formatters.
    PdfSerializer[] outputFormatters = { 
//        new TsvPdfSerializer(),
//        new XmlPdfSerializer(),
//        new JsonPdfSerializer(),
        new JsonPdfSerializer()
    };
    pdfSerializers = new HashMap<>();
    for (PdfSerializer formatter : outputFormatters) {
      pdfSerializers.put(formatter.getOutputFormat(), formatter);
    }
    defaultPdfSerializerName = outputFormatters[0].getOutputFormat();
    
    // Define the available pdf parsers.
    PdfBoxParser[] parsers = { 
        new PdfBoxParser() 
    };
    pdfParsers = new HashMap<>();
    for (PdfParser parser : parsers) {
      pdfParsers.put(parser.getName(), parser);
    }
    defaultPdfParserName = parsers[0].getName();
  }
  
  /**
   * The default constructor.
   */
  public PdfParserMain(CommandLine cmd) {
    this.input = getInput(cmd);
    this.output = getOutput(cmd);
    this.pdfParser = getSelectedPdfParser(cmd);
    this.pdfSerializer = getSelectedPdfSerializer(cmd);
    this.pdfsToProcess = new ArrayList<Path>();
  }

  /**
   * The default constructor.
   */
  public PdfParserMain(PdfParser parser) {
    this.pdfParser = parser;
    this.pdfsToProcess = new ArrayList<Path>();
  }
  
  /**
   * Parses the given input files.
   */
  public List<PdfDocument> process() throws Exception {
    initialize();

    List<PdfDocument> textDocuments = new ArrayList<>();
    PdfDocument textDocument;
    while ((textDocument = parseNextPdf()) != null) {
      textDocuments.add(textDocument);
    }
    
    serialize(textDocuments);
    
    return textDocuments;
  }
   
  /**
   * Parses the next PDF from the batch of found PDFs and returns if there still
   * was one left.
   */
  protected PdfDocument parseNextPdf() throws Exception {
    if (!pdfsToProcess.isEmpty()) {
      numOfProcessedPdfs++;
      Path pdf = pdfsToProcess.remove(pdfsToProcess.size() - 1);
      PdfDocument document = pdfParser.parse(pdf);
            
      return document;
    }
    return null;
  }

  /**
   * Writes the given list of documents to output using the given serializer.
   */
  protected void serialize(List<PdfDocument> documents) throws IOException {
    if (documents != null) {
      for (PdfDocument document : documents) {
        Path outputDir = ensureOutputDirectory(document);
  
        if (outputDir != null) {
          Path outputPath = outputDir.resolve(getOutputFilename(document));
          OutputStream stream = Files.newOutputStream(outputPath);
          
          // Output the document to the output stream.
          serialize(document, stream);
          
          stream.close();
        } else if (!isPreventConsoleOutput()) {
          serialize(document, System.out);
        }
      }
    }
  }

  /**
   * Writes the given document to the given output stream.
   */
  protected void serialize(PdfDocument document, OutputStream stream)
      throws IOException {
    this.pdfSerializer.serialize(document, stream);
  }
  
  // ___________________________________________________________________________
  // Initialization methods.

  /**
   * Initializes this parser.
   */
  protected void initialize() throws IOException {
    this.pdfsToProcess.clear();
    this.numOfProcessedPdfs = 0;

    // Check, if there is a parser given.
    if (pdfParser == null) {
      throw new IllegalArgumentException("There is no parser given.");
    }

    // Check, if there is an output formatter given.
    if (pdfSerializer == null) {
      throw new IllegalArgumentException("There is no serializer given.");
    }
    
    // Check the given input.
    if (input == null) {
      throw new IllegalArgumentException("There is no input given.");
    } else {
      this.inputFileOrDirectory = Paths.get(input);
    }
    
    // Read the pdf files from given input.
    pdfsToProcess = PathUtils.listPaths(inputFileOrDirectory, "pdf");
    
    if (pdfsToProcess == null || pdfsToProcess.isEmpty()) {
      throw new IllegalArgumentException("No pdf files found.");
    }
    
    if (output != null) {
      this.outputDirectory = Paths.get(output);
      if (Files.isRegularFile(outputDirectory)) {
        throw new IllegalArgumentException("The output directory is a file");
      }
    }
  }

  /**
   * Tries to ensure that the output directory exists physically. Returns the 
   * output directory, if either the directory already exists or if it could 
   * be created successfully; null otherwise.
   */
  protected Path ensureOutputDirectory(PdfDocument document) 
      throws IOException {
    Path outputDirectory = null;
        
    if (document != null && output != null) {
      if (Files.isRegularFile(inputFileOrDirectory)) {
        outputDirectory = this.outputDirectory;
      }
            
      if (Files.isDirectory(inputFileOrDirectory)) {
        // Keep the directory structure from the input directory.
        Path inputFile = document.getPdfFile();
        if (inputFile != null) {
          Path inputParentPath = inputFile.getParent().toAbsolutePath(); 
          Path basePath = inputFileOrDirectory.toAbsolutePath();
          
          // Obtain the path of the file relative to the input path.
          if (inputParentPath.startsWith(basePath)) {
            Path relInputPath = basePath.relativize(inputParentPath);
            // Create the relative path in the output directory.
            outputDirectory = this.outputDirectory.resolve(relInputPath);
          }
        }
      }
    }
    
    if (outputDirectory != null) {
      return Files.createDirectories(outputDirectory);
    }
    return null;
  }
  
  /**
   * Returns the name for the output file of the given text document.
   */
  protected String getOutputFilename(PdfDocument document) {    
    if (document != null) {
      Path inputFile = document.getPdfFile();
      if (inputFile != null) {
        String inputName = inputFile.getFileName().toString();
        return inputName + "." + pdfSerializer.getOutputFormat();
      }
    }
    return null;
  }
  
  // ___________________________________________________________________________
  // Getters and and setters.

  /**
   * Returns the input, which could be a file or a directory.
   */
  public String getInput() {
    return input;
  }

  /**
   * Sets the input, which could be a file or a directory.
   */
  public void setInput(String input) {
    this.input = input;
  }

  /**
   * Returns the output, which could be a file or a directory.
   */
  public String getOutput() {
    return output;
  }

  /**
   * Sets the output, which could be a file or a directory.
   */
  public void setOutput(String output) {
    this.output = output;
  }

  /**
   * Returns a collection of names of all valid pdf parsers.
   */
  public static Collection<String> getAvailablePdfParserNames() {
    return pdfParsers.keySet();
  }
  
  /**
   * Returns the valid output formats.
   */
  public static Collection<String> getAvailablePdfSerializerNames() {
    return pdfSerializers.keySet();
  }

  /**
   * Returns the selected output formatter.
   */
  public PdfSerializer getOutputFormatter() {
    return pdfSerializer;
  }

  /**
   * Sets the output formatter.
   */
  public void setOutputFormatter(PdfSerializer outputFormatter) {
    this.pdfSerializer = outputFormatter;
  }

  /**
   * Set the preventConsoleOutput flag.
   */
  public void setPreventConsoleOutput(boolean prevent) {
    this.preventConsoleOutput = prevent;
  }
  
  /**
   * Returns the preventConsoleOutput flag.
   */
  public boolean isPreventConsoleOutput() {
    return preventConsoleOutput;
  }
  
  /**
   * Extracts the input argument from the given command line.
   */
  protected String getInput(CommandLine cmd) {
    if (cmd != null) {
      String[] args = cmd.getArgs();
      if (args != null && args.length > 0) {
        return args[0];
      }
    }
    throw new IllegalArgumentException(getUsage());
  }
  
  /**
   * Extracts the output argument from the given command line.
   */
  protected static String getOutput(CommandLine cmd) {
    if (cmd != null) {
      String[] args = cmd.getArgs();
      if (args != null && args.length > 1) {
        return args[1];
      }
    }
    return null;
  }
  
  /**
   * Extracts the selected pdf parser from the given command line.
   */
  protected static PdfParser getSelectedPdfParser(CommandLine cmd) {
    if (cmd != null) {
      String pdfParser = cmd.getOptionValue("p", defaultPdfParserName);
      return pdfParsers.get(pdfParser); 
    }
    return null; 
  }
  
  /**
   * Extracts the selected pdf serializer from the given command line.
   */
  protected static PdfSerializer getSelectedPdfSerializer(CommandLine cmd) {
    if (cmd != null) {
      String pdfSerializer = cmd.getOptionValue("f", defaultPdfSerializerName);
      return pdfSerializers.get(pdfSerializer); 
    }
    return null; 
  }
  
  // ___________________________________________________________________________
  // The main method.

  /**
   * The main method.
   */
  public static void main(String[] args) throws Exception {
    // Parse the command line.
    CommandLine cmd = parseCommandLine(args);
    
    // Start the program.
    new PdfParserMain(cmd).process();     
  }
  
  /**
   * Parses the given command line arguments.
   */
  protected static CommandLine parseCommandLine(String[] args) {
    CommandLineParser cmdParser = new DefaultParser();
    try {
      return cmdParser.parse(getOptions(), args);
    } catch (ParseException e) {
      throw new IllegalArgumentException(getUsage());
    }
  }
  
  /**
   * Returns the usage as string. 
   */
  protected static String getUsage() {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(stream);
    printUsage(writer, getOptions());
    writer.close();
    try {
      stream.close();
    } catch (Exception e) {
      // TODO: Something to do?
    }
    return stream.toString();
  }
  
  /**
   * Prints the usage to stdout.
   */
  protected static void printUsage(Options options) {
    PrintWriter writer = new PrintWriter(System.out);
    printUsage(writer, options);
    writer.close();
  }
  
  /**
   * Prints the usage to the given writer.
   */
  protected static void printUsage(PrintWriter writer, Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(writer, 80, 
        "java PdfParserMain [options] <input> [<output>]",
        "Parses the given input (pdf file or a directory of pdf files) and "
            + "outputs the extraction results into the given output dir. If "
            + "the output isn't given, the result will be printed to stdout. \n"
            + "Options: ", options, 0, 0, null);
    
  }
  
  /**
   * Returns the command line options.
   */
  protected static Options getOptions() {
    // Define the command line options.
    Options options = new Options();
    options.addOption("f", "format", true, "The output format. \n"
        + "Available formats are: " + getAvailablePdfSerializerNames());
    options.addOption("p", "parser", true, "The pdf parser to use. \n"
        + "Available pdf parsers are: " + getAvailablePdfParserNames());
    return options;
  }
}
