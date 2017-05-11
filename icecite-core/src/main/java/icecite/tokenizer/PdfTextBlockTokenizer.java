package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfCharacterSet;
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
   * @param characters
   *        The characters to tokenize.
   * @return The list of identified text blocks.
   */
  List<PdfTextBlock> tokenize(PdfCharacterSet characters);
}