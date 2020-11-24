package pdfact.core.pipes.parse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;

/**
 * A plain implementation of {@link ParseDocumentPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParseDocumentPipe implements ParseDocumentPipe {
  /**
   * The logger.
   */
  protected static final Logger LOG = LogManager.getLogger(PlainParseDocumentPipe.class);

  // ==============================================================================================

  @Override
  public Document execute(Document doc) throws PdfActException {
    LOG.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    LOG.debug("Process: Parsing the document.");
    parseDocument(doc);
    LOG.debug("Parsing the document done.");

    LOG.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return doc;
  }

  /**
   * Parses the given document.
   * 
   * @param doc
   *        The document to parse.
   * 
   * @throws PdfActException
   *         If something went wrong while parsing the document.
   */
  protected void parseDocument(Document doc) throws PdfActException {
    new PdfBoxPdfStreamsParser().parse(doc);
  }
}
