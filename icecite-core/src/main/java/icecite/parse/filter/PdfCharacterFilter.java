package icecite.parse.filter;

import icecite.models.PdfCharacter;
import icecite.parse.translate.DiacriticsTranslator;

/**
 * A class with methods to decide whether to filter PDF characters while
 * parsing PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfCharacterFilter {
  /**
   * Returns true if the given PDF character should be filtered on parsing a
   * PDF document.
   * 
   * @param character
   *        The character to process.
   * @return true if the given PDF character should be ignored while parsing a
   *         PDF document; false otherwise.
   */
  public static boolean filterPdfCharacter(PdfCharacter character) {
    // Ignore the character, if it is null.
    if (character == null) {
      return true;
    }

    // Ignore the character, if it is a diacritic.
    if (DiacriticsTranslator.isDiacritic(character)) {
      return true;
    }

    // Ignore the character, if the width of its bounding box is <= 0.
    if (character.getRectangle().getWidth() <= 0) {
      return true;
    }

    // Ignore the character, if the height of its bounding box is <= 0.
    if (character.getRectangle().getHeight() <= 0) {
      return true;
    }

    // Ignore the the character, if its text is empty.
    String text = character.getText();
    if (text == null || text.trim().isEmpty()) {
      return true;
    }
    return false;
  }
}
