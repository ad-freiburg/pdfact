package icecite.models;

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage extends HasCharacters, HasFigures, HasShapes,
    HasTextBlocks {
  /**
   * Returns the page number.
   * 
   * @return The page number.
   */
  int getPageNumber();

  /**
   * Sets the page number.
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
     * Creates a new instance of PdfPage.
     * 
     * @return A new instance of {@link PdfPage}.
     */
    PdfPage create();

    /**
     * Creates a new instance of PdfPage.
     * 
     * @param pageNum
     *        The page number.
     * 
     * @return A new instance of {@link PdfPage}.
     */
    PdfPage create(int pageNum);
  }
}
