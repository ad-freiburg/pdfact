package pdfact.model;

/**
 * A word in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Word extends Element, HasCharacters, HasText, HasPositions {
  /**
   * Returns a boolean flag that indicates whether this word is hyphenated.
   * 
   * @return True, if this word is hyphenated, false otherwise.
   */
  boolean isHyphenated();

  /**
   * Sets the boolean flag that indicates whether this word is hyphenated.
   * 
   * @param isHyphenated
   *        True, if this word is hyphenated, false otherwise.
   */
  void setIsHyphenated(boolean isHyphenated);

  // ==========================================================================

  /**
   * Returns true, if this word resulted from the dehyphenation of words.
   * 
   * @return True, if this word resulted from the dehyphenation of words; false
   *         otherwise.
   */
  boolean isDehyphenated();

  /**
   * Sets the boolean flag that indicates whether this word resulted from the
   * dehyphenation of words.
   * 
   * @param isDehyphenated
   *        True, if this word resulted from the dehyphenation of words; false
   *        otherwise.
   */
  void setIsDehyphenated(boolean isDehyphenated);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Word}.
   * 
   * @author Claudius Korzen
   */
  public interface WordFactory {
    /**
     * Creates a new instance of {@link Word}.
     * 
     * @return A new instance of {@link Word}.
     */
    Word create();
  }
}
