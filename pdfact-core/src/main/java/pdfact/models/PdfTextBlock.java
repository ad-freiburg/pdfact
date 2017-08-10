package pdfact.models;

/**
 * A text block in a page of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlock extends PdfSinglePositionElement, HasTextLines, 
    HasRole, HasText {
  /**
   * Returns the parent paragraph, which this block is a member of (used on the
   * identification of the paragraphs, to decide whether this text block was
   * already matched to a paragraph).
   * 
   * @return The parent paragraph.
   */
  PdfParagraph getParentPdfParagraph();

  /**
   * Sets the parent paragraph, which this block is a member of.
   * 
   * @param paragraph
   *        The parent paragraph.
   */
  void setParentPdfParagraph(PdfParagraph paragraph);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfTextBlock}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextBlockFactory {
    /**
     * Creates a new instance of PdfTextBlock.
     * 
     * @return A new instance of {@link PdfTextBlock}.
     */
    PdfTextBlock create();
  }
}
