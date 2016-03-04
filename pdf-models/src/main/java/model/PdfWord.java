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
}
