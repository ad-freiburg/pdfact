package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A position in a document (a pair of a page and a bounding box, represented by
 * a rectangle).
 * 
 * @author Claudius Korzen
 */
public class Position {
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
   * @param page      The page of the position.
   * @param rectangle The rectangle of the position.
   */
  public Position(Page page, Rectangle rectangle) {
    this.page = page;
    this.rectangle = rectangle;
  }

  /**
   * Creates a new position.
   * 
   * @param page The page.
   * @param minX The minX value of the rectangle to be created.
   * @param minY The minY value of the rectangle to be created.
   * @param maxX The maxX value of the rectangle to be created.
   * @param maxY The maxY value of the rectangle to be created.
   * 
   */
  public Position(Page page, float minX, float minY, float maxX, float maxY) {
    this.page = page;
    this.rectangle = new Rectangle(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new position.
   * 
   * @param page   The page.
   * @param point1 The lower left vertex of the rectangle to be created.
   * @param point2 The upper right vertex of the rectangle to be created.
   */
  public Position(Page page, Point point1, Point point2) {
    this.page = page;
    this.rectangle = new Rectangle(point1, point2);
  }

  // ==============================================================================================

  /**
   * Returns the rectangle of this position.
   * 
   * @return The rectangle of this position.
   */
  public Rectangle getRectangle() {
    return this.rectangle;
  }

  /**
   * Sets the rectangle of this position.
   * 
   * @param rectangle The rectangle of this position.
   */
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  // ==============================================================================================

  /**
   * Returns the page of this position.
   * 
   * @return The page of this position.
   */
  public Page getPage() {
    return this.page;
  }

  /**
   * Returns the page number of the page of this position.
   * 
   * @return The page number of the page of this position or 0 if there is no page
   *         given.
   */
  public int getPageNumber() {
    return getPage() != null ? getPage().getPageNumber() : 0;
  }

  /**
   * Sets the page of this position.
   * 
   * @param page The page of this position.
   */
  public void setPage(Page page) {
    this.page = page;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Position(page: " + getPage() + ", rect: " + getRectangle() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Position) {
      Position otherPosition = (Position) other;

      EqualsBuilder build = new EqualsBuilder();
      build.append(getRectangle(), otherPosition.getRectangle());
      // Using getPage() here results in an infinite loop.
      build.append(getPageNumber(), otherPosition.getPageNumber());

      return build.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getRectangle());
    // Using getPage() here results in an infinite loop.
    builder.append(getPageNumber());
    return builder.hashCode();
  }
}
