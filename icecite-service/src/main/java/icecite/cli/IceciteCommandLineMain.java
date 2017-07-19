package icecite.cli;

import static icecite.cli.IceciteCommandLineOptionNames.OPTION_OUTPUT_FORMAT;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import icecite.Icecite;
import icecite.guice.IceciteServiceModule;

/**
 * The main class to manage the Icecite parser from the command line.
 * 
 * @author Claudius Korzen
 */
public class IceciteCommandLineMain {
  /**
   * The main method.
   * 
   * @param args
   *        The command line arguments.
   */
  public static void main(String[] args) {
    // Create the injector.
    Injector injector = Guice.createInjector(new IceciteServiceModule());

    // Create a new instance of Icecite.
    Icecite icecite = injector.getInstance(Icecite.class);

    // Create the parser to parse the command line arguments.
    CommandLineParser clp = injector.getInstance(CommandLineParser.class);

    // Create the help formatter.
    HelpFormatter hf = injector.getInstance(HelpFormatter.class);

    // Define the options.
    Options options = injector.getInstance(Options.class);
    for (Option option : defineOptions()) {
      options.addOption(option);
    }

    // Parse the command line arguments.
    CommandLine cmd = null;
    try {
      cmd = clp.parse(options, args);
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      printUsage(hf, options);
      System.exit(1);
    }

    // Make sure that there is a path to an input file given.
    String inputFilePath = args.length > 0 ? args[0] : null;
    if (inputFilePath == null || inputFilePath.isEmpty()) {
      System.err.println("There is no input path given.");
      printUsage(hf, options);
      System.exit(1);
    }
    
    // Make sure that there is a path to an output file given.
    String outputFilePath = args.length > 1 ? args[1] : null;
    if (outputFilePath == null || outputFilePath.isEmpty()) {
      System.err.println("There is no output path given.");
      printUsage(hf, options);
      System.exit(1);
    }
    
    // Pass the options to the parser.
    icecite.setInputFile(Paths.get(inputFilePath));
    icecite.setOutputFile(Paths.get(outputFilePath));
    icecite.setOutputFormat(cmd.getOptionValue(OPTION_OUTPUT_FORMAT));
    
    try {
      icecite.run();
    } catch (Exception e) {
      System.err.println("An error occured: " + e.getMessage());
      System.exit(1);
    }
  }

  // ===========================================================================
  
  /**
   * Defines the available command line options.
   * 
   * @return A list of the available command line options. 
   */
  protected static List<Option> defineOptions() {
    List<Option> options = new ArrayList<>();

    // Add an option to define the output format.
    options.add(Option.builder()
      .argName(OPTION_OUTPUT_FORMAT)
      .longOpt(OPTION_OUTPUT_FORMAT)
      .hasArg(true)
      .desc("The output format. One of:.") // TODO: List the choices.
      .build()
    );

    return options;
  }
  
  // ===========================================================================
  // Utility methods.

  /**
   * Prints the usage info.
   * 
   * @param formatter
   *        The help formatter to use to format the help.
   * @param options
   *        The command line options.
   */
  protected static void printUsage(HelpFormatter formatter, Options options) {
    formatter.printHelp(
        "java -jar IceciteCommandLineMain.java [options] <pdf-file> <out-file>",
        "\nParses the given pdf file and outputs the extraction result to "
            + "the given output file.",
        options, null);
  }
}
