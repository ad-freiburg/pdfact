package icecite.models;

/**
 * An interface that declares that the implementing object has a font face.
 *
 * @author Claudius Korzen
 */
public interface HasFontFace {
  /**
   * Returns the font face.
   * 
   * @return The font face.
   */
  PdfFontFace getFontFace();

  /**
   * Sets the given font face.
   * 
   * @param fontFace
   *        The font face.
   */
  void setFontFace(PdfFontFace fontFace);
}
