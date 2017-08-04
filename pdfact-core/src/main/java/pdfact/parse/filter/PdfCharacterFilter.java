package pdfact.parse.filter;

import pdfact.models.PdfCharacter;
import pdfact.parse.translate.DiacriticsTranslator;

/**
 * A class to filter out certain PDF characters while parsing PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfCharacterFilter {
  /**
   * Checks if the given characters should be filtered out.
   * 
   * @param character
   *        The character to check.
   * 
   * @return True if the given PDF character should be filtered out; False
   *         otherwise.
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
