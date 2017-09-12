package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have a color.
 *
 * @author Claudius Korzen
 */
public interface HasColor {
  /**
   * Returns the color of this element.
   * 
   * @return The color of this element.
   */
  Color getColor();

  /**
   * Sets the color of this element.
   * 
   * @param color
   *        The color of this element.
   */
  void setColor(Color color);
}
