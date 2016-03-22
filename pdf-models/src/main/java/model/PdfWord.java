package model;

/**
 * The interface for a single word.
 *
 * @author Claudius Korzen
 */
public interface PdfWord extends PdfTextElement {
  /**
   * Returns the first character in this word.
   */
  public PdfCharacter getFirstTextCharacter();
  
  /**
   * Returns the last character in this word.
   */
  public PdfCharacter getLastTextCharacter();
  
  /**
   * Returns true if this word is hyphenated.
   */
  public boolean isHyphenated();
  
  /**
   * Returns true if this word contains a subscript.
   */
  public boolean containsSubScript();
  
  /**
   * Sets the flag whether this word contains a subscript.
   */
  public void setContainsSubScript(boolean containsSubscript);
  
  /**
   * Returns true if this word contains a superscript.
   */
  public boolean containsSuperScript();
  
  /**
   * Sets the flag whether this word contains a superscript.
   */
  public void setContainsSuperScript(boolean containsSuperscript);
  
  /**
   * Returns true if this word contains a punctuation mark.
   */
  public boolean containsPunctuationMark();

  /**
   * Sets the flag whether this word contains a punctuation mark.
   */
  public void setContainsPunctuationMark(boolean containsPunctuationMark);
}
