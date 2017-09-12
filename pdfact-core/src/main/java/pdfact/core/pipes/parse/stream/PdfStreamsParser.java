package pdfact.core.pipes.parse.stream;

import pdfact.core.model.PdfDocument;
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
  void parse(PdfDocument pdf) throws PdfActException;

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfStreamsParser}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfStreamsParserFactory {
    /**
     * Creates a new instance of {@link PdfStreamsParser}.
     * 
     * @return A new instance of {@link PdfStreamsParser}.
     */
    PdfStreamsParser create();
  }
}
