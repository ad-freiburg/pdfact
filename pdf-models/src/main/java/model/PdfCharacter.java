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
   * Returns true, if this character is a diacritic.
   */
  public boolean isDiacritic();
  
  /**
   * Merges this character with the given diacritic.
   */
  public void mergeDiacritic(PdfCharacter diacritic);
  
  /**
   * Returns true, if this character is a subscript.
   */
  public boolean isSubScript();
  
  /**
   * Sets the flag whether this character is a subscript.
   */
  public void setIsSubScript(boolean isSubscript);
  
  /**
   * Returns true, if this character is a superscript.
   */
  public boolean isSuperScript();
  
  /**
   * Sets the flag whether this character is a superscript.
   */
  public void setIsSuperScript(boolean isSuperscript);
  
  /**
   * Returns true, if this character is a punctuation mark.
   */
  public boolean isPunctuationMark();
  
  /**
   * Sets the flag whether this character is a punctuation mark.
   */
  public void setIsPunctuationMark(boolean isPunctuationMark);
    
  /**
   * Returns true if this characters has a valid encoding.
   */
  public boolean hasEncoding();
}
