package pdfact.parse;

import java.io.File;
import java.nio.file.Path;

import com.google.inject.assistedinject.Assisted;

import pdfact.exception.PdfActException;
import pdfact.models.PdfDocument;

// TODO: Add a feature to read "invisible" tokens -> Needed for "synctex"

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
   * @throws PdfActException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(Path pdf) throws PdfActException;

  /**
   * Parses the given PDF file.
   * 
   * @param pdf
   *        The PDF file to parse, given as File object.
   * @return An instance of PdfDocument that contains the characters, figures
   *         and shapes per pages.
   * @throws PdfActException
   *         If something went wrong while parsing the PDF file.
   */
  PdfDocument parsePdf(File pdf) throws PdfActException;

  // ==========================================================================

  /**
   * Returns true, if ligatures should be translated; False otherwise.
   * 
   * @return true, if ligatures should be translated; False otherwise.
   */
  boolean isTranslateLigatures();

  /**
   * Sets the flag that indicates whether ligatures should be translated or
   * not.
   * 
   * @param translateLigatures
   *        the flag that indicates whether ligatures should be translated or
   *        not.
   */
  void setIsTranslateLigatures(boolean translateLigatures);

  // ==========================================================================

  /**
   * Returns true, if diacritics should be translated; False otherwise.
   * 
   * @return true, if diacritics should be translated; False otherwise.
   */
  boolean isTranslateDiacritics();

  /**
   * Sets the flag that indicates whether diacritics should be translated or
   * not.
   * 
   * @param translateDiacritics
   *        the flag that indicates whether diacritics should be translated or
   *        not.
   */
  void setIsTranslateDiacritics(boolean translateDiacritics);

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
     * @param translateLigatures
     *        The boolean flag to indicate whether ligatures should be
     *        translated or not.
     * @param translateDiacritics
     *        The boolean flag to indicate whether characters with diacritics
     *        should be translated or not.
     * 
     * @return An instance of PdfParser.
     */
    PdfParser create(@Assisted("translateLigatures") boolean translateLigatures,
        @Assisted("translateDiacritics") boolean translateDiacritics);
  }
}