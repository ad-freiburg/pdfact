package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;

/**
 * A tokenizer that tokenizes text lines into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphTokenizer {
  /**
   * Tokenizes the given list of text lines into paragraphs.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   *
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> tokenize(PdfDocument pdf);
}
