package pdfact.core.pipes.parse;

import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link ParseDocumentPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParseDocumentPipe implements ParseDocumentPipe {
  @Override
  public Document execute(Document doc) throws PdfActException {
    parseDocument(doc);
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
