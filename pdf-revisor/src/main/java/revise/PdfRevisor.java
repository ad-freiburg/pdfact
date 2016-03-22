package revise;

import dehyphenize.PdfDehyphenizer;
import dehyphenize.PlainPdfDehyphenizer;
import merge.PdfParagraphMerger;
import merge.PlainPdfParagraphMerger;
import model.PdfDocument;

public class PdfRevisor {
  /**
   * The paragraph merger.
   */
  protected PdfParagraphMerger merger;
  
  /** 
   * The dehyphenizer. 
   */
  protected PdfDehyphenizer dehypehnizer;
  
  /**
   * The public constructor.
   */
  public PdfRevisor() {
    this.merger = new PlainPdfParagraphMerger();
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
    this.merger.merge(document);
  }
  
  /**
   * Dehyphenizes the text lines of the given document.
   */
  protected void dehyphenize(PdfDocument document) {
    this.dehypehnizer.dehyphenize(document);
  }
}
