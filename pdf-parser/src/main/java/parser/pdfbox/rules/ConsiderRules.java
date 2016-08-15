package parser.pdfbox.rules;

import model.PdfCharacter;
import model.PdfColor;
import model.PdfFigure;
import model.PdfShape;

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
    // Don't consider the character, if it is a diacritic.
    if (character.isDiacritic()) {
      return false;
    }
    
    PdfColor color = character.getColor();
    
    // Don't consider the character, if its color is white.
    if (color != null && color.isWhite(0.05f)) {
      return false;
    }

    // Don't consider the character "|" because it is used as PARA_ADDENDUM in
    // tex-paragraph-parser.
    if ("|".equals(character.getUnicode().trim()) 
        && character.getFontsize() < 6.5f) { // TODO
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
    
    // If the character has a valid encoding, we have to consider it.
    if (character.hasEncoding()) {
      return true;
    }
    
    // Don't consider the character, if its doesn't hold any content.
    if (character.getUnicode().trim().isEmpty()) {
      return false;
    }
    
    
    return true;
  }
  
  /**
   * Returns true, if the given figure should be considered on extraction.
   */
  public static boolean considerPdfFigure(PdfFigure figure) {
    return true;
  }
  
  /**
   * Returns true, if the given shape should be considered on extraction.
   */
  public static boolean considerPdfShape(PdfShape shape) {
    if (shape == null) {
      return false;
    }
    
    PdfColor color = shape.getColor();
    
    if (color == null) {
      return false;
    }
        
    return !color.isWhite(); 
  }
}
