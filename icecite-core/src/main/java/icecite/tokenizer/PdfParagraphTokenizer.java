package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;

/**
 * A tokenizer that tokenizes text lines into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphTokenizer {
  /**
   * Tokenizes the given list of text lines into paragraphs.
   * 
   * @param textLines The text lines to tokenize.
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> tokenize(List<PdfTextLine> textLines);
}
