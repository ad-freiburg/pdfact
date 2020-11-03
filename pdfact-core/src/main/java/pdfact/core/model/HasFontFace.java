package pdfact.core.model;

/**
 * An interface to implement by elements that have a font face (that is: a pair
 * consisting of a font and a font size).
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
   * @param fontFace The font face of this element.
   */
  void setFontFace(FontFace fontFace);
}
