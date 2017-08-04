package icecite.cli;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import icecite.Icecite;
import icecite.exception.IceciteException;
import icecite.guice.IceciteCoreModule;
import icecite.guice.IceciteServiceModule;
import icecite.parse.stream.pdfbox.guice.OperatorProcessorModule;

/**
 * The command line interface of Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteCommandLineInterface {
  /**
   * The logger.
   */
  static final Logger LOG = Logger.getLogger(IceciteCommandLineInterface.class);

  /**
   * The main method that starts the command line interface.
   * 
   * @param args
   *        The command line arguments.
   */
  public static void main(String[] args) {
    int statusCode = 0;
    String errorMessage = null;
    Throwable errorCause = null;
    
    try {
      new IceciteCommandLineInterface().process(args);
    } catch (IceciteException e) {
      statusCode = e.getExitCode();
      errorMessage = e.getMessage();
      errorCause = e.getCause();
    }

    if (statusCode != 0) {
      System.err.println(errorMessage);
      if (errorCause != null) {
        errorCause.printStackTrace();
      }
    }
    System.exit(statusCode);
  }

  /**
   * Processes the given command line arguments.
   * 
   * @param args
   *        The command line arguments.
   * @throws IceciteException If processing the arguments fails.
   */
  protected void process(String[] args) throws IceciteException {
    // Create the injector.
    // TODO: Avoid to inject all needed modules here.
    Injector injector = Guice.createInjector(new IceciteCoreModule(),
        new OperatorProcessorModule(), new IceciteServiceModule());
    Icecite icecite = injector.getInstance(Icecite.class);
    IceciteCommandLineParser clParser = new IceciteCommandLineParser(icecite);

    // Parse the command line arguments.
    clParser.parseArguments(args);

    // Set the path to the input file.
    icecite.setInputFilePath(clParser.getInputFilePath());
    // Set the path to the output file.
    icecite.setSerializationFilePath(clParser.getOutputFilePath());
    // Set the output format if it is given.
    if (clParser.hasOutputFormat()) {
      icecite.setSerializationFormat(clParser.getOutputFormat());
    }
    // Set the path to the visualization path if it is given.
    if (clParser.hasVisualizationFilePath()) {
      icecite.setVisualizationFilePath(clParser.getVisualizationFilePath());
    }
    // Set the features to extract.
    if (clParser.hasFeatures()) {
      icecite.setFeatures(clParser.getFeatures());
    }
    // Set the roles to consider on extraction.
    if (clParser.hasRoles()) {
      icecite.setRoles(clParser.getRoles());
    }
    
    // Run Icecite.
    icecite.run();
  }
}
