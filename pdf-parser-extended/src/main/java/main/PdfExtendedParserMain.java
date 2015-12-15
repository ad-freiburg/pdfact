package main;

import org.apache.commons.cli.CommandLine;

import model.PdfDocument;
import parser.PdfExtendedParser;
import parser.PdfXYCutParser;

/**
 * The main class to start the pdf analyzer.
 * 
 * @author Claudius Korzen
 */
public class PdfExtendedParserMain extends PdfParserMain {
  /**
   * The selected pdf parser.
   */
  protected PdfExtendedParser extendedParser;
  
  /**
   * The default constructor.
   */
  public PdfExtendedParserMain(CommandLine cmd) {
    super(cmd);
    this.extendedParser = new PdfXYCutParser();
  }

  /**
   * Parses the next PDF from the batch of found PDFs and returns if there still
   * was one left.
   */
  protected PdfDocument parseNextPdf() throws Exception {
    PdfDocument document = super.parseNextPdf();
    extendedParser.parse(document);
    return document;
  }
  
  /**
   * The main method.
   */
  public static void main(String[] args) throws Exception {
    // Parse the command line.
    CommandLine cmd = parseCommandLine(args);
        
    // Start the program.
    new PdfExtendedParserMain(cmd).process();     
  }
}

