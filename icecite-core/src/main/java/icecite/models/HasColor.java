package icecite.models;

/**
 * An interface that declares that the implementing object has a color.
 *
 * @author Claudius Korzen
 */
public interface HasColor {
  /**
   * Returns the color.
   * 
   * @return The color.
   */
  PdfColor getColor();

  /**
   * Sets the given color.
   * 
   * @param color
   *        The color.
   */
  void setColor(PdfColor color);
}
