package icecite.cli;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import icecite.Icecite;
import icecite.exception.IceciteException;
import icecite.guice.IceciteCoreModule;
import icecite.guice.IceciteServiceModule;
import icecite.parse.stream.pdfbox.guice.OperatorProcessorModule;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * The command line interface of Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteCommandLineInterface {
  /**
   * The logger.
   */
  static Logger LOG = Logger.getLogger(IceciteCommandLineInterface.class);
  
  /**
   * The name of the option to define the input file.
   */
  protected static final String INPUT_FILE_OPTION = "inputFile";
  
  /**
   * The name of the option to define the output file.
   */
  protected static final String OUTPUT_FILE_OPTION = "outputFile";
  
  /**
   * The name of the option to define the output format.
   */
  protected static final String OUTPUT_FORMAT_OPTION = "format";
  
  /**
   * The name of the option to define the visualization file.
   */
  protected static final String VISUALIZATION_FILE_OPTION = "visualize";
  
  /**
   * The name of the option to define a feature.
   */
  protected static final String FEATURE_OPTION = "feature";
  
  /**
   * The name of the option to define a semantic role.
   */
  protected static final String ROLE_OPTION = "role";
  
  // ==========================================================================
  
  /**
   * The main method that starts the command line interface.
   * 
   * @param args
   *        The command line arguments.
   */
  public static void main(String[] args) {
    // Create the injector.
    // TODO: Avoid to inject all needed modules here.
    Injector injector = Guice.createInjector(new IceciteCoreModule(),
        new OperatorProcessorModule(), new IceciteServiceModule());

    // Create an instance of Icecite.
    Icecite icecite = injector.getInstance(Icecite.class);

    // Create the argument parser.
    ArgumentParser argParser = createArgumentParser(icecite);

    // Parse the command line arguments.
    Namespace ns = null;
    try {
      ns = argParser.parseArgs(args);
    } catch (ArgumentParserException e) {
      argParser.handleError(e);
      System.exit(1);
    }

    try {
      // Set the path to the input file.
      icecite.setInputFilePath(ns.getString(INPUT_FILE_OPTION));
      
      // Set the path to the output file.
      icecite.setSerializationFilePath(ns.getString(OUTPUT_FILE_OPTION));
      
      // Set the output format if it is given.
      String outputFormat = ns.getString(OUTPUT_FORMAT_OPTION);
      if (outputFormat != null) {
        icecite.setSerializationFormat(outputFormat);
      }
      
      // Set the path to the visualization path if it is given.
      String visualizationFilePath = ns.getString(VISUALIZATION_FILE_OPTION);
      if (visualizationFilePath != null) {
        icecite.setVisualizationFilePath(visualizationFilePath);
      }
      
      // Set the features to extract.
      List<String> features = ns.getList(FEATURE_OPTION);
      if (features != null) {
        icecite.setFeatures(features);
      }
      
      // Set the roles to consider on extraction.
      List<String> roles = ns.getList(ROLE_OPTION);
      if (roles != null) {
        icecite.setRoles(roles);
      }
      
      // Run Icecite.
      icecite.run();
    } catch (IceciteException e) {
      LOG.error("An error occured: " + e.getMessage(), e.getCause());
      System.exit(e.getStatusCode());
    }
  }

  /**
   * Creates an argument parser for the given instance of Icecite.
   * 
   * @param icecite
   *        The instance of Icecite to process.
   * @return The created argument parser.
   */
  protected static ArgumentParser createArgumentParser(Icecite icecite) {
    ArgumentParser parser = ArgumentParsers.newArgumentParser("Icecite");

    Argument inputFile = parser.addArgument(INPUT_FILE_OPTION);
    inputFile.required(true);
    inputFile.help("The path to the input file.");
    
    Argument outputFile = parser.addArgument(OUTPUT_FILE_OPTION);
    outputFile.required(true);
    outputFile.help("The path to the output file.");
    
    Argument outputFormat = parser.addArgument("--" + OUTPUT_FORMAT_OPTION);
    Set<String> formatChoices = icecite.getSerializationFormatChoices();
    outputFormat.choices(formatChoices);
    outputFormat.required(false);
    outputFormat.help("The output format, one of " + formatChoices + ".");
    outputFormat.metavar("<FORMAT>");
    
    Argument visFile = parser.addArgument("--" + VISUALIZATION_FILE_OPTION);
    visFile.required(false);
    visFile.help("The path to the visualization file.");
    visFile.metavar("<FILE>");
    
    Argument features = parser.addArgument("--" + FEATURE_OPTION);
    features.choices(icecite.getFeatureNameChoices());
    features.required(false);
    features.nargs("*");
    features.help("The feature(s) to extract. Choices: " 
        + icecite.getFeatureNameChoices());
    features.metavar("<FEATURE>", "<FEATURE>");
    
    Argument roles = parser.addArgument("--" + ROLE_OPTION);
    roles.choices(icecite.getRoleNameChoices());
    roles.required(false);
    roles.nargs("*");
    roles.help("The semantic role(s) to consider on extraction. Choices: " 
        + icecite.getRoleNameChoices());
    roles.metavar("<ROLE>", "<ROLE>");
    return parser;
  }
}
