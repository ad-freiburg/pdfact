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
  List<PdfCharacter> getCharacters();
  
  /**
   * Gets the character in unicode.
   */
  String getUnicode();
}
