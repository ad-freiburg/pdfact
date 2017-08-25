package pdfact.model;

/**
 * An interface that is implemented by PDF elements that has a font face (a 
 * pair consisting of a font and a font size).
 *
 * @author Claudius Korzen
 */
public interface HasFontFace {
  /**
   * Returns the font face of the element.
   * 
   * @return The font face.
   */
  FontFace getFontFace();

  /**
   * Sets the given font face to the element.
   * 
   * @param fontFace
   *        The font face to set.
   */
  void setFontFace(FontFace fontFace);
}
