package model;

import java.util.List;

/**
 * Interface to declare, that the implementing class contains text.
 *
 * @author Claudius Korzen
 */
public interface HasText extends HasPdfColor, HasPdfFont {
  /**
   * Gets the characters.
   */
  public List<PdfCharacter> getTextCharacters();
  
  /**
   * Gets the character in unicode.
   */
  public String getUnicode();
  
  /**
   * Returns true, if this characters is an ascii letter.
   */
  public boolean isAscii();
  
  /**
   * Returns true, if this characters is a digit.
   */
  public boolean isDigit();
}
