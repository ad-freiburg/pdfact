package pdfact.cli;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import pdfact.cli.model.SerializeFormat;
import pdfact.cli.model.TextUnit;
import pdfact.cli.util.exception.PdfActParseCommandLineException;
import pdfact.core.PdfActCoreSettings;
import pdfact.core.model.LogLevel;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

/**
 * The main class for the command line interface of PdfAct.
 *
 * @author Claudius Korzen
 */
public class PdfActCLI {
  /**
   * Starts the command line interface of PdfAct.
   *
   * @param args
   *     The command line arguments.
   */
  protected void run(String[] args) {
    int statusCode = 0;
    String errorMessage = null;
    Throwable cause = null;

    // Create the command line argument parser.
    PdfActCommandLineParser parser = new PdfActCommandLineParser();

    // Create an instance of PdfAct.
    PdfAct pdfAct = new PdfAct();

    try {
      // Parse the command line arguments.
      parser.parseArgs(args);

      // Pass the log level.
      pdfAct.setLogLevel(LogLevel.getLogLevel(parser.getLogLevel()));

      // Pass the serialization format if there is any.
      if (parser.hasSerializationFormat()) {
        String format = parser.getSerializeFormat();
        pdfAct.setSerializationFormat(SerializeFormat.fromString(format));
      }

      // Pass the target of the serialization.
      if (parser.hasSerializationPath()) {
        pdfAct.setSerializationPath(Paths.get(parser.getSerializationPath()));
      } else {
        pdfAct.setSerializationStream(System.out);
      }

      // Pass the target of the visualization.
      if (parser.hasVisualizationPath()) {
        pdfAct.setVisualizationPath(Paths.get(parser.getVisualizationPath()));
      }

      // Pass the chosen text unit.
      if (parser.hasTextUnit()) {
        pdfAct.setTextUnit(TextUnit.fromString(parser.getTextUnit()));
      }

      // Pass the semantic roles filter for serialization & visualization.
      if (parser.hasSemanticRolesFilters()) {
        List<String> roles = parser.getSemanticRolesFilters();
        pdfAct.setSemanticRoles(SemanticRole.fromStrings(roles));
      }

      // Run PdfAct.
      pdfAct.parse(parser.getPdfPath());
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
   * The main method that runs the command line interface.
   *
   * @param args
   *     The command line arguments.
   */
  public static void main(String[] args) {
    new PdfActCLI().run(args);
  }

  // ==============================================================================================

  /**
   * A parser that parses the command line arguments for the CLI of PdfAct.
   *
   * @author Claudius Korzen
   */
  class PdfActCommandLineParser {
    /**
     * The command line argument parser.
     */
    protected ArgumentParser parser;

    // ==============================================================================================

    /**
     * The name of the option to define the path to the PDF file to process.
     */
    protected static final String PDF_PATH = "pdfPath";

    /**
     * The path to the PDF file to process.
     */
    @Arg(dest = PDF_PATH)
    protected String pdfPath;

    // ==============================================================================================

    /**
     * The name of the option to define the target path for the serialization.
     */
    protected static final String SERIALIZE_PATH = "serializationPath";

    /**
     * The target path for the serialization.
     */
    @Arg(dest = SERIALIZE_PATH)
    protected String serializePath;

    // ==============================================================================================

    /**
     * The name of the option to define the serialization format.
     */
    protected static final String SERIALIZE_FORMAT = "format";

    /**
     * The serialization format.
     */
    @Arg(dest = SERIALIZE_FORMAT)
    protected String serializeFormat;

    // ==============================================================================================

    /**
     * The name of the option to define the target path for the visualization.
     */
    protected static final String VISUALIZATION_PATH = "visualize";

    /**
     * The target path for the visualization.
     */
    @Arg(dest = VISUALIZATION_PATH)
    protected String visualizationPath;

    // ==============================================================================================

    /**
     * The name of the option to define the text unit to extract.
     */
    protected static final String TEXT_UNIT = "unit";

    /**
     * The text unit to extract.
     */
    @Arg(dest = TEXT_UNIT)
    protected String textUnit;

    // ==============================================================================================

    /**
     * The name of the option to define the semantic role(s) filters.
     */
    protected static final String SEMANTIC_ROLES_FILTER = "role";

    /**
     * The semantic role(s) filters.
     */
    @Arg(dest = SEMANTIC_ROLES_FILTER)
    protected List<String> semanticRolesFilters;

    // ==============================================================================================

    /**
     * The name of the option to enable log output.
     */
    protected static final String LOG_LEVEL = "debug";

    /**
     * The log level.
     */
    @Arg(dest = LOG_LEVEL)
    protected int logLevel;

    // ==============================================================================================

    /**
     * Creates a new command line argument parser.
     */
    public PdfActCommandLineParser() {
      this.parser = ArgumentParsers.newFor("pdfact")
        .terminalWidthDetection(false)
        .defaultFormatWidth(100)
        .build();
      this.parser.description("Pdfact is a tool for extracting the text and "
        + "structure from PDF files.");

      // Add an argument to define the path to the PDF file to be processed.
      this.parser.addArgument(PDF_PATH)
          .dest(PDF_PATH)
          .required(true)
          .metavar("<pdf-file>")
          .help("The path to the PDF file to be processed.");

      // Add an argument to define the target path to the output file.
      this.parser.addArgument(SERIALIZE_PATH)
          .dest(SERIALIZE_PATH)
          .nargs("?")
          .metavar("<output-file>")
          .help("The path to the file to which the extraction output should be "
              + "written.\nIf not specified, the output will be written to "
              + "stdout.");
              
      // Add an argument to define the output format.
      Set<String> formatChoices = SerializeFormat.getNames();
      this.parser.addArgument("--" + SERIALIZE_FORMAT)
          .dest(SERIALIZE_FORMAT)
          .required(false)
          .choices(formatChoices)
          .metavar("<format>")
          .help("The output format. Available options: " + formatChoices + ".\n"
              + "If the format is \"txt\", the output will contain the text "
              + "matching the specified --" + TEXT_UNIT + " and --" 
              + SEMANTIC_ROLES_FILTER + " options in plain text format (in the "
              + "format: one text element per line). If the format is \"xml\" "
              + "or \"json\", the output will also contain layout information "
              + "for each text element, e.g., the positions in the PDF file.");

      // Add an argument to define the text unit to extract.
      Set<String> unitChoices = TextUnit.getPluralNames();
      this.parser.addArgument("--" + TEXT_UNIT)
          .dest(TEXT_UNIT)
          .choices(unitChoices)
          .required(false)
          .metavar("<unit>")
          .help("The granularity in which the extracted text (and the layout "
              + "information, if the chosen output format is dedicated to "
              + "provide such information) should be broken down in the "
              + "output. Available options: " + unitChoices + ".\n"
              + "For example, when the script is called with the option "
              + "\"--" + TEXT_UNIT + " words\", the output will be broken down "
              + "by words, that is: the text (and layout information) will be "
              + "provided word-wise.");
              

      // Add an argument to define the semantic role(s).
      Set<String> semanticRolesChoices = SemanticRole.getNames();
      this.parser.addArgument("--" + SEMANTIC_ROLES_FILTER)
          .dest(SEMANTIC_ROLES_FILTER)
          .nargs("*")
          .choices(semanticRolesChoices)
          .required(false)
          .metavar("<role>", "<role>")
          .help("The semantic role(s) of the text elements to be extracted. "
              + "Available options: " + semanticRolesChoices + ".\n"
              + "For example, if the script is called with the option \"--" 
              + SEMANTIC_ROLES_FILTER + " headings\", the output will only "
              + "contain the text (and optionally, the layout information) of "
              + "the text belonging to a heading. If not specified, all text "
              + "will be extracted, regardless of the semantic roles.");

      // Add an argument to define the target path for the visualization.
      this.parser.addArgument("--" + VISUALIZATION_PATH)
          .dest(VISUALIZATION_PATH)
          .required(false)
          .metavar("<path>")
          .help("The path to the file to which a visualization of the "
              + "extracted text (that is: the original PDF file enriched "
              + "which bounding boxes around the extracted text elements) "
              + "should be written to. If not specified, no such "
              + "visualization will be created.");

      // Add an argument to define the log level.
      StringBuilder choiceStr = new StringBuilder();
      int i = 0;
      choiceStr.append("[");
      for (LogLevel level : LogLevel.getLogLevels()) {
        choiceStr.append(level.getIntLevel() + " (= " + level + ")");
        choiceStr.append(i < LogLevel.getLogLevels().size() - 1 ? ", " : "");
        i++;
      }
      choiceStr.append("]");

      LogLevel debugLevel = LogLevel.DEBUG;
      this.parser.addArgument("--" + LOG_LEVEL)
          .dest(LOG_LEVEL)
          .nargs("?")
          .metavar("<level>")
          .type(Integer.class)
          .setDefault(PdfActCoreSettings.DEFAULT_LOG_LEVEL.getIntLevel())
          .action(new StoreDefaultArgumentAction(debugLevel.getIntLevel()))
          .help("The verbosity of the log messages. Available options:  "
              + choiceStr.toString() + ".\n"
              + "The level defines the minimum level of severity required for "
              + "a message to be logged.");
    }

    /**
     * Parses the given command line arguments.
     *
     * @param args
     *     The command line arguments to parse.
     *
     * @throws PdfActException
     *     If parsing the command line arguments fails.
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

    // ==============================================================================================
    // Getters methods.

    /**
     * Returns true, if a path to a PDF file is given; false otherwise.
     *
     * @return True, if a path to a PDF file is given; false otherwise.
     */
    public boolean hasPdfPath() {
      return this.pdfPath != null;
    }

    /**
     * Returns the path to the PDF file.
     *
     * @return The path to the PDF file.
     */
    public String getPdfPath() {
      return this.pdfPath;
    }

    // ==============================================================================================

    /**
     * Returns true, if a target path for the serialization is given; false
     * otherwise.
     *
     * @return True, if a target path for the serialization is given; false
     *     otherwise.
     */
    public boolean hasSerializationPath() {
      return this.serializePath != null;
    }

    /**
     * Returns the target path for the serialization.
     *
     * @return The target path for the serialization.
     */
    public String getSerializationPath() {
      return this.serializePath;
    }

    // ==============================================================================================

    /**
     * Returns true, if an serialization format is given.
     *
     * @return True, if an serialization format is given.
     */
    public boolean hasSerializationFormat() {
      return this.serializeFormat != null;
    }

    /**
     * Returns the serialization format.
     *
     * @return The serialization format.
     */
    public String getSerializeFormat() {
      return this.serializeFormat;
    }

    // ==============================================================================================

    /**
     * Returns true, if a target path for the visualization is given.
     *
     * @return True, if a target path for the visualization is given.
     */
    public boolean hasVisualizationPath() {
      return this.visualizationPath != null;
    }

    /**
     * Returns the target path for the visualization.
     *
     * @return The target path for the visualization.
     */
    public String getVisualizationPath() {
      return this.visualizationPath;
    }

    // ==============================================================================================

    /**
     * Returns true, if there is a text unit given.
     *
     * @return True, if there is a text unit given.
     *     False otherwise.
     */
    public boolean hasTextUnit() {
      return this.textUnit != null;
    }

    /**
     * Returns the text unit.
     *
     * @return The text unit.
     */
    public String getTextUnit() {
      return this.textUnit;
    }

    // ==============================================================================================

    /**
     * Returns true, if there is at least one semantic role filter given.
     *
     * @return True, if there is at least one semantic role filter given; False
     *     otherwise.
     */
    public boolean hasSemanticRolesFilters() {
      return this.semanticRolesFilters != null;
    }

    /**
     * Returns the semantic role(s) filter(s).
     *
     * @return The semantic role(s) filter(s).
     */
    public List<String> getSemanticRolesFilters() {
      return this.semanticRolesFilters;
    }

    // ==============================================================================================

    /**
     * Returns the log level.
     *
     * @return The log level.
     */
    public int getLogLevel() {
      return this.logLevel;
    }
  }

  /**
   * Argument action to store argument value or a given default value if the
   * argument value is null.
   */
  private static class StoreDefaultArgumentAction implements ArgumentAction {
    /**
     * The default value to store if the argument value is null.
     */
    protected Object defaultValue;

    /**
     * Creates a new StoreDefaultArgumentAction.
     *
     * @param defaultValue
     *     The default value to store if the argument value is null.
     */
    public StoreDefaultArgumentAction(Object defaultValue) {
      this.defaultValue = defaultValue;
    }

    @Override
    public void run(ArgumentParser parser, Argument arg,
        Map<String, Object> attrs, String flag, Object value)
        throws ArgumentParserException {
      if (value == null) {
        attrs.put(arg.getDest(), this.defaultValue);
      } else {
        attrs.put(arg.getDest(), value);
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
