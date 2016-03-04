package model;

/**
 * The interface for a single pdf text character.
 *
 * @author Claudius Korzen
 */
public interface PdfCharacter extends PdfTextElement { 
  /**
   * Returns the orientation of this character.
   */
  public float getOrientation();
  
  /**
   * Returns the codepoint of this character.
   */
  public int getCodePoint();
  
  /**
   * Returns true, if this character is diacritic.
   */
  public boolean isDiacritic();
  
  /**
   * Merges this character with the given diacritic.
   */
  public void mergeDiacritic(PdfCharacter diacritic);
}
