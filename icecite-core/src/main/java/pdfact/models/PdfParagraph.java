package pdfact.models;

/**
 * A text paragraph in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraph extends HasTextBlocks, HasRole, HasText,
    PdfElement {
  /**
   * The factory to create instances of {@link PdfParagraph}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfParagraphFactory {
    /**
     * Creates a new instance of PdfParagraph.
     * 
     * @return A new instance of {@link PdfParagraph}.
     */
    PdfParagraph create();
  }
}
