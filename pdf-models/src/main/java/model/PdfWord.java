package model;

/**
 * The interface for a single word.
 *
 * @author Claudius Korzen
 */
public interface PdfWord extends PdfTextElement {
  /**
   * Returns true, if this word is hyphenized.
   */
  public boolean isHyphenized();
  
  /**
   * Sets the isHyphenized flag.
   */
  public void setIsHyphenized(boolean isHyphenized);
}
