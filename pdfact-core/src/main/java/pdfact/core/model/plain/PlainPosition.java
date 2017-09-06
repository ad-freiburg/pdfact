package pdfact.core.model.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Page;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link Position}.
 * 
 * @author Claudius Korzen.
 */
public class PlainPosition implements Position {
  /**
   * The rectangle.
   */
  protected Rectangle rectangle;

  /**
   * The page.
   */
  protected Page page;

  /**
   * Creates a new position.
   * 
   * @param page
   *        The page.
   * @param rectangle
   *        The rectangle.
   */
  @AssistedInject
  public PlainPosition(@Assisted Page page, @Assisted Rectangle rectangle) {
    this.page = page;
    this.rectangle = rectangle;
  }

  /**
   * Creates a new position.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
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
   */
  @AssistedInject
  public PlainPosition(RectangleFactory rectangleFactory,
      @Assisted("page") Page page,
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
   *        The lower left vertex of the rectangle to be created.
   * @param point2
   *        The upper right vertex of the rectangle to be created.
   */
  @AssistedInject
  public PlainPosition(RectangleFactory rectangleFactory,
      @Assisted("page") Page page,
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
  public Page getPage() {
    return this.page;
  }

  @Override
  public void setPage(Page page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPosition(page: " + getPage() + ", rect: " + getRectangle()
        + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object o) {
    if (o instanceof Position) {
      Position other = (Position) o;

      EqualsBuilder build = new EqualsBuilder();
      build.append(getRectangle(), other.getRectangle());
      build.append(getPage().getPageNumber(), other.getPage().getPageNumber());

      return build.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getRectangle());
    builder.append(getPage().getPageNumber());
    return builder.hashCode();
  }
}
