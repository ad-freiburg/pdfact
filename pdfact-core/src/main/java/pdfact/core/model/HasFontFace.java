package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have a font face (a
 * pair consisting of a font and a font size).
 *
 * @author Claudius Korzen
 */
public interface HasFontFace {
  /**
   * Returns the font face of this element.
   * 
   * @return The font face.
   */
  FontFace getFontFace();

  /**
   * Sets the given font face of this element.
   * 
   * @param fontFace
   *        The font face of this element.
   */
  void setFontFace(FontFace fontFace);
}
