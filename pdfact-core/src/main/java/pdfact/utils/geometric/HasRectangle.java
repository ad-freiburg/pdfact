package pdfact.utils.geometric;

/**
 * An interface that declares that the implementing object has a rectangle.
 *
 * @author Claudius Korzen
 */
public interface HasRectangle {
  /**
   * Returns the rectangle of the implementing object.
   * 
   * @return The rectangle of the implementing object.
   */
  Rectangle getRectangle();

  /**
   * Sets the rectangle of the implementing object.
   * 
   * @param rectangle
   *        The rectangle of the implementing object.
   */
  void setRectangle(Rectangle rectangle);
}