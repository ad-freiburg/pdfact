package merge;

import model.PdfDocument;

public interface PdfBodyTextMerger {
  /**
   * Merges the body text paragraphs of the given document.
   */
  public void mergeBodyText(PdfDocument document);
}
