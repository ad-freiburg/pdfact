package icecite.models;

/**
 * An interface that declares that the implementing object has a color.
 *
 * @author Claudius Korzen
 */
public interface HasColor {
  /**
   * Returns the color of the implementing object.
   * 
   * @return The color of the implementing object.
   */
  PdfColor getColor();

  /**
   * Sets the color of the implementing object.
   * 
   * @param color
   *        The color of the implementing object.
   */
  void setColor(PdfColor color);
}
