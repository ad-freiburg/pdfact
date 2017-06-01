package icecite.models;

/**
 * A text block in a page of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlock
    extends PdfElement, HasPage, HasCharacters, HasTextLines, HasText {
  /**
   * The factory to create instances of {@link PdfTextBlock}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextBlockFactory {
    /**
     * Creates a PdfTextBlock.
     * 
     * @param page
     *        The page in which the block is located.
     *
     * @return An instance of {@link PdfTextBlock}.
     */
    PdfTextBlock create(PdfPage page);
  }
}
