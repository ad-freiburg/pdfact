package pdfact.models;

/**
 * A text block in a page of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlock extends PdfSinglePositionElement, HasTextLines,
    HasRole, HasText {
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
