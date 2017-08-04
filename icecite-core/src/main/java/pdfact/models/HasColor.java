package pdfact.models;

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
  PdfColor getColor();

  /**
   * Sets the color of the element.
   * 
   * @param color
   *        The color.
   */
  void setColor(PdfColor color);
}
