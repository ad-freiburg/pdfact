package icecite.models;

/**
 * An interface that declares that the implementing object has a font.
 *
 * @author Claudius Korzen
 */
public interface HasFont {
  /**
   * Returns the font.
   * 
   * @return The font.
   */
  PdfFont getFont();

  /**
   * Sets the given font.
   * 
   * @param font
   *        The font.
   */
  void setFont(PdfFont font);

  // ==========================================================================

  /**
   * Returns the font size.
   * 
   * @return The font size.
   */
  float getFontSize();

  /**
   * Sets the given font size.
   * 
   * @param fontsize
   *        The font size.
   */
  void setFontSize(float fontsize);
}
