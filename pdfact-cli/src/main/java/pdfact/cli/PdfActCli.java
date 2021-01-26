package pdfact.cli;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.model.SerializationFormat;
import pdfact.cli.util.exception.PdfActParseCommandLineException;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * The command line interface of PdfAct.
 *
 * @author Claudius Korzen
 */
public class PdfActCli {
  /**
   * Starts this command line interface.
   *
   * @param args The command line arguments.
   */
  protected void start(String[] args) {
    int statusCode = 0;
    String errorMessage = null;
    Throwable cause = null;

    // Create the command line argument parser.
    PdfActCommandLineParser parser = new PdfActCommandLineParser();

    try {
      // Parse the command line arguments.
      parser.parseArgs(args);

      // Create an instance of PdfAct.
      PdfAct pdfAct = new PdfAct();

      // Pass the debugging flags.
      pdfAct.setDebugPdfParsing(parser.isDebugPdfParsing);
      pdfAct.setDebugCharacterExtraction(parser.isDebugCharExtraction);
      pdfAct.setDebugSplittingLigatures(parser.isDebugSplittingLigatures);
      pdfAct.setDebugMergingDiacritics(parser.isDebugMergingDiacritics);
      pdfAct.setDebugTextLineDetection(parser.isDebugLineDetection);
      pdfAct.setDebugWordDetection(parser.isDebugWordDetection);
      pdfAct.setDebugTextBlockDetection(parser.isDebugBlockDetection);
      pdfAct.setDebugRoleDetection(parser.isDebugRoleDetection);
      pdfAct.setDebugParagraphDetection(parser.isDebugParagraphDetection);
      pdfAct.setDebugWordDehyphenation(parser.isDebugWordDehyphenation);

      // Pass the serialization format if there is any.
      String serializationFormatStr = parser.serializationFormat;
      if (serializationFormatStr != null) {
        pdfAct.setSerializationFormat(SerializationFormat.fromString(serializationFormatStr));
      }

      // Pass the serialization target path.
      String serializationPathStr = parser.serializationPath;
      if (serializationPathStr != null) {
        pdfAct.setSerializationPath(Paths.get(serializationPathStr));
      } else {
        pdfAct.setSerializationStream(System.out);
      }

      // Pass the target of the visualization.
      String visualizationPathStr = parser.visualizationPath;
      if (visualizationPathStr != null) {
        pdfAct.setVisualizationPath(Paths.get(visualizationPathStr));
      }

      // Pass the chosen text unit.
      List<String> extractionUnits = parser.extractionUnits;
      if (extractionUnits != null) {
        pdfAct.setExtractionUnits(ExtractionUnit.fromStrings(extractionUnits));
      }

      // Compute the semantic roles to include on serialization & visualization.
      Set<String> roles = new HashSet<>();
      List<String> semanticRolesToInclude = parser.semanticRolesToInclude;
      if (semanticRolesToInclude != null) {
        roles.addAll(semanticRolesToInclude);
      }
      List<String> semanticRolesToExclude = parser.semanticRolesToExclude;
      if (semanticRolesToExclude != null) {
        roles.removeAll(semanticRolesToExclude);
      }
      pdfAct.setSemanticRoles(SemanticRole.fromStrings(roles));

      // Set the "with control characters"-flag.
      pdfAct.setInsertControlCharacters(parser.withControlCharacters); 

      // Run PdfAct.
      pdfAct.parse(parser.pdfPath);
    } catch (PdfActException e) {
      statusCode = e.getExitCode();
      errorMessage = e.getMessage();
      cause = e.getCause();
    }

    if (statusCode != 0) {
      // Print the error message (regardless of the log level).
      System.err.println(errorMessage);
      // Print the stack trace if there is any and debugging is enabled.
      if (cause != null && LogManager.getRootLogger().isDebugEnabled()) {
        cause.printStackTrace();
      }
    }

    System.exit(statusCode);
  }

  // ==============================================================================================

  /**
   * The main method to run the command line interface.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    new PdfActCli().start(args);
  }

  // ==============================================================================================

  /**
   * A parser to parse the command line arguments.
   *
   * @author Claudius Korzen
   */
  class PdfActCommandLineParser {
    /**
     * The command line argument parser.
     */
    protected ArgumentParser parser;

    // ============================================================================================

    /**
     * The name of the option to define the path to the PDF file to process.
     */
    public static final String PDF_PATH = "pdfPath";

    /**
     * The path to the PDF file to process.
     */
    @Arg(dest = PDF_PATH)
    public String pdfPath;

    // ============================================================================================

    /**
     * The name of the option to define the target path for the serialization.
     */
    public static final String SERIALIZE_PATH = "serializationPath";

    /**
     * The target path for the serialization.
     */
    @Arg(dest = SERIALIZE_PATH)
    public String serializationPath;

    // ============================================================================================

    /**
     * The name of the option to define the serialization format.
     */
    public static final String SERIALIZE_FORMAT = "format";

    /**
     * The serialization format.
     */
    @Arg(dest = SERIALIZE_FORMAT)
    public String serializationFormat = "txt";

    // ============================================================================================

    /**
     * The name of the option to define the target path for the visualization.
     */
    public static final String VISUALIZATION_PATH = "visualize";

    /**
     * The target path for the visualization.
     */
    @Arg(dest = VISUALIZATION_PATH)
    public String visualizationPath;

    // ============================================================================================

    /**
     * The name of the option to define the units to extract.
     */
    public static final String EXTRACTION_UNITS = "units";

    /**
     * The text unit to extract.
     */
    @Arg(dest = EXTRACTION_UNITS)
    public List<String> extractionUnits = Arrays.asList("paragraphs");

    // ============================================================================================

    /**
     * The name of the option to define the semantic roles to include (text blocks with a semantic
     * role that is not included won't be extracted).
     */
    public static final String INCLUDE_SEMANTIC_ROLES = "include-roles";

    /**
     * The semantic role(s) to include (text blocks with a semantic role that is not included won't
     * be extracted).
     */
    @Arg(dest = INCLUDE_SEMANTIC_ROLES)
    public List<String> semanticRolesToInclude = new ArrayList<>(SemanticRole.getNames());

    // ============================================================================================

    /**
     * The name of the option to define the semantic roles to exclude (text blocks with a semantic
     * role that is excluded won't be extracted).
     */
    public static final String EXCLUDE_SEMANTIC_ROLES = "exclude-roles";

    /**
     * The semantic role(s) to exclude (text blocks with a semantic role that is excluded won't be
     * extracted).
     */
    @Arg(dest = EXCLUDE_SEMANTIC_ROLES)
    public List<String> semanticRolesToExclude = new ArrayList<>();

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the PDF parsing step.
     */
    public static final String DEBUG_PDF_PARSING = "debug-pdf-parsing";

    /**
     * The boolean flag indicating whether or not to print debug info about the PDF parsing step.
     */
    @Arg(dest = DEBUG_PDF_PARSING)
    public boolean isDebugPdfParsing = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the extracted characters.
     */
    public static final String DEBUG_CHAR_EXTRACTION = "debug-character-extraction";

    /**
     * The boolean flag indicating whether or not to print debug info about the extracted chars.
     */
    @Arg(dest = DEBUG_CHAR_EXTRACTION)
    public boolean isDebugCharExtraction = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about splitting ligatures.
     */
    public static final String DEBUG_SPLITTING_LIGATURES = "debug-splitting-ligatures";

    /**
     * The boolean flag indicating whether or not to print debug info about splitting ligatures.
     */
    @Arg(dest = DEBUG_SPLITTING_LIGATURES)
    public boolean isDebugSplittingLigatures = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about merging diacritics.
     */
    public static final String DEBUG_MERGING_DIACRITICS = "debug-merging-diacritics";

    /**
     * The boolean flag indicating whether or not to print debug info about splitting ligatures.
     */
    @Arg(dest = DEBUG_MERGING_DIACRITICS)
    public boolean isDebugMergingDiacritics = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the text line detection.
     */
    public static final String DEBUG_LINE_DETECTION = "debug-text-line-detection";

    /**
     * The boolean flag indicating whether or not to print debug info about the text line detection.
     */
    @Arg(dest = DEBUG_LINE_DETECTION)
    public boolean isDebugLineDetection = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the word detection.
     */
    public static final String DEBUG_WORD_DETECTION = "debug-word-detection";

    /**
     * The boolean flag indicating whether or not to print debug info about the word detection.
     */
    @Arg(dest = DEBUG_WORD_DETECTION)
    public boolean isDebugWordDetection = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the text block detection.
     */
    public static final String DEBUG_BLOCK_DETECTION = "debug-text-block-detection";

    /**
     * The boolean flag indicating whether or not to print debug info about the text block
     * detection.
     */
    @Arg(dest = DEBUG_BLOCK_DETECTION)
    public boolean isDebugBlockDetection = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the semantic roles
     * detection.
     */
    public static final String DEBUG_ROLE_DETECTION = "debug-semantic-role-detection";

    /**
     * The boolean flag indicating whether or not to print debug info about the semantic roles
     * detection.
     */
    @Arg(dest = DEBUG_ROLE_DETECTION)
    public boolean isDebugRoleDetection = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the paragraphs detection.
     */
    public static final String DEBUG_PARAGRAPH_DETECTION = "debug-paragraph-detection";

    /**
     * The boolean flag indicating whether or not to print debug info about the paragraphs
     * detection.
     */
    @Arg(dest = DEBUG_PARAGRAPH_DETECTION)
    public boolean isDebugParagraphDetection = false;

    // ============================================================================================

    /**
     * The name of the option to enable the printing of debug info about the word dehyphenation.
     */
    public static final String DEBUG_WORD_DEHYPHENATION = "debug-word-dehyphenation";

    /**
     * The boolean flag indicating whether or not to print debug info about the word dehyphenation.
     */
    @Arg(dest = DEBUG_WORD_DEHYPHENATION)
    public boolean isDebugWordDehyphenation = false;

   // ============================================================================================

    /**
     * The name of the option to define the "with control characters" flag.
     */
    public static final String WITH_CONTROL_CHARACTERS = "with-control-characters";

    /**
     * The flag indicating whether or not to add control characters to the TXT serialization 
     * output.
     */
    @Arg(dest = WITH_CONTROL_CHARACTERS)
    public boolean withControlCharacters = false;


    // ============================================================================================

    /**
     * Creates a new command line argument parser.
     */
    public PdfActCommandLineParser() {
      this.parser = ArgumentParsers.newFor("pdfact")
        .terminalWidthDetection(false)
        .defaultFormatWidth(100).build();
      this.parser.description("A tool to extract the text, structure and layout from PDF files.");

      // Add an option to define the path to the PDF file to be processed.
      this.parser.addArgument(PDF_PATH).dest(PDF_PATH)
        .required(true)
        .metavar("<pdf-file>")
        .help("The path to the PDF file to be processed.");

      // Add an argument to define the target path to the output file.
      this.parser.addArgument(SERIALIZE_PATH).dest(SERIALIZE_PATH)
        .required(false).nargs("?")
        .metavar("<output-file>")
        .help("The path to the file to which the extraction output should be written.\n"
            + "If not specified, the output will be written to stdout.");

      // Add an option to define the output format.
      Set<String> choices = SerializationFormat.getNames();
      String choicesStr = String.join(", ", choices);
      String defaultStr = this.serializationFormat;
      this.parser.addArgument("--" + SERIALIZE_FORMAT).dest(SERIALIZE_FORMAT)
        .required(false)
        .metavar("<format>")
        .choices(choices)
        .setDefault(this.serializationFormat)
        .help("The output format.\n" 
            + "- Available options: " + choicesStr + ".\n" 
            + "- Default: \"" + defaultStr + "\".\n"
            + "In case of txt, the text elements will be extracted as plain text, in the "
            + "format: one text element per line. In case of xml or json, the text elements "
            + "will be extracted together with their layout information, e.g., their positions "
            + "in the PDF file, their fonts and their colors.");

      // Add an option to define the text units.
      choicesStr = String.join(", ", ExtractionUnit.getPluralNames());
      defaultStr = String.join(",", this.extractionUnits);
      this.parser.addArgument("--" + EXTRACTION_UNITS).dest(EXTRACTION_UNITS)
        .required(false)
        .metavar("<units>")
        .action(new SplitAtDelimiterAction(","))
        .setDefault(this.extractionUnits)
        .help("The granularity in which the elements should be extracted in the output, "
            + "separated by \",\".\n" 
            + "- Available options: " + choicesStr + ".\n" 
            + "- Default: \"" + defaultStr + "\".\n"
            + "For example, when the script is called with the option \"--" + EXTRACTION_UNITS
            + " words\", the output will be broken down by words, that is: the text and layout "
            + "information are provided word-wise.");

      // Add an option to define the semantic role(s) to include.
      choicesStr = String.join(", ", SemanticRole.getNames());
      defaultStr = String.join(",", this.semanticRolesToInclude);
      this.parser.addArgument("--" + INCLUDE_SEMANTIC_ROLES).dest(INCLUDE_SEMANTIC_ROLES)
        .required(false)
        .metavar("<roles>")
        .action(new SplitAtDelimiterAction(","))
        .setDefault(this.semanticRolesToInclude)
        .help("The list of the semantic roles to include, separated by \",\".\n"
            + "- Available options: " + choicesStr + ".\n"
            + "- Default: \"" + defaultStr + "\".\n"
            + "Only the elements with a semantic role that is included in this list will be "
            + "extracted. All other elements won't be extracted. For example, if the script is "
            + "called with the option \"--" + INCLUDE_SEMANTIC_ROLES + " headings,body\", the "
            + "output will only contain the text elements (and optionally, the layout "
            + "information) belonging to a heading or a body text paragraph. Per default, all "
            + "available semantic roles are included, that is: all elements will be extracted, "
            + "regardless of the semantic roles.\n"
            + "NOTE: The detection of the semantic roles of the text elements is still in an "
            + "experimental state. So don't expect the semantic roles to be highly accurate.");

      // Add an option to define the semantic role(s) to exclude.
      defaultStr = String.join(",", this.semanticRolesToExclude);
      this.parser.addArgument("--" + EXCLUDE_SEMANTIC_ROLES).dest(EXCLUDE_SEMANTIC_ROLES)
        .required(false)
        .metavar("<roles>")
        .action(new SplitAtDelimiterAction(","))
        .setDefault(this.semanticRolesToExclude)
        .help("The list of the semantic roles to exclude, separated by \",\".\n"
            + "- Available options: " + choicesStr + ".\n"
            + "- Default: \"" + defaultStr + "\".\n"
            + "All elements with a semantic role that is included in this list won't be "
            + "extracted. For example, if the script is called with the option \"--"
            + EXCLUDE_SEMANTIC_ROLES + " body\", the text (and layout information) belonging to "
            + "body text paragraphs won't be extracted. Per default, no semantic role is "
            + "excluded, that is: all elements will be extracted.\n"
            + "NOTE: The detection of the semantic roles of the text elements is still in an "
            + "experimental state. So don't expect the semantic roles to be highly accurate.");

      // Add an option to define the target path for the visualization.
      this.parser.addArgument("--" + VISUALIZATION_PATH).dest(VISUALIZATION_PATH)
        .required(false)
        .type(String.class)
        .metavar("<path>")
        .help("The path to a file (ending in *.pdf) to which a visualization of the extracted "
            + "elements (that is: the original PDF file enriched which bounding boxes around the "
            + "extracted elements and the semantic roles in case the unit is \"paragraph\") "
            + "should be written to. The file doesn't have to be existent before. If not "
            + "specified, no such visualization will be created.");

      // Add an option to define whether or not control characters (which identify headings and
      // page breaks should be inserted into the TXT serialization output.
      this.parser.addArgument("--" + WITH_CONTROL_CHARACTERS).dest(WITH_CONTROL_CHARACTERS)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.withControlCharacters)
        .help("Add the following control characters to the TXT serialization output:\n"
            + "- \"^L\" (\"form feed\") between two elements when a page break occurs between the "
            + "two elements in the PDF.\n"
            + "- \"^A\" (\"start of heading\") in front of headings.");

      // Add an option to enable the printing of debug info about the PDF parsing step.
      this.parser.addArgument("--" + DEBUG_PDF_PARSING).dest(DEBUG_PDF_PARSING)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugPdfParsing)
        .help("Print debug info about the PDF parsing step.");
      
      // Add an option to enable the printing of debug info about the characters extraction.
      this.parser.addArgument("--" + DEBUG_CHAR_EXTRACTION).dest(DEBUG_CHAR_EXTRACTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugCharExtraction)
        .help("Print debug info about the character extraction step.");

      // Add an option to enable the printing of debug info about splitting ligatures.
      this.parser.addArgument("--" + DEBUG_SPLITTING_LIGATURES).dest(DEBUG_SPLITTING_LIGATURES)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugSplittingLigatures)
        .help("Print debug info about the splitting ligatures step.");

      // Add an option to enable the printing of debug info about merging diacritics.
      this.parser.addArgument("--" + DEBUG_MERGING_DIACRITICS).dest(DEBUG_MERGING_DIACRITICS)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugMergingDiacritics)
        .help("Print debug info about the merging diacritics step.");

      // Add an option to enable the printing of debug info about the text line detection.
      this.parser.addArgument("--" + DEBUG_LINE_DETECTION).dest(DEBUG_LINE_DETECTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugLineDetection)
        .help("Print debug info about the text line detection step.");

      // Add an option to enable the printing of debug info about the word detection.
      this.parser.addArgument("--" + DEBUG_WORD_DETECTION).dest(DEBUG_WORD_DETECTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugWordDetection)
        .help("Print debug info about the word detection step.");

      // Add an option to enable the printing of debug info about the text block detection.
      this.parser.addArgument("--" + DEBUG_BLOCK_DETECTION).dest(DEBUG_BLOCK_DETECTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugBlockDetection)
        .help("Print debug info about the text block detection step.");

      // Add an option to enable the printing of debug info about the roles detection.
      this.parser.addArgument("--" + DEBUG_ROLE_DETECTION).dest(DEBUG_ROLE_DETECTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugRoleDetection)
        .help("Print debug info about the semantic roles detection step.");

      // Add an option to enable the printing of debug info about the paragraphs detection.
      this.parser.addArgument("--" + DEBUG_PARAGRAPH_DETECTION).dest(DEBUG_PARAGRAPH_DETECTION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugParagraphDetection)
        .help("Print debug info about the paragraphs detection step.");

      // Add an option to enable the printing of debug info about the word dehyphenation.
      this.parser.addArgument("--" + DEBUG_WORD_DEHYPHENATION).dest(DEBUG_WORD_DEHYPHENATION)
        .required(false)
        .action(Arguments.storeTrue())
        .setDefault(this.isDebugWordDehyphenation)
        .help("Print debug info about the word dehyphenation step.");
    }

    /**
     * Parses the given command line arguments.
     *
     * @param args The command line arguments to parse.
     *
     * @throws PdfActException If parsing the command line arguments fails.
     */
    public void parseArgs(String[] args) throws PdfActException {
      try {
        this.parser.parseArgs(args, this);
      } catch (HelpScreenException e) {
        // Set the status code to 0, such that no error message is shown.
        throw new PdfActParseCommandLineException(null, 0, e);
      } catch (ArgumentParserException e) {
        String message = e.getMessage() + "\n\n" + getUsage();
        throw new PdfActParseCommandLineException(message, e);
      }
    }

    /**
     * Returns the usage for this command line parser.
     *
     * @return The usage for this command line parser.
     */
    public String getUsage() {
      return this.parser.formatUsage();
    }

    /**
     * Returns the help for this command line parser.
     *
     * @return The help for this command line parser.
     */
    public String getHelp() {
      return this.parser.formatHelp();
    }
  }

  // ==============================================================================================

  /**
   * Argument action to split a given string at a given delimiter and to store a list of all
   * resulting substrings.
   */
  private static class SplitAtDelimiterAction implements ArgumentAction {
    /**
     * The delimiter to split at.
     */
    protected String delimiter;

    /**
     * Creates a new SplitAtDelimiterAction.
     *
     * @param delimiter The delimiter to split at.
     */
    public SplitAtDelimiterAction(String delimiter) {
      this.delimiter = delimiter;
    }

    @Override
    public void run(ArgumentParser parser, Argument arg, Map<String, Object> attrs, String flag,
            Object value) throws ArgumentParserException {
      if (value != null) {
        attrs.put(arg.getDest(), Arrays.asList(((String) value).split(delimiter)));
      }
    }

    @Override
    public void onAttach(Argument arg) {
    }

    @Override
    public boolean consumeArgument() {
      return true;
    }
  }
}
