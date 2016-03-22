package cli;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import analyzer.PdfAnalyzer;
import analyzer.PlainPdfAnalyzer;
import de.freiburg.iif.path.FileAndDirectoryParser;
import de.freiburg.iif.path.PathUtils;
import model.PdfDocument;
import model.PdfFeature;
import model.PdfRole;
import parser.PdfExtendedParser;
import parser.PdfParser;
import parser.PdfXYCutParser;
import parser.pdfbox.PdfBoxParser;
import serializer.JsonPdfSerializer;
import serializer.XmlPdfSerializer;
import serializer.TsvPdfSerializer;
import serializer.TxtPdfSerializer;
import serializer.PdfSerializer;
import visualizer.PdfVisualizer;
import visualizer.PlainPdfVisualizer;

/**
 * The main class to start the pdf parser.
 * 
 * @author Claudius Korzen
 */
public class PdfParserCommandLine {
  /**
   * The name of the option to define the feature(s) to extract.
   */
  protected static final String OPTION_FEATURE = "feature";

  /**
   * The name of the option to define the roles(s) to extract.
   */
  protected static final String OPTION_ROLE = "role";
  
  /**
   * The name of the option to define the serialization format.
   */
  protected static final String OPTION_FORMAT = "format";

  /**
   * The name of the option to define the prefix to consider on input parsing.
   */
  protected static final String OPTION_PREFIX = "prefix";

  /**
   * The name of the option to define the suffix to consider on input parsing.
   */
  protected static final String OPTION_SUFFIX = "suffix";

  /**
   * The name of the option to define whether to parse the input recursively.
   */
  protected static final String OPTION_RECURSIVE = "recursive";

  /**
   * The name of the option to define whether to visualize the features.
   */
  protected static final String OPTION_VISUALIZE = "visualize";

  /**
   * The pdf parser to extract characters, figures and shapes.
   */
  protected PdfParser pdfParser;

  /**
   * The extended pdf parser to extract words, lines and paragraphs.
   */
  protected PdfExtendedParser extendedPdfParser;

  /**
   * The pdf analyzer to identify the roles of paragraphs.
   */
  protected PdfAnalyzer pdfAnalyzer;
  
  /**
   * The valid pdf serializers.
   */
  protected static Map<String, PdfSerializer> validPdfSerializers;

  /**
   * The selected serializer to serialize the extracted features.
   */
  protected PdfSerializer pdfSerializer;

  /**
   * The pdf visualizer to create a visualization of the extracted features.
   */
  protected PdfVisualizer pdfVisualizer;

  /**
   * The input as it is given by the command line.
   */
  protected Path input;

  /**
   * The directory of the input. Is equal to input, if input is a directory.
   * Otherwise, it's the parent directory of the input.
   */
  protected Path inputDirectory;

  /**
   * The output directory given by the command line.
   */
  protected Path outputDirectory;

  /**
   * The output file.
   */
  protected Path outputFile;
  
  /**
   * The prefix to consider on parsing input files given by the command line.
   */
  protected String prefix;

  /**
   * The suffix to consider on parsing input files given by the command line.
   */
  protected String suffix;

  /**
   * Flag that indicates if we have to parse the input recursively.
   */
  protected boolean parseInputRecursively;

  /**
   * The features to serialize given by the command line.
   */
  protected List<PdfFeature> features;

  /**
   * The roles to serialize given by the command line.
   */
  protected List<PdfRole> roles;
  
  /**
   * Flag that indicates if we have to visualize the features.
   */
  protected boolean createVisualization;

  // ___________________________________________________________________________

  /**
   * The main method.
   */
  public static void main(String[] args) throws Exception {
    try {
      new PdfParserCommandLine(args).process();
    } catch (IllegalArgumentException e) {
      System.err.println("An error occured: " + e.getMessage() + "\n");
      printUsage();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static {
    addSerializationFormatChoice(new TsvPdfSerializer());
    addSerializationFormatChoice(new XmlPdfSerializer());
    addSerializationFormatChoice(new TxtPdfSerializer());
    addSerializationFormatChoice(new JsonPdfSerializer());
  }
  
  /**
   * Creates a new instance of this program based on the given arguments.
   * 
   * @throws ParseException
   * @throws IOException
   */
  public PdfParserCommandLine(String[] args) throws ParseException,
      IOException {
    CommandLine cmd = new DefaultParser().parse(getOptions(), args);

    this.pdfParser = new PdfBoxParser();
    this.extendedPdfParser = new PdfXYCutParser();
    this.pdfAnalyzer = new PlainPdfAnalyzer();
    this.pdfVisualizer = new PlainPdfVisualizer();

    if (cmd.hasOption(OPTION_FEATURE)) {
      this.features = PdfFeature.fromNames(cmd.getOptionValues(OPTION_FEATURE));
    } else {
      this.features = PdfFeature.valuesAsList();
    }
    if (cmd.hasOption(OPTION_ROLE)) {
      this.roles = PdfRole.fromNames(cmd.getOptionValues(OPTION_ROLE));
    } else {
      this.roles = PdfRole.valuesAsList();
    }
    
    this.prefix = cmd.getOptionValue(OPTION_PREFIX, "");
    this.suffix = cmd.getOptionValue(OPTION_SUFFIX, ".pdf");
    this.parseInputRecursively = cmd.hasOption(OPTION_RECURSIVE);
    this.createVisualization = cmd.hasOption(OPTION_VISUALIZE);

    evaluateInput(cmd);
    evaluateOutput(cmd);

    evaluateSerializationFormat(cmd);
  }

  /**
   * Processes the given input files.
   */
  public void process() throws IOException {
    // Obtain the files to process.
    List<Path> filesToProcess = readInputFiles(this.input);
    
    if (filesToProcess != null && !filesToProcess.isEmpty()) {
      System.out.println(filesToProcess.size() + " files found.");
      
      for (Path file : filesToProcess) {
        processFile(file);
      }
    } else {
      throw new IllegalStateException("No files to process.");
    }
  }

  /**
   * Processes the given file.
   */
  protected void processFile(Path file) throws IOException {
    System.out.println("\nProcess " + file + ".");

    // Parse the given file.
    PdfDocument document = parse(file);
    
    // Analyze the document.
    analyze(document);
    
    // Serialize the document.
    serialize(document);

    if (this.createVisualization) {
      visualize(document);
    }
  }

  /**
   * Parses the given file.
   */
  protected PdfDocument parse(Path file) throws IOException {
    // Extract characters, figures and shapes.
    PdfDocument document = pdfParser.parse(file);
    // Extract words, lines and paragraphs.
    return extendedPdfParser.parse(document);
  }
  
  /**
   * Analyzes the given document.
   */
  protected void analyze(PdfDocument document) throws IOException {
    pdfAnalyzer.analyze(document);
  }

  /**
   * Serializes the given document.
   */
  protected void serialize(PdfDocument document) throws IOException {
    Path serializationFile = getSerializationTargetFile(document);

    System.out.println("Serialize to " + serializationFile + ".");

    if (serializationFile != null) {
      // Check, if we have to create the parent directory for the file.
      Path directory = serializationFile.getParent();

      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
      }

      pdfSerializer.setFeatures(this.features);
      pdfSerializer.setRoles(this.roles);
      
      OutputStream stream = Files.newOutputStream(serializationFile);
      pdfSerializer.serialize(document, stream);
      stream.close();

      return;
    }

    throw new IllegalStateException("There is no serialization file given");
  }

  /**
   * Visualizes the given document.
   */
  protected void visualize(PdfDocument document) throws IOException {
    Path visualizationFile = getVisualizationTargetFile(document);

    System.out.println("Visualize to " + visualizationFile + ".");

    if (visualizationFile != null) {
      // Check, if we have to create the parent directory for the file.
      Path directory = visualizationFile.getParent();

      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
      }

      OutputStream stream = Files.newOutputStream(visualizationFile);
      pdfVisualizer.visualize(document, this.features, stream);
      stream.close();

      return;
    }

    throw new IllegalStateException("There is no visualization file given");
  }

  // ___________________________________________________________________________
  
  /**
   * Parses the given input for input files.
   */
  protected List<Path> readInputFiles(Path input) throws IOException {
    if (input == null) {
      return null;
    }

    FileAndDirectoryParser inputParser = new FileAndDirectoryParser();

    if (this.prefix != null) {
      inputParser.setPrefix(this.prefix);
    }

    if (this.suffix != null) {
      inputParser.setSuffix(this.suffix);
    }

    inputParser.setScanRecursive(this.parseInputRecursively);

    return inputParser.parse(input);
  }
  
  // ___________________________________________________________________________
  
  /**
   * Returns the name for the output file of the given text document.
   */
  protected Path getVisualizationTargetFile(PdfDocument document) {
    if (this.outputFile != null) {
      String parent = this.outputFile.getParent().toAbsolutePath().toString();
      String filename = PathUtils.getBasename(this.outputFile) + "-v.pdf";
      return Paths.get(parent, filename);
    }
    
    Path targetDir = getTargetDirectory(document);
    if (targetDir != null) {
      Path inputFile = document.getPdfFile();
      if (inputFile != null) {
        String filename = inputFile.getFileName().toString();
        String targetName = filename + "-v.pdf";

        return targetDir.resolve(targetName);
      }
    }
    return null;
  }

  /**
   * Returns the name for the output file of the given text document.
   */
  protected Path getSerializationTargetFile(PdfDocument document) {
    if (this.outputFile != null) {
      return outputFile;
    }
    
    Path targetDir = getTargetDirectory(document);
    if (targetDir != null) {
      Path inputFile = document.getPdfFile();
      if (inputFile != null) {
        String filename = inputFile.getFileName().toString();
        String targetName = filename + "." + pdfSerializer.getOutputFormat();

        return targetDir.resolve(targetName);
      }
    }
    return null;
  }

  /**
   * Returns the target directory for the given document.
   */
  protected Path getTargetDirectory(PdfDocument document) {
    if (document != null) {
      Path inputFile = document.getPdfFile();
      if (inputFile != null) {
        Path inputParent = inputFile.getParent();
        Path relativeInputParent = inputDirectory.relativize(inputParent);

        return outputDirectory.resolve(relativeInputParent);
      }
    }
    return null;
  }

  // ___________________________________________________________________________
  // Evaluation methods.
  
  /**
   * Evaluates the given input file/directory.
   */
  protected void evaluateInput(CommandLine cmd) throws IOException {
    String[] args = cmd.getArgs();

    // Evaluate the input file/directory given by the command line.
    String inputStr = args.length > 0 ? args[0] : null;

    if (inputStr == null) {
      throw new IllegalArgumentException("There is no input given.");
    }

    this.input = Paths.get(inputStr);

    // Check if the input is readable.
    if (!Files.isReadable(this.input)) {
      throw new IllegalArgumentException("The input can't be read.");
    }

    // Obtain the input directory.
    if (Files.isRegularFile(this.input)) {
      this.inputDirectory = this.input.getParent();
    } else if (Files.isDirectory(this.input)) {
      this.inputDirectory = this.input;
    } else {
      throw new IllegalArgumentException("The input isn't a file or directory");
    }
  }

  /**
   * Evaluates the given input file/directory.
   */
  protected void evaluateOutput(CommandLine cmd) throws IOException {
    String[] args = cmd.getArgs();

    String outputStr = args.length > 1 ? args[1] : null;

    if (outputStr == null) {
      // No explicit output given, take the input directory.
      this.outputDirectory = this.inputDirectory;
    } else {
      Path output = Paths.get(outputStr);

      if (Files.isDirectory(output)) {
        // The output is a directory, everything ok.
        this.outputDirectory = output;
      } else if (Files.isRegularFile(output)) {
        if (Files.isDirectory(this.input)) {
          // The output is an existing file although input is a driectory.
          throw new IllegalArgumentException("The given output is a file.");
        } else if (!Files.isWritable(output)) {
          // The output isn't writable.
          throw new IllegalArgumentException("Can't write to output.");
        } else {
          this.outputFile = output;
        }
      } else if (!Files.exists(output)) {
        if (Files.isDirectory(this.input)) {
          // The output doesn't exist, try to create it.
          this.outputDirectory = Files.createDirectories(output);
        } else {
          this.outputFile = output;
        }
      }
    }
  }

  /**
   * Evaluates the given input file/directory.
   */
  protected void evaluateSerializationFormat(CommandLine cmd)
    throws IOException {
    String serializationFormat = cmd.getOptionValue(OPTION_FORMAT);

    // Obtain the selected pdf serializer.
    if (isValidSerializationFormat(serializationFormat)) {
      this.pdfSerializer = getPdfSerializer(serializationFormat);
    } else {
      this.pdfSerializer = getDefaultPdfSerializer();
    }

    if (this.pdfSerializer == null) {
      throw new IllegalStateException("There is no serializer given");
    }
  }

  /**
   * Adds a choice for a pdf serializer.
   */
  protected static void addSerializationFormatChoice(PdfSerializer serializer) {
    if (serializer != null) {
      if (validPdfSerializers == null) {
        validPdfSerializers = new LinkedHashMap<>();
      }
      validPdfSerializers.put(serializer.getOutputFormat(), serializer);
    }
  }

  /**
   * Returns true, if the given format is a valid serialization format.
   */
  protected static boolean isValidSerializationFormat(String format) {
    if (validPdfSerializers == null) {
      return false;
    }
    return validPdfSerializers.containsKey(format);
  }

  /**
   * Returns all valid serialization formats.
   */
  protected static List<String> getValidSerializationFormats() {
    if (validPdfSerializers == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(validPdfSerializers.keySet());
  }

  /**
   * Returns the serializer for the given format.
   */
  protected static PdfSerializer getPdfSerializer(String format) {
    return validPdfSerializers.get(format);
  }

  /**
   * Returns the default pdf serializer.
   */
  protected static PdfSerializer getDefaultPdfSerializer() {
    if (validPdfSerializers != null) {
      return validPdfSerializers.values().iterator().next();
    }
    return null;
  }
  
  // ___________________________________________________________________________
  // Helper utils.
  
  /**
   * Prints the usage to the given writer.
   */
  protected static void printUsage() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(
        "java -jar PdfParserCommandLine.jar [options] <input> [<output>]",
        "\nParses the given input (pdf file or a directory of pdf files) and "
            + "outputs the extraction results into the given output dir. If "
            + "the output isn't given, the result will be printed to stdout.",
        getOptions(), null);
  }

  /**
   * Returns the command line options.
   */
  protected static Options getOptions() {
    // Define the command line options.
    Options options = new Options();

    // Add option for format.
    options.addOption(Option.builder()
        .argName(OPTION_FORMAT)
        .longOpt(OPTION_FORMAT)
        .hasArg(true)
        .desc("The output format. Available: " + getValidSerializationFormats())
        .build());

    // Add option for the prefix.
    options.addOption(Option.builder()
        .argName(OPTION_PREFIX)
        .longOpt(OPTION_PREFIX)
        .hasArg(true)
        .desc("The prefix of files to consider on parsing the input.")
        .build());

    // Add option for the prefix.
    options.addOption(Option.builder()
        .argName(OPTION_SUFFIX)
        .longOpt(OPTION_SUFFIX)
        .hasArg(true)
        .desc("The suffix of files to consider on parsing the input.")
        .build());

    // Add option for the prefix.
    options.addOption(Option.builder()
        .argName(OPTION_RECURSIVE)
        .longOpt(OPTION_RECURSIVE)
        .desc("Parse the input recursively.")
        .build());

    // Add option for the features to extract.
    options.addOption(Option.builder()
        .argName(OPTION_FEATURE)
        .longOpt(OPTION_FEATURE)
        .hasArg(true)
        .desc("The features to extract. Available: " + PdfFeature.getNames())
        .build());

    // Add option for the roles to extract.
    options.addOption(Option.builder()
        .argName(OPTION_ROLE)
        .longOpt(OPTION_ROLE)
        .hasArg(true)
        .desc("The roles to extract. Available: " + PdfRole.getNames())
        .build());
    
    // Add option for the features to extract.
    options.addOption(Option.builder()
        .argName(OPTION_VISUALIZE)
        .longOpt(OPTION_VISUALIZE)
        .desc("Create visualization of the extracted features.")
        .build());

    return options;
  }
}
