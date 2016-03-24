package revise;

import dehyphenize.PdfDehyphenizer;
import dehyphenize.PlainPdfDehyphenizer;
import merge.PdfBodyTextMerger;
import merge.PlainPdfBodyTextMerger;
import model.PdfDocument;

public class PdfRevisor {
  /**
   * The paragraph merger.
   */
  protected PdfBodyTextMerger merger;
  
  /** 
   * The dehyphenizer. 
   */
  protected PdfDehyphenizer dehypehnizer;
  
  /**
   * The public constructor.
   */
  public PdfRevisor() {
    this.merger = new PlainPdfBodyTextMerger();
    this.dehypehnizer = new PlainPdfDehyphenizer();
  }
  
  /**
   * Revises the given document.
   */
  public void revise(PdfDocument document) {
    mergeParagraphs(document);
    dehyphenize(document);
  }
  
  // ___________________________________________________________________________
  
  /**
   * Merges the paragraphs of the given document.
   */
  protected void mergeParagraphs(PdfDocument document) {
    this.merger.mergeBodyText(document);
  }
  
  /**
   * Dehyphenizes the text lines of the given document.
   */
  protected void dehyphenize(PdfDocument document) {
    this.dehypehnizer.dehyphenize(document);
  }
}
