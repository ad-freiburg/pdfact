package pdfact.core.model;

/**
 * An interface to implement by elements that have a bounding box (represented
 * as a rectangle).
 *
 * @author Claudius Korzen
 */
public interface HasRectangle {
  /**
   * Returns the rectangle of this element.
   * 
   * @return The rectangle of this element.
   */
  Rectangle getRectangle();

  /**
   * Sets the rectangle of this element.
   * 
   * @param rectangle The rectangle of this element.
   */
  void setRectangle(Rectangle rectangle);
}
