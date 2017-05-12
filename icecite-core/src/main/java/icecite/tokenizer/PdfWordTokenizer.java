package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfWord;

/**
 * A tokenizer that tokenizes lists of characters into words.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordTokenizer {  
  /**
   * Tokenizes the given list of characters into words.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   * @param chars
   *        The characters to tokenize.
   * @return The list of identified paragraphs.
   */
  List<PdfWord> tokenize(PdfDocument pdf, PdfPage page, PdfCharacterSet chars);
}
