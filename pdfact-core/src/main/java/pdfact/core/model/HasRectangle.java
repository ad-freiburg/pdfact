package pdfact.core.model;

/**
 * An interface that is implemented by an object that has a rectangle.
 *
 * @author Claudius Korzen
 */
public interface HasRectangle {
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
}
