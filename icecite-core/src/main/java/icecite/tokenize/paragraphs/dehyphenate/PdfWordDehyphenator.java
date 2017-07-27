package icecite.tokenize.paragraphs.dehyphenate;

import com.google.inject.assistedinject.Assisted;

import icecite.models.PdfDocument;
import icecite.models.PdfWord;

/**
 * An interface to dehyphenate words.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordDehyphenator {
  /**
   * Returns the word that results from joining the two given subword, either
   * by keeping the hyphen or by removing the hyphen.
   * 
   * @param subword1
   *        The first subword (the subword before the hyphen).
   * @param subword2
   *        The second subword (the subword behind the hyphen).
   * 
   * @return The word that results from joining the two given subword
   */
  PdfWord dehyphenate(PdfWord subword1, PdfWord subword2);

  /**
   * The factory to create instances of PdfWordDehyphenator.
   * 
   * @author Claudius Korzen
   */
  public interface PdfWordDehyphenatorFactory {
    /**
     * Creates a new PdfWordDehyphenator.
     * 
     * @param pdf
     *        The PDF document to process.
     * 
     * @return An instance of PdfWordDehyphenator.
     */
    PdfWordDehyphenator create(@Assisted PdfDocument pdf);
  }
}
