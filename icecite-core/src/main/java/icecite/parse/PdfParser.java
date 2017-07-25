package icecite.parse;

import java.io.File;
import java.nio.file.Path;

import com.google.inject.assistedinject.Assisted;

import icecite.exception.IceciteException;
import icecite.models.PdfDocument;

/**
 * A PDF parser that parses the characters, figures and shapes of a PDF file.
 *
 * @author Claudius Korzen
 */
public interface PdfParser {
  /**
   * Parses the given PDF file.
   * 
   * @param pdf
   *        The PDF file to parse, given as Path object.
   * @return An instance of PdfDocument that contains the characters, figures
   *         and shapes per pages.
   * @throws IceciteException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(Path pdf) throws IceciteException;

  /**
   * Parses the given PDF file.
   * 
   * @param pdf
   *        The PDF file to parse, given as File object.
   * @return An instance of PdfDocument that contains the characters, figures
   *         and shapes per pages.
   * @throws IceciteException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(File pdf) throws IceciteException;

  // ==========================================================================

  /**
   * The factory to create instance of {@link PdfParser}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfParserFactory {
    /**
     * Creates a PdfParser.
     * 
     * @return An instance of PdfParser.
     */
    PdfParser create();

    /**
     * Creates a PdfParser.
     * 
     * @param resolveLigatures
     *        The boolean flag to indicate whether ligatures should be resolved
     *        or not.
     * @param resolveDiacritics
     *        The boolean flag to indicate whether characters with diacritics
     *        should be resolved or not.
     * 
     * @return An instance of PdfParser.
     */
    PdfParser create(@Assisted("resolveLigatures") boolean resolveLigatures,
        @Assisted("resolveDiacritics") boolean resolveDiacritics);
  }
}