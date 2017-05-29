package icecite.semanticizer;

import icecite.models.PdfDocument;

/**
 * A semanticizer that identifies the semantics of paragraphs in PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphSemanticizer {
  /**
   * Identifies the semantics of the paragraphs in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  void semanticize(PdfDocument pdf);
}
