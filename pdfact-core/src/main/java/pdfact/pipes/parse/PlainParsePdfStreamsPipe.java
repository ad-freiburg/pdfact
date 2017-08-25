package pdfact.pipes.parse;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.PdfStreamsParser;
import pdfact.pipes.parse.stream.PdfStreamsParser.PdfStreamsParserFactory;
import pdfact.util.exception.PdfActException;

/**
 * A plain implementation of {@link ParsePdfStreamsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParsePdfStreamsPipe implements ParsePdfStreamsPipe {
  /**
   * The factory to create instances of {@link PdfStreamsParser}.
   */
  protected PdfStreamsParserFactory factory;

  /**
   * Creates a new pipe that parses the streams of a PDF file for characters,
   * figures and shapes.
   * 
   * @param factory
   *        The factory to create instances of {@link PdfStreamsParser}.
   */
  @AssistedInject
  public PlainParsePdfStreamsPipe(PdfStreamsParserFactory factory) {
    this.factory = factory;
  }

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    parsePdf(pdf);
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
