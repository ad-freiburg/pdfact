package pdfact.core.pipes.parse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link ParsePdfStreamsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParsePdfStreamsPipe implements ParsePdfStreamsPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainParsePdfStreamsPipe.class);

  // ==============================================================================================

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
    new PdfBoxPdfStreamsParser().parse(pdf);
  }
}
