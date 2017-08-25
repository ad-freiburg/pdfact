package pdfact.model;

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Page extends HasTextBlocks, HasTextLines, HasCharacters,
    HasFigures, HasShapes {
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
   * The factory to creates instances of {@link Page}.
   * 
   * @author Claudius Korzen
   */
  public interface PageFactory {
    /**
     * Creates a new instance of {@link Page}.
     * 
     * @return A new instance of {@link Page}.
     */
    Page create();

    /**
     * Creates a new instance of {@link Page}.
     * 
     * @param pageNum
     *        The page number.
     * 
     * @return A new instance of {@link Page}.
     */
    Page create(int pageNum);
  }
}
