package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A position in a PDF document (a pair of a page and a bounding box, rpresented
 * by a rectangle).
 * 
 * @author Claudius Korzen
 */
public interface Position {
  /**
   * Returns the rectangle of this position.
   * 
   * @return The rectangle of this position.
   */
  Rectangle getRectangle();

  /**
   * Sets the rectangle of this position.
   * 
   * @param rectangle
   *        The rectangle of this position.
   */
  void setRectangle(Rectangle rectangle);

  // ==========================================================================

  /**
   * Returns the page of this position.
   * 
   * @return The page of this position.
   */
  Page getPage();

  /**
   * Returns the page number of the page of this position.
   * 
   * @return The page number of the page of this position or 0 if there is no
   *         page given.
   */
  int getPageNumber();

  /**
   * Sets the page of this position.
   * 
   * @param page
   *        The page of this position.
   */
  void setPage(Page page);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Position}.
   * 
   * @author Claudius Korzen
   */
  public interface PositionFactory {
    /**
     * Creates a new instance of {@link Position}.
     * 
     * @param page
     *        The page.
     * @param rectangle
     *        The rectangle.
     * 
     * @return A new instance of {@link Position}.
     */
    Position create(Page page, Rectangle rectangle);

    /**
     * Creates a new instance of {@link Position}.
     * 
     * @param page
     *        The page.
     * @param minX
     *        The minX value of the rectangle.
     * @param minY
     *        The minY value of the rectangle.
     * @param maxX
     *        The maxX value of the rectangle.
     * @param maxY
     *        The maxY value of the rectangle.
     * 
     * @return A new instance of {@link Position}.
     */
    Position create(
        @Assisted("page") Page page,
        @Assisted("minX") float minX,
        @Assisted("minY") float minY,
        @Assisted("maxX") float maxX,
        @Assisted("maxY") float maxY);

    /**
     * Creates a new instance of {@link Position}.
     * 
     * @param page
     *        The page.
     * @param lowerLeft
     *        The lower left point of the rectangle.
     * @param upperRight
     *        The upper right point of the rectangle.
     * 
     * @return A new instance of {@link Position}.
     */
    Position create(
        @Assisted("page") Page page,
        @Assisted("point1") Point lowerLeft,
        @Assisted("point2") Point upperRight);
  }
}
