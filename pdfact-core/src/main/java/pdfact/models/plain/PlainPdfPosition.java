package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfPage;
import pdfact.models.PdfPosition;
import pdfact.utils.geometric.Point;
import pdfact.utils.geometric.Rectangle;
import pdfact.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfPosition}.
 * 
 * @author Claudius Korzen.
 */
public class PlainPdfPosition implements PdfPosition {
  /**
   * The rectangle.
   */
  protected Rectangle rectangle;

  /**
   * The page.
   */
  protected PdfPage page;

  // ==========================================================================
  // The constructor.

  /**
   * Creates a new position.
   * 
   * @param page
   *        The page.
   * @param rect
   *        The rectangle.
   */
  @AssistedInject
  public PlainPdfPosition(@Assisted PdfPage page, @Assisted Rectangle rect) {
    this.page = page;
    this.rectangle = rect;
  }

  /**
   * Creates a new position.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
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
   */
  @AssistedInject
  public PlainPdfPosition(RectangleFactory rectangleFactory,
      @Assisted("page") PdfPage page,
      @Assisted("minX") float minX,
      @Assisted("minY") float minY,
      @Assisted("maxX") float maxX,
      @Assisted("maxY") float maxY) {
    this.page = page;
    this.rectangle = rectangleFactory.create(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new position.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param page
   *        The page.
   * @param point1
   *        The lower left vertex of the bounding box.
   * @param point2
   *        The upper right vertex of the bounding box.
   * 
   */
  @AssistedInject
  public PlainPdfPosition(RectangleFactory rectangleFactory,
      @Assisted("page") PdfPage page,
      @Assisted("point1") Point point1,
      @Assisted("point2") Point point2) {
    this.page = page;
    this.rectangle = rectangleFactory.create(point1, point2);
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this.rectangle;
  }

  @Override
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  // ==========================================================================

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public void setPage(PdfPage page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfPosition(page: " + getPage() + ", rect: " + getRectangle()
        + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfPosition) {
      PdfPosition otherPosition = (PdfPosition) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherPosition.getPage());
      builder.append(getRectangle(), otherPosition.getRectangle());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPage());
    builder.append(getRectangle());
    return builder.hashCode();
  }
}
