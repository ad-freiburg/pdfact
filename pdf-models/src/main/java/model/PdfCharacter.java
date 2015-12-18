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
}
