package pdfact.models;

/**
 * An interface that is implemented by PDF elements that has a font face (that
 * is a pair consisting of a font and a font size).
 *
 * @author Claudius Korzen
 */
public interface HasFontFace {
  /**
   * Returns the font face of the element.
   * 
   * @return The font face.
   */
  PdfFontFace getFontFace();

  /**
   * Sets the given font face to the element.
   * 
   * @param fontFace
   *        The font face to set.
   */
  void setFontFace(PdfFontFace fontFace);
}
