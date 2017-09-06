package pdfact.core.model;

import com.google.inject.assistedinject.Assisted;

/**
 * A position in a PDF document (a pair of a page and a bounding box).
 * 
 * @author Claudius Korzen
 */
public interface Position {
  /**
   * Returns the rectangle.
   * 
   * @return The rectangle.
   */
  Rectangle getRectangle();

  /**
   * Sets the rectangle.
   * 
   * @param rectangle
   *        The rectangle.
   */
  void setRectangle(Rectangle rectangle);

  // ==========================================================================

  /**
   * Returns the page.
   * 
   * @return The page.
   */
  Page getPage();

  /**
   * Sets the page.
   * 
   * @param page
   *        The page.
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
     *        The minX value of the rectangle to be created.
     * @param minY
     *        The minY value of the rectangle to be created.
     * @param maxX
     *        The maxX value of the rectangle to be created.
     * @param maxY
     *        The maxY value of the rectangle to be created.
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
     * @param point1
     *        The lower left vertex of the rectangle to be created.
     * @param point2
     *        The upper right vertex of the rectangle to be created.
     * 
     * @return A new instance of {@link Position}.
     */
    Position create(
        @Assisted("page") Page page,
        @Assisted("point1") Point point1,
        @Assisted("point2") Point point2);
  }
}
