package icecite.models;

import com.google.inject.assistedinject.Assisted;

import icecite.utils.geometric.Point;
import icecite.utils.geometric.Rectangle;

/**
 * A position in a PDF document, consisting of a page and a bounding box.
 * 
 * @author Claudius Korzen
 */
public interface PdfPosition {
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
  PdfPage getPage();

  /**
   * Sets the page of this position.
   * 
   * @param page
   *        The page of this position.
   */
  void setPage(PdfPage page);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfPosition}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfPositionFactory {
    /**
     * Creates a new PdfPosition.
     * 
     * @param page
     *        The page.
     * @param rectangle
     *        The rectangle.
     * 
     * @return An instance of {@link PdfPosition}.
     */
    PdfPosition create(PdfPage page, Rectangle rectangle);

    /**
     * Creates a new PdfPosition.
     * 
     * @param page
     *        The page.
     * @param minX
     *        The minX value of the bounding box.
     * @param minY
     *        The minY value of the bounding box.
     * @param maxX
     *        The maxX value of the bounding box.
     * @param maxY
     *        The maxY value of the bounding box.
     * 
     * @return An instance of {@link PdfPosition}.
     */
    PdfPosition create(@Assisted("page") PdfPage page,
        @Assisted("minX") float minX, @Assisted("minY") float minY,
        @Assisted("maxX") float maxX, @Assisted("maxY") float maxY);

    /**
     * Creates a new PdfPosition.
     * 
     * @param page
     *        The page.
     * @param point1
     *        The lower left vertex.
     * @param point2
     *        The upper right vertex.
     * 
     * @return An instance of {@link PdfPosition}.
     */
    PdfPosition create(@Assisted("page") PdfPage page,
        @Assisted("point1") Point point1,
        @Assisted("point2") Point point2);
  }
}
