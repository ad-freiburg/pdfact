package pdfact.core.model;

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Page extends HasCharacters, HasFigures, HasShapes,
    HasTextAreas, HasTextLines, HasTextBlocks {
  /**
   * Returns the page number of this page.
   * 
   * @return The page number of this page.
   */
  int getPageNumber();

  /**
   * Sets the page number of this page.
   * 
   * @param pageNumber
   *        The page number of this page.
   */
  void setPageNumber(int pageNumber);

  // ==========================================================================

  /**
   * Returns the height of this page.
   *
   * @return The height of this page.
   */
  float getHeight();

  /**
   * Sets the height of this page.
   *
   * @param height
   *        The height of this page.
   */
  void setHeight(float height);

  // ==========================================================================

  /**
   * Returns the width of this page.
   *
   * @return The width of this page.
   */
  float getWidth();

  /**
   * Sets the width of this page.
   *
   * @param width
   *        The width of this page.
   */
  void setWidth(float width);

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
     * @param pageNumber
     *        The page number.
     * 
     * @return A new instance of {@link Page}.
     */
    Page create(int pageNumber);
  }
}
