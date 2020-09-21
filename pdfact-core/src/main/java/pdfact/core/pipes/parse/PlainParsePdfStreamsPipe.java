package pdfact.core.pipes.parse;

import org.apache.logging.log4j.Logger;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.PdfStreamsParser.PdfStreamsParserFactory;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link ParsePdfStreamsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParsePdfStreamsPipe implements ParsePdfStreamsPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create a parser that parses PDF streams.
   */
  protected PdfStreamsParserFactory factory;

  /**
   * Creates a new pipe that parses the streams of a PDF file for characters,
   * figures and shapes.
   * 
   * @param factory
   *        The factory to create a parser that parses PDF streams.
   */
  @AssistedInject
  public PlainParsePdfStreamsPipe(PdfStreamsParserFactory factory) {
    this.factory = factory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Parsing the PDF.");
    parsePdf(pdf);
    log.debug("Parsing the PDF done.");

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  /**
   * Parses the streams of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to parse.
   * 
   * @throws PdfActException
   *         If something went wrong while parsing the streams of the PDF.
   */
  protected void parsePdf(PdfDocument pdf) throws PdfActException {
    this.factory.create().parse(pdf);
  }
}
