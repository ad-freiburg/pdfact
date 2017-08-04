package pdfact.semanticize;

import pdfact.models.PdfDocument;

/**
 * A semanticizer that identifies the semantics of elements in PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextSemanticizer {
  /**
   * Identifies the semantics of the paragraphs in the given PDF document.
   */
  void semanticize();

  /**
   * The factory to create instances of PdfTextSemanticizer.
   */
  public interface PdfTextSemanticizerFactory {
    /**
     * Creates a new PdfTextSemanticizer.
     * 
     * @param pdf
     *        The PDF document.
     * @return A new instance of {@link PdfTextSemanticizer}.
     */
    PdfTextSemanticizer create(PdfDocument pdf);
  }
}
