package pdfact.tokenize;

import pdfact.models.PdfDocument;

/**
 * A tokenizer, that tokenizes pages of PDF documents into text blocks, text
 * lines and words *before* semantics were identified.
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
  void tokenize(PdfDocument pdf);

  /**
   * The factory to create instance of {@link PdfTextTokenizer}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextTokenizerFactory {
    /**
     * Creates a PdfTokenizer.
     * 
     * @return An instance of {@link PdfTextTokenizer}.
     */
    PdfTextTokenizer create();
  }
}
