package icecite.tokenize;

import icecite.models.PdfDocument;

/**
 * A tokenizer, that tokenizes pages of PDF documents into text blocks, text
 * lines and words.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextTokenizer {
  /**
   * Identifies text blocks, text lines and words in each page of the given PDF
   * document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  void tokenizePdfDocument(PdfDocument pdf);
}
