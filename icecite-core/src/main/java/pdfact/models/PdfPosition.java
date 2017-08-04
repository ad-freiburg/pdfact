package pdfact.models;

import com.google.inject.assistedinject.Assisted;

import pdfact.utils.geometric.Point;
import pdfact.utils.geometric.Rectangle;

/**
 * A position in a PDF document, consisting of a page and a bounding box.
 * 
 * @author Claudius Korzen
 */
public interface PdfPosition {
  /**
   * Returns the bounding box.
   * 
   * @return The bounding box.
   */
  Rectangle getRectangle();

  /**
   * Sets the bounding box.
   * 
   * @param rectangle
   *        The bounding box.
   */
  void setRectangle(Rectangle rectangle);

  // ==========================================================================

  /**
   * Returns the page.
   * 
   * @return The page.
   */
  PdfPage getPage();

  /**
   * Sets the page.
   * 
   * @param page
   *        The page.
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
     * Creates a new instance of PdfPosition.
     * 
     * @param page
     *        The page.
     * @param rectangle
     *        The bounding box.
     * 
     * @return A new instance of {@link PdfPosition}.
     */
    PdfPosition create(PdfPage page, Rectangle rectangle);

    /**
     * Creates a new instance of PdfPosition.
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
     * @return A new instance of {@link PdfPosition}.
     */
    PdfPosition create(@Assisted("page") PdfPage page,
        @Assisted("minX") float minX, @Assisted("minY") float minY,
        @Assisted("maxX") float maxX, @Assisted("maxY") float maxY);

    /**
     * Creates a new instance of PdfPosition.
     * 
     * @param page
     *        The page.
     * @param point1
     *        The lower left vertex of the bounding box.
     * @param point2
     *        The upper right vertex of the bounding box.
     * 
     * @return A new instance of {@link PdfPosition}.
     */
    PdfPosition create(@Assisted("page") PdfPage page,
        @Assisted("point1") Point point1,
        @Assisted("point2") Point point2);
  }
}
