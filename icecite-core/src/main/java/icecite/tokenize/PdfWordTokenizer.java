package icecite.tokenize;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;
import icecite.models.PdfWordList;

/**
 * A tokenizer that tokenizes lists of characters into words.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordTokenizer {
  /**
   * Tokenizes the given text line into words.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   * @param line
   *        The text line to tokenize.
   *
   * @return The list of identified words.
   */
  PdfWordList tokenize(PdfDocument pdf, PdfPage page, PdfTextLine line);
}
