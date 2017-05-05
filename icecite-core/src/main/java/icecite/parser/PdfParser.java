package icecite.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import icecite.models.PdfDocument;

/**
 * A PDF parser.
 *
 * @author Claudius Korzen
 */
public interface PdfParser {
  /**
   * Parses the given PDF file.
   * 
   * @param pdf
   *        The PDF file to parse, given as Path object.
   * @return An instance of PdfDocument, containing the parsed contents.
   * @throws IOException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(Path pdf) throws IOException;

  /**
   * Parses the given PDF file.
   * 
   * @param pdf
   *        The PDF file to parse, given as File object.
   * @return An instance of PdfDocument, containing the parsed contents.
   * @throws IOException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(File pdf) throws IOException;
}