package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfCharacterSet;
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
   * @param characters
   *        The characters to tokenize.
   * @return The list of identified words.
   */
  List<PdfWord> tokenize(PdfCharacterSet characters);
}
