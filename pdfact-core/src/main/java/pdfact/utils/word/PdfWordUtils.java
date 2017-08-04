package pdfact.utils.word;

import pdfact.models.PdfWord;
import pdfact.utils.text.StringUtils;

/**
 * A collection of utility methods that deal with PdfWord objects.
 * 
 * @author Claudius Korzen
 */
public class PdfWordUtils {
  /**
   * Normalizes the text of the given word.
   * 
   * @param word
   *        The word to process.
   * 
   * @return The normalized text of the word.
   */
  public static String normalize(PdfWord word) {
    if (word == null) {
      return null;
    }
    return StringUtils.normalize(word.getText(), false, true, true);
  }
}
