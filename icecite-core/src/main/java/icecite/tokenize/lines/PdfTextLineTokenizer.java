package icecite.tokenize.lines;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLineList;

/**
 * A tokenizer that tokenizes lists of characters into text lines.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineTokenizer {
  /**
   * Tokenizes the given text area (of characters) into text lines.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   * @param area
   *        The text area (of characters) to tokenize.
   * 
   * @return The list of identified text lines.
   */
  PdfTextLineList tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList area);
}