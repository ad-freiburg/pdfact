package pdfact.cli;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.internal.HelpScreenException;
import pdfact.PdfAct;
import pdfact.PdfActCoreSettings;
import pdfact.exception.PdfActException;
import pdfact.exception.PdfActParseCommandLineException;
import pdfact.guice.PdfActCoreModule;
import pdfact.guice.PdfActServiceModule;
import pdfact.log.PdfActLogLevel;
import pdfact.model.PdfSerializationFormat;
import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;
import pdfact.parse.stream.pdfbox.guice.OperatorProcessorModule;

/**
 * A command line interface for PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCommandLineInterface {
  /**
   * The main instance of PdfAct.
   */
  protected PdfAct pdfAct;

  /**
   * Creates a new command line interface for PdfAct.
   */
  public PdfActCommandLineInterface() {
    // TODO: Avoid to inject all needed modules here.
    Injector injector = Guice.createInjector(
        new PdfActCoreModule(),
        new OperatorProcessorModule(),
        new PdfActServiceModule());
    this.pdfAct = injector.getInstance(PdfAct.class);
  }

  /**
   * Runs the command line interface for PdfAct.
   * 
   * @param args
   *        The command line arguments.
   */
  protected void run(String[] args) {
    int statusCode = 0;
    String errorMessage = null;
    Throwable errorCause = null;

    // Create the command line argument parser.
    PdfActCommandLineParser parser = new PdfActCommandLineParser();

    try {
      // Parse the command line arguments.
      parser.parseArgs(args);

      // Pass the arguments to PdfAct.
      // Pass the log level.
      this.pdfAct.setLogLevel(parser.getLogLevel());

      // Pass the path to the input file.
      this.pdfAct.setInputFilePath(parser.getInputFilePath());

      // Pass the path to the serialization file.
      if (parser.hasOutputFilePath()) {
        this.pdfAct.setSerializationTarget(parser.getOutputFilePath());
      } else {
        this.pdfAct.setSerializationTarget(System.out);
      }

      // Pass the serialization format if there is any.
      if (parser.hasOutputFormat()) {
        this.pdfAct.setSerializationFormat(parser.getOutputFormat());
      }

      // Pass the path to the visualization file if there is any.
      if (parser.hasVisualizationFilePath()) {
        this.pdfAct.setVisualizationTarget(parser.getVisualizationFilePath());
      }

      // Pass the element types filter for serialization & visualization.
      if (parser.hasElementTypesFilter()) {
        this.pdfAct.setElementTypesFilter(parser.getElementTypesFilter());
      }

      // Pass the semantic roles filter for serialization & visualization.
      if (parser.hasSemanticRolesFilter()) {
        this.pdfAct.setSemanticRolesFilter(parser.getSemanticRolesFilter());
      }

      // Run PdfAct.
      this.pdfAct.run();
    } catch (PdfActException e) {
      statusCode = e.getExitCode();
      errorMessage = e.getMessage();
      errorCause = e.getCause();
    }

    if (statusCode != 0) {
      // Print the error message (regardless of the log level).
      System.err.println(errorMessage);
      // Print the stack trace if there is any and debugging is enabled.
      if (errorCause != null && this.pdfAct.hasLogLevel(PdfActLogLevel.DEBUG)) {
        errorCause.printStackTrace();
      }
    }

    System.exit(statusCode);
  }

  // ==========================================================================

  /**
   * The main method that runs the command line interface.
   * 
   * @param args
   *        The command line arguments.
   */
  public static void main(String[] args) {
    new PdfActCommandLineInterface().run(args);
  }

  // ==========================================================================

  /**
   * A parser that parses the command line arguments in order to run PdfAct.
   * 
   * @author Claudius Korzen
   */
  class PdfActCommandLineParser {
    /**
     * The command line argument parser.
     */
    protected ArgumentParser parser;

    // ========================================================================

    /**
     * The name of the option to define the path to the PDF file to process.
     */
    protected static final String OPTION_PDF_FILE_PATH = "inputFile";

    /**
     * The path to the PDF file to process.
     */
    @Arg(dest = OPTION_PDF_FILE_PATH)
    protected String inputFilePath;

    // ========================================================================

    /**
     * The name of the option to define the path to the output file.
     */
    protected static final String OPTION_OUTPUT_FILE_PATH = "outputFile";

    /**
     * The path to the output file.
     */
    @Arg(dest = OPTION_OUTPUT_FILE_PATH)
    protected String outputFilePath;

    // ========================================================================

    /**
     * The name of the option to define the output format.
     */
    protected static final String OPTION_OUTPUT_FORMAT = "format";

    /**
     * The output format.
     */
    @Arg(dest = OPTION_OUTPUT_FORMAT)
    protected String outputFormat;

    // ========================================================================

    /**
     * The name of the option to define the path to the visualization file.
     */
    protected static final String OPTION_VISUALIZATION_FILE_PATH = "visualize";

    /**
     * The output format.
     */
    @Arg(dest = OPTION_VISUALIZATION_FILE_PATH)
    protected String visualizationFilePath;

    // ========================================================================

    /**
     * The name of the option to define the element types to be included in the
     * output.
     */
    protected static final String OPTION_ELEMENT_TYPES_FILTER = "type";

    /**
     * The element type(s) to be included in the output.
     */
    @Arg(dest = OPTION_ELEMENT_TYPES_FILTER)
    protected List<String> elementTypesFilter;

    // ========================================================================

    /**
     * The name of the option to define the semantic role(s) to consider on
     * generating the output.
     */
    protected static final String OPTION_SEMANTIC_ROLES_FILTER = "role";

    /**
     * The semantic role(s) to consider.
     */
    @Arg(dest = OPTION_SEMANTIC_ROLES_FILTER)
    protected List<String> roles;

    // ========================================================================

    /**
     * The name of the option to enable log output.
     */
    protected static final String OPTION_LOG_LEVEL = "debug";

    /**
     * The log level.
     */
    @Arg(dest = OPTION_LOG_LEVEL)
    protected int logLevel;

    // ========================================================================

    /**
     * Creates a new command line argument parser.
     */
    public PdfActCommandLineParser() {
      this.parser = ArgumentParsers.newArgumentParser("pdfact");
      this.parser.description("pdfact extracts text from PDF files.");

      // Add an argument to define the path to the PDF file to process.
      this.parser.addArgument(OPTION_PDF_FILE_PATH)
          .dest(OPTION_PDF_FILE_PATH)
          .required(true)
          .metavar("<pdf-file>")
          .help("Defines the path to the PDF file to process.");

      // Add an argument to define the path to the output file.
      this.parser.addArgument(OPTION_OUTPUT_FILE_PATH)
          .dest(OPTION_OUTPUT_FILE_PATH)
          .nargs("?")
          .metavar("<output-file>")
          .help("Defines the path to the file where pdfact should write the "
              + "text output. If not specified, the output will be written "
              + "to stdout.");

      // Add an argument to define the output format.
      Set<String> formatChoices = PdfSerializationFormat.getNames();
      this.parser.addArgument("--" + OPTION_OUTPUT_FORMAT)
          .dest(OPTION_OUTPUT_FORMAT)
          .required(false)
          .choices(formatChoices)
          .metavar("<format>")
          .help("Defines the format in which the text output should be "
              + "written. Choose from: " + formatChoices + ".");

      // Add an argument to define the element types filter.
      Set<String> elementTypesChoices = PdfElementType.getGroupNames();
      this.parser.addArgument("--" + OPTION_ELEMENT_TYPES_FILTER)
          .dest(OPTION_ELEMENT_TYPES_FILTER)
          .nargs("*")
          .choices(elementTypesChoices)
          .required(false)
          .metavar("<type>", "<type>")
          .help("Defines one or more element types to be included in the text "
              + "output (and visualization if the --"
              + OPTION_VISUALIZATION_FILE_PATH + " option is given). "
              + "Choose from:" + formatChoices + ".");

      // Add an argument to define the semantic role(s) to extract.
      Set<String> roleChoices = PdfRole.getNames();
      this.parser.addArgument("--" + OPTION_SEMANTIC_ROLES_FILTER)
          .dest(OPTION_SEMANTIC_ROLES_FILTER)
          .nargs("*")
          .choices(roleChoices)
          .required(false)
          .metavar("<role>", "<role>")
          .help("Defines one or more semantic role(s) in order to filter the "
              + "chosen element types to be included in the text output (and "
              + "visualization if the --" + OPTION_VISUALIZATION_FILE_PATH + " "
              + "option is given) by those roles. If not specified, all "
              + "element types will be included, regardless of their semantic "
              + "roles. Choose from: " + roleChoices);

      // Add an argument to define the path to the visualization file.
      this.parser.addArgument("--" + OPTION_VISUALIZATION_FILE_PATH)
          .dest(OPTION_VISUALIZATION_FILE_PATH)
          .required(false)
          .metavar("<visualization-file>")
          .help("Defines a path to a file where pdfact should write a "
              + "visualization of the text output (that is a PDF file where "
              + "the chosen elements are surrounded by bounding boxes). If "
              + "not specified, no visualization will be created.");

      // Add an argument to define the log level.
      StringBuilder choiceStr = new StringBuilder();
      for (PdfActLogLevel level : PdfActLogLevel.getLogLevels()) {
        choiceStr.append("  " + level.getIntLevel() + " " + level + "\n");
      }
      PdfActLogLevel debugLevel = PdfActLogLevel.DEBUG;
      this.parser.addArgument("--" + OPTION_LOG_LEVEL)
          .dest(OPTION_LOG_LEVEL)
          .nargs("?")
          .metavar("<level>")
          .type(Integer.class)
          .setDefault(PdfActCoreSettings.DEFAULT_LOG_LEVEL.getIntLevel())
          .action(new StoreDefaultArgumentAction(debugLevel.getIntLevel()))
          .help("Defines the verbosity of debug messages. The level defines "
              + "the minimum level of severity required for a message to be "
              + "logged. Choose from: \n" + choiceStr.toString());
    }

    /**
     * Parses the given command line arguments.
     * 
     * @param args
     *        The command line arguments to parse.
     * 
     * @throws PdfActException
     *         If parsing the command line arguments fails.
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

    // ========================================================================
    // Getters methods.

    /**
     * Returns true, if a path to the input file is given; false otherwise.
     * 
     * @return True, if a path to the input file is given; false otherwise.
     */
    public boolean hasInputFilePath() {
      return this.inputFilePath != null;
    }

    /**
     * Returns the path to the input file.
     * 
     * @return The path to the input file.
     */
    public String getInputFilePath() {
      return this.inputFilePath;
    }

    // ========================================================================

    /**
     * Returns true, if a path to the output file is given; false otherwise.
     * 
     * @return True, if a path to the output file is given; false otherwise.
     */
    public boolean hasOutputFilePath() {
      return this.outputFilePath != null;
    }

    /**
     * Returns the path to the output file.
     * 
     * @return The path to the output file.
     */
    public String getOutputFilePath() {
      return this.outputFilePath;
    }

    // ========================================================================

    /**
     * Returns true, if an output format is given.
     * 
     * @return True, if an output format is given.
     */
    public boolean hasOutputFormat() {
      return this.outputFormat != null;
    }

    /**
     * Returns the output format.
     * 
     * @return The output format.
     */
    public String getOutputFormat() {
      return this.outputFormat;
    }

    // ========================================================================

    /**
     * Returns true, if a path to a visualization file is given.
     * 
     * @return True, if a path to a visualization file is given.
     */
    public boolean hasVisualizationFilePath() {
      return this.visualizationFilePath != null;
    }

    /**
     * Returns the path to the visualization file.
     * 
     * @return The path to the visualization file.
     */
    public String getVisualizationFilePath() {
      return this.visualizationFilePath;
    }

    // ========================================================================

    /**
     * Returns true, if there is at least one element type is given.
     * 
     * @return True, if there is at least one element type is given; False
     *         otherwise.
     */
    public boolean hasElementTypesFilter() {
      return this.elementTypesFilter != null;
    }

    /**
     * Returns the feature(s) to extract.
     * 
     * @return The feature(s) to extract.
     */
    public List<String> getElementTypesFilter() {
      return this.elementTypesFilter;
    }

    // ========================================================================

    /**
     * Returns true, if there is at least one role given.
     * 
     * @return True, if there is at least one role given; False otherwise.
     */
    public boolean hasSemanticRolesFilter() {
      return this.roles != null;
    }

    /**
     * Returns the role(s) to consider.
     * 
     * @return The role(s) to consider.
     */
    public List<String> getSemanticRolesFilter() {
      return this.roles;
    }

    // ========================================================================

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
     *        The default value to store if the argument value is null.
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
