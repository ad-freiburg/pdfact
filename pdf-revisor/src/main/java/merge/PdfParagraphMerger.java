package merge;

import model.PdfDocument;

public interface PdfParagraphMerger {
  /**
   * Merges the paragraphs of the given document.
   */
  public void merge(PdfDocument document);
}
