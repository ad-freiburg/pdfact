package parser.pdfbox.rules;

import model.PdfCharacter;
import model.PdfColor;

/**
 * Some rules.
 *
 * @author Claudius Korzen
 *
 */
public class ConsiderRules {

  /**
   * Returns true, if the given character should be considered on extraction.
   */
  public static boolean considerPdfCharacter(PdfCharacter character) {
    PdfColor color = character.getColor();

    // Don't consider the character, if its color is white.
    if (color != null && color.isWhite(0.05f)) {
      return false;
    }

    // TODO: The font may have an enecoding for a whitepace. Check this.
    // Don't consider the character, if its doesn't hold any content.
    if (character.getUnicode().trim().isEmpty()) {
      return false;
    }
    
    // Don't consider the character, if its orientation isn't 0.
    if (character.getOrientation() != 0) {
      return false;
    }

    // Don't consider the character, if its a proper rectangle.
    if (character.getRectangle().getWidth() <= 0 
        || character.getRectangle().getHeight() <= 0) {
      return false;
    }
    
    return true;
  }
}
