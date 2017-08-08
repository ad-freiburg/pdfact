package pdfact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import pdfact.exception.PdfActException;
import pdfact.exception.PdfActValidateException;
import pdfact.log.PdfActLogLevel;
import pdfact.models.PdfDocument;
import pdfact.parse.PdfParser.PdfParserFactory;
import pdfact.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import pdfact.tokenize.PdfTextTokenizer.PdfTextTokenizerFactory;
import pdfact.tokenize.paragraphs.PdfParagraphTokenizer.PdfParagraphTokenizerFactory;

/**
 * The main entry point of PdfAct. This class wires up all necessary steps in
 * order to handle the extraction process(es) from PDF file(s).
 * 
 * @author Claudius Korzen
 */
public class PdfActCore {
  /**
   * The logger.
   */
  protected static final Logger LOG = Logger.getLogger(PdfActCore.class);

  /**
   * The logger level.
   */
  protected PdfActLogLevel logLevel = PdfActCoreSettings.DEFAULT_LOG_LEVEL;

  /**
   * The path to the input PDF file.
   */
  protected Path inputFile;

  // ==========================================================================

  /**
   * The factory to create instances of PdfParser.
   */
  protected PdfParserFactory parserFactory;

  /**
   * The factory to create instances of PdfTextTokenizer.
   */
  protected PdfTextTokenizerFactory tokenizerFactory;

  /**
   * The factory to create instances of PdfTextSemanticizer.
   */
  protected PdfTextSemanticizerFactory semanticizerFactory;

  /**
   * The factory to create instances of PdfParagraphTokenizerFactory.
   */
  protected PdfParagraphTokenizerFactory paragraphTokenizerFactory;

  // ==========================================================================
  // The input arguments defined by the user.

  /**
   * The default constructor.
   * 
   * @param parserFactory
   *        The factory to create instances of PdfParser.
   * @param tokenizerFactory
   *        The factory to create instances of PdfTextTokenizer.
   * @param semanticizerFactory
   *        The factory to create instances of PdfTextSemanticizer.
   * @param paragraphTokenizerFactory
   *        The factory to create instances of PdfParagraphTokenizer.
   */
  @Inject
  public PdfActCore(PdfParserFactory parserFactory,
      PdfTextTokenizerFactory tokenizerFactory,
      PdfTextSemanticizerFactory semanticizerFactory,
      PdfParagraphTokenizerFactory paragraphTokenizerFactory) {
    this.parserFactory = parserFactory;
    this.tokenizerFactory = tokenizerFactory;
    this.semanticizerFactory = semanticizerFactory;
    this.paragraphTokenizerFactory = paragraphTokenizerFactory;
  }

  /**
   * Starts the extraction process.
   * 
   * @return The parsed PDF document.
   * 
   * @throws PdfActException
   *         If something went wrong on the extraction process.
   */
  public PdfDocument run() throws PdfActException {
    long start = System.currentTimeMillis();
    LOG.info("Welcome to PdfAct.");
    LOG.info("--------------------------------------------------------------");
    LOG.info("Arguments:");
    LOG.info("--------------------------------------------------------------");
    LOG.info("Input: " + this.inputFile);
    LOG.info("--------------------------------------------------------------");
    LOG.info("Progress:");
    LOG.info("--------------------------------------------------------------");

    // Parse the PDF "as it is".
    PdfDocument pdf = parse();
    // Tokenize the PDF into words, lines and text blocks.
    tokenize(pdf);
    // Identify the semantics of text blocks.
    semanticize(pdf);
    // Join the text blocks to paragraphs.
    paragraphify(pdf);

    long end = System.currentTimeMillis();
    LOG.info("Finished in " + (end - start) + " ms.");

    return pdf;
  }

  // ==========================================================================

  /**
   * Parses the given input PDF file.
   *
   * @return The parsed PDF document.
   * 
   * @throws PdfActException
   *         If something went wrong on parsing the input file.
   */
  protected PdfDocument parse() throws PdfActException {
    LOG.info("Parsing the PDF...");

    // Check if a (validated) input path is given.
    if (this.inputFile == null) {
      throw new PdfActValidateException("No input file given.");
    }

    return this.parserFactory.create().parsePdf(this.inputFile);
  }

  /**
   * Tokenizes the given PDF document into words, lines and text blocks.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong on tokenizing the PDF.
   */
  protected void tokenize(PdfDocument pdf) throws PdfActException {
    LOG.info("Identifying words, text lines and text blocks...");

    this.tokenizerFactory.create().tokenize(pdf);
  }

  /**
   * Identifies the semantic of text blocks in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong on identifying the semantics.
   */
  protected void semanticize(PdfDocument pdf) throws PdfActException {
    LOG.info("Identifying the semantics of the text blocks...");

    this.semanticizerFactory.create(pdf).semanticize();
  }

  /**
   * Identifies the paragraphs in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong on identifying the paragraphs.
   */
  protected void paragraphify(PdfDocument pdf) throws PdfActException {
    LOG.info("Identifying the text paragraphs...");

    this.paragraphTokenizerFactory.create().tokenize(pdf);
  }

  // ==========================================================================

  /**
   * Returns the input file.
   * 
   * @return The input file.
   */
  public Path getInputFile() {
    return this.inputFile;
  }

  /**
   * Validates and sets the path to the input file to process.
   * 
   * @param path
   *        The path to the input file to process.
   * 
   * @throws PdfActException
   *         If the given path is not valid.
   */
  public void setInputFilePath(String path) throws PdfActException {
    // Check if a path is given.
    if (path == null) {
      throw new PdfActValidateException("No input file given.");
    }

    Path file = Paths.get(path).toAbsolutePath();
    // Check if the file exists.
    if (!Files.exists(file)) {
      throw new PdfActValidateException(
          "The input file '" + path + "' does not exist.");
    }

    // Check if the file exists.
    if (!Files.isRegularFile(file)) {
      throw new PdfActValidateException(
          "The input file '" + path + "' is not a regular file.");
    }

    // Check if the file exists.
    if (!Files.isReadable(file)) {
      throw new PdfActValidateException(
          "The input file '" + path + "' can't be read.");
    }

    this.inputFile = file;
  }

  // ==========================================================================

  /**
   * Returns the log level.
   * 
   * @return The log level.
   */
  public PdfActLogLevel getLogLevel() {
    return this.logLevel;
  }

  /**
   * Checks if the current log level implies the given log level.
   * 
   * @param level
   *        The level to check.
   * 
   * @return True, if the current log level implies the given log level.
   */
  public boolean hasLogLevel(PdfActLogLevel level) {
    return this.logLevel != null && this.logLevel.implies(level);
  }

  /**
   * Validates and sets the log level.
   * 
   * @param level
   *        The log level given as an int.
   * @throws PdfActException
   *         if the log level is not valid.
   */
  public void setLogLevel(int level) throws PdfActException {
    if (!PdfActLogLevel.isValidLogLevel(level)) {
      throw new PdfActValidateException("Invalid log level \'" + level + "\'.");
    }

    this.logLevel = PdfActLogLevel.getLogLevel(level);
    LOG.setLevel(this.logLevel.getLog4jEquivalent());
  }
}
