package icecite.parse.stream;

import java.io.File;

import icecite.exception.IceciteException;

/**
 * A PDF stream parser.
 *
 * @author Claudius Korzen
 */
public interface PdfStreamParser {
  /**
   * Parses the given PDF file.
   *
   * @param pdf
   *        The PDF file to parse.
   * @throws IceciteException
   *         If something went wrong while parsing the PDF:
   */
  void parsePdf(File pdf) throws IceciteException;

  // ==========================================================================

  /**
   * The factory to create instances of PdfStreamParser.
   * 
   * @author Claudius Korzen
   */
  public interface PdfStreamParserFactory {
    /**
     * Creates a PdfStreamParser.
     * 
     * @param handlers
     *        The callback methods to handle the parsed PDF elements.
     * 
     * @return An instance of {@link PdfStreamParser}.
     */
    PdfStreamParser create(HasPdfStreamParserHandlers handlers);
  }
}
