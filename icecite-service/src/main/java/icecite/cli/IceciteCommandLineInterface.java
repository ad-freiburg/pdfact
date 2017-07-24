package icecite.cli;

import java.io.ByteArrayOutputStream;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

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
   * The path to the input PDF file.
   */
  @Argument(
    index = 0,
    required = true,
    metaVar = "inputFile",
    usage = "The path to the input PDF file.")
  protected String inputFilePath;

  /**
   * The path to the output file.
   */
  @Argument(
    index = 1,
    required = true,
    metaVar = "outputFile",
    usage = "The path to the output file.")
  protected String outputFilePath;

  /**
   * The output format.
   */
  // TODO: List the choices in the usage.
  @Option(
    name = "--format",
    metaVar = "<format>",
    usage = "The output format.")
  protected String outputFormat;

  /**
   * The path to the visualization file.
   */
  @Option(
    name = "--visualize",
    metaVar = "<path>",
    usage = "The path to the visualization file.")
  protected String visualizationFilePath;

  /**
   * The feature(s) to extract.
   */
  // TODO: List the choices in the usage.
  @Option(
    name = "--feature",
    metaVar = "<feature>",
    handler = StringArrayOptionHandler.class,
    usage = "The feature(s) to extract.")
  protected String[] features = {};

  /**
   * The role(s) to consider on extraction.
   */
  // TODO: List the choices in the usage.
  @Option(
    name = "--role",
    metaVar = "<role>",
    handler = StringArrayOptionHandler.class,
    usage = "The role(s) to consider on extraction.")
  protected String[] roles = {};

  // ==========================================================================

  /**
   * The parser to parse the command line arguments.
   */
  protected CmdLineParser parser;

  /**
   * The default constructor.
   */
  public IceciteCommandLineInterface() {
    this.parser = new CmdLineParser(this);
  }

  /**
   * Runs this command line interface.
   * 
   * @param args
   *        The command line arguments.
   * @throws CmdLineException
   *         If parsing the command line arguments has failed.
   * @throws IceciteException
   *         If running Icecite using the given arguments has failed.
   * 
   */
  public void run(String[] args) throws CmdLineException, IceciteException {
    // Parse the command line arguments.
    this.parser.parseArgument(args);

    // Create the injector.
    // TODO: Avoid to inject all needed modules here.
    Injector injector = Guice.createInjector(new IceciteCoreModule(),
        new OperatorProcessorModule(), new IceciteServiceModule());

    // Create a new instance of Icecite and pass the parameters to it.
    Icecite icecite = injector.getInstance(Icecite.class);

    icecite.setInputFilePath(this.inputFilePath);
    icecite.setSerializationFilePath(this.outputFilePath);

    if (this.outputFormat != null) {
      icecite.setSerializationFormat(this.outputFormat);
    }
    if (this.visualizationFilePath != null) {
      icecite.setVisualizationFilePath(this.visualizationFilePath);
    }
    if (this.features != null && this.features.length > 0) {
      icecite.setFeatures(this.features);
    }
    if (this.roles != null && this.roles.length > 0) {
      icecite.setRoles(this.roles);
    }

    icecite.run();
  }

  /**
   * Returns the usage info for this command line interface.
   * 
   * @return The usage info for this command line interface.
   */
  public String getUsageInfo() {
    String usageHeader = "Usage: java -jar *.jar [<options>] inputFile outputFile\n";
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      baos.write(usageHeader.getBytes(), 0, usageHeader.length());
      this.parser.printUsage(baos);
      return baos.toString("utf-8");
    } catch (Exception e) {
      return null;
    }
  }

  // ==========================================================================
  // The main method.

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
    boolean showUsageInfo = false;

    IceciteCommandLineInterface cli = new IceciteCommandLineInterface();
    try {
      cli.run(args);
    } catch (IceciteException e) {
      errorMessage = e.getMessage();
      errorCause = e.getCause();
      statusCode = e.getStatusCode();
    } catch (CmdLineException e) {
      errorMessage = e.getMessage();
      errorCause = e.getCause();
      statusCode = 1;
      showUsageInfo = true;
    }

    if (statusCode != 0) {
      System.err.println("An error occured: " + errorMessage);
      if (errorCause != null) {
        errorCause.printStackTrace();
      }
      if (showUsageInfo) {
        System.out.println(cli.getUsageInfo());
      }
    }
    System.exit(statusCode);
  }
}
