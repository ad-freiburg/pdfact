package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfTextLine;

/**
 * A tokenizer that tokenizes lists of characters into text lines.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineTokenizer {
  /**
   * Tokenizes the given list of characters into text lines.
   * 
   * @param characters
   *        The characters to tokenize.
   * @return The list of identified text lines.
   */
  List<PdfTextLine> tokenize(PdfCharacterSet characters);
}