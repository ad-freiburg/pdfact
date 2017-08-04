package pdfact.cli;

import java.util.List;
import java.util.Set;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.internal.HelpScreenException;
import pdfact.Icecite;
import pdfact.exception.IceciteParseCommandLineException;

/**
 * A parser that parses the command line arguments.
 * 
 * @author Claudius Korzen
 */
public class IceciteCommandLineParser {
  /**
   * The parser.
   */
  protected ArgumentParser parser;

  // ==========================================================================

  /**
   * The name of the option to define the path to the input file.
   */
  protected static final String OPTION_INPUT_FILE_PATH = "inputFile";

  /**
   * The path to the input file.
   */
  @Arg(dest = OPTION_INPUT_FILE_PATH)
  protected String inputFilePath;

  // ==========================================================================

  /**
   * The name of the option to define the path to the output file.
   */
  protected static final String OPTION_OUTPUT_FILE_PATH = "outputFile";

  /**
   * The path to the output file.
   */
  @Arg(dest = OPTION_OUTPUT_FILE_PATH)
  protected String outputFilePath;

  // ==========================================================================

  /**
   * The name of the option to define the output format.
   */
  protected static final String OPTION_OUTPUT_FORMAT = "format";

  /**
   * The output format.
   */
  @Arg(dest = OPTION_OUTPUT_FORMAT)
  protected String outputFormat;

  // ==========================================================================

  /**
   * The name of the option to define the path to the visualization file.
   */
  protected static final String OPTION_VISUALIZATION_FILE_PATH = "visualize";

  /**
   * The output format.
   */
  @Arg(dest = OPTION_VISUALIZATION_FILE_PATH)
  protected String visualizationFilePath;

  // ==========================================================================

  /**
   * The name of the option to define the feature(s) to extract.
   */
  protected static final String OPTION_FEATURE = "feature";

  /**
   * The feature(s) to extract.
   */
  @Arg(dest = OPTION_FEATURE)
  protected List<String> features;

  // ==========================================================================

  /**
   * The name of the option to define the semantic role(s) to consider.
   */
  protected static final String OPTION_ROLE = "role";

  /**
   * The semantic role(s) to consider.
   */
  @Arg(dest = OPTION_ROLE)
  protected List<String> roles;

  // ==========================================================================

  /**
   * The default constructor.
   * 
   * @param icecite
   *        An instance of Icecite.
   */
  public IceciteCommandLineParser(Icecite icecite) {
    String className = icecite.getClass().getName();
    this.parser = ArgumentParsers.newArgumentParser(className);

    // Add an argument to define the path to the input file.
    this.parser.addArgument(OPTION_INPUT_FILE_PATH)
        .dest(OPTION_INPUT_FILE_PATH)
        .help("The path to the input file.")
        .required(true);

    // Add an argument to define the path to the output file.
    this.parser.addArgument(OPTION_OUTPUT_FILE_PATH)
        .dest(OPTION_OUTPUT_FILE_PATH)
        .help("The path to the output file.")
        .required(true);

    // Add an argument to define the output format.
    Set<String> formatChoices = icecite.getSerializationFormatChoices();
    this.parser.addArgument("--" + OPTION_OUTPUT_FORMAT)
        .dest(OPTION_OUTPUT_FORMAT)
        .help("The output format, one of " + formatChoices + ".")
        .metavar("<format>")
        .choices(formatChoices)
        .required(false);

    // Add an argument to define the path to the visualization file.
    this.parser.addArgument("--" + OPTION_VISUALIZATION_FILE_PATH)
        .dest(OPTION_VISUALIZATION_FILE_PATH)
        .help("The path to the visualization file.")
        .metavar("<file>")
        .required(false);

    // Add an argument to define the feature(s) to extract.
    Set<String> featureChoices = icecite.getFeatureNameChoices();
    this.parser.addArgument("--" + OPTION_FEATURE)
        .dest(OPTION_FEATURE)
        .help("The feature(s) to extract. Choices: " + featureChoices)
        .metavar("<feature>", "<feature>")
        .nargs("*")
        .choices(featureChoices)
        .required(false);

    // Add an argument to define the role(s) to extract.
    Set<String> roleChoices = icecite.getRoleNameChoices();
    this.parser.addArgument("--" + OPTION_ROLE)
        .dest(OPTION_ROLE)
        .help("The semantic role(s) to consider. Choices: " + roleChoices)
        .metavar("<role>", "<role>")
        .nargs("*")
        .choices(roleChoices)
        .required(false);
  }

  /**
   * Parses the given command line arguments.
   * 
   * @param args
   *        The command line arguments to parse.
   * 
   * @throws IceciteParseCommandLineException
   *         If parsing the command line arguments fails.
   */
  public void parseArguments(String[] args)
      throws IceciteParseCommandLineException {
    try {
      this.parser.parseArgs(args, this);
    } catch (HelpScreenException e) {
      // Set the status code to 0, such that no error message is shown.
      throw new IceciteParseCommandLineException(null, 0, e);
    } catch (ArgumentParserException e) {
      String message = e.getMessage() + "\n\n" + getUsage();
      throw new IceciteParseCommandLineException(message, e);
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
  
  // ==========================================================================
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

  // ==========================================================================
  
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

  // ==========================================================================
  
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

  // ==========================================================================
  
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

  // ==========================================================================
  
  /**
   * Returns true, if there is at least one feature given.
   * 
   * @return True, if there is at least one feature given; False otherwise.
   */
  public boolean hasFeatures() {
    return this.features != null;
  }
  
  /**
   * Returns the feature(s) to extract.
   * 
   * @return The feature(s) to extract.
   */
  public List<String> getFeatures() {
    return this.features;
  }

  // ==========================================================================
  
  /**
   * Returns true, if there is at least one role given.
   * 
   * @return True, if there is at least one role given; False otherwise.
   */
  public boolean hasRoles() {
    return this.roles != null;
  }
  
  /**
   * Returns the role(s) to consider.
   * 
   * @return The role(s) to consider.
   */
  public List<String> getRoles() {
    return this.roles;
  }
}
