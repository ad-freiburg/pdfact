package icecite.tokenize.areas;

import java.util.List;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;

/**
 * A tokenizer that tokenizes lists of characters into text areas. A text area
 * has no special definition and is merely used to distinguish characters from
 * different columns of a page.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextAreaTokenizer {
  /**
   * Tokenizes the given page into text areas.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   *
   * @return The list of identified paragraphs.
   */
  List<PdfCharacterList> tokenize(PdfDocument pdf, PdfPage page);
}