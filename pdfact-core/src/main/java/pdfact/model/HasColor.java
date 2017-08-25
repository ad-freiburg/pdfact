package pdfact.model;

/**
 * An interface that is implemented by PDF elements that have a color.
 *
 * @author Claudius Korzen
 */
public interface HasColor {
  /**
   * Returns the color of the element.
   * 
   * @return The color.
   */
  Color getColor();

  /**
   * Sets the color of the element.
   * 
   * @param color
   *        The color to set.
   */
  void setColor(Color color);
}
