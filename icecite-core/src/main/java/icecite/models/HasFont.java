package icecite.models;

/**
 * An interface that declares that the implementing object has a font.
 *
 * @author Claudius Korzen
 */
public interface HasFont {
  /**
   * Returns the font of the implementing object.
   * 
   * @return The font of the implementing object.
   */
  PdfFont getFont();

  /**
   * Sets the font of the implementing object.
   * 
   * @param font
   *        The font of the implementing object.
   */
  void setFont(PdfFont font);
}
