package pdfact.core.pipes.parse.stream;

import pdfact.core.model.Document;
import pdfact.core.util.exception.PdfActException;

/**
 * A parser that parses the streams of a PDF file.
 *
 * @author Claudius Korzen
 */
public interface PdfStreamsParser {
  /**
   * Parses the streams of the given PDF document for characters, figures and
   * shapes. Once the parsing is done, the extracted characters, figures and
   * shapes can be found in the related PDF page of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to parse.
   *
   * @throws PdfActException
   *         If something went wrong while parsing the PDF:
   */
  void parse(Document pdf) throws PdfActException;
}
