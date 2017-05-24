package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;

/**
 * A tokenizer that tokenizes lists of characters into text blocks.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlockTokenizer {
  /**
   * Tokenizes the given list of characters into text blocks.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   * @param chars
   *        The characters to tokenize.
   * @return The list of identified paragraphs.
   */
  List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList chars);
}