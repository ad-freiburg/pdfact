package icecite.tokenize;

import icecite.models.PdfDocument;

/**
 * A tokenizer, that tokenizes the PDF document into paragraphs *after* the
 * semantics were identified.
 * 
 * @author Claudius Korzen
 */
public interface PdfDocumentTokenizer {
  /**
   * Identifies paragraphs in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  void tokenizePdfDocument(PdfDocument pdf);
}
