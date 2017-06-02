package icecite.tokenize;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;

/**
 * A tokenizer that tokenizes the text blocks of a PDF document to paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphTokenizer {
  /**
   * Tokenizes the text blocks of the given PDF document into paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> tokenize(PdfDocument pdf);
}
