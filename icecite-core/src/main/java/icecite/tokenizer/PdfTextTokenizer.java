package icecite.tokenizer;

import icecite.models.PdfDocument;

/**
 * A tokenizer, that identifies text blocks, words, text lines and paragraphs
 * in pages of PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextTokenizer {
  /**
   * Identifies text blocks, words, text lines and paragraphs in the pages of
   * the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  void tokenizePdfDocument(PdfDocument pdf);
}
