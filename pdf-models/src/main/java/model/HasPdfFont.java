package model;

/**
 * Interface to declare, that the implementing class holds a PdfFont. 
 *
 * @author Claudius Korzen
 */
public interface HasPdfFont {
  /**
   * Returns the font.
   */
  PdfFont getFont();
  
  /**
   * Returns the font size.
   */
  float getFontsize();
}
