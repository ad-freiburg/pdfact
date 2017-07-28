package icecite.models;

// MAYBE: Add Interface HasWords?

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage
    extends HasCharacters, HasFigures, HasShapes, HasTextLines, HasTextBlocks {
  /**
   * Returns the number of this page in the PDF document.
   * 
   * @return The page number.
   */
  int getPageNumber();

  /**
   * Sets the number of this page in the PDF document.
   * 
   * @param pageNumber
   *        The page number.
   */
  void setPageNumber(int pageNumber);

  // ==========================================================================

  /**
   * The factory to creates instances of {@link PdfPage}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfPageFactory {
    /**
     * Creates a new PDF page.
     * 
     * @return An instance of {@link PdfPage}.
     */
    PdfPage create();

    /**
     * Creates a new PDF page.
     * 
     * @param pageNum
     *        The number of the page in the PDF document.
     * 
     * @return An instance of {@link PdfPage}.
     */
    PdfPage create(int pageNum);
  }
}
