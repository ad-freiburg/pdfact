package pdfact.core.pipes.semanticize.modules;

import pdfact.core.model.Document;

/**
 * A semantic role module that identifies the text blocks of a specific semantic
 * role in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextSemanticizerModule {
  /**
   * Identifies the text blocks of a specific semantic role.
   * 
   * @param pdf
   *        The PDF document to semanticize.
   */
  void semanticize(Document pdf);
}