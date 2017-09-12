package pdfact.core.model;

/**
 * A word in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Word extends Element, HasCharacters, HasText, HasPositions {
  /**
   * Returns true if this word is hyphenated.
   * 
   * @return True if this word is hyphenated; false otherwise.
   */
  boolean isHyphenated();

  /**
   * Sets the boolean flag that indicates whether this word is hyphenated.
   * 
   * @param isHyphenated
   *        The boolean flag that indicates whether this word is hyphenated.
   */
  void setIsHyphenated(boolean isHyphenated);

  // ==========================================================================

  /**
   * Returns true if this word is dehyphenated.
   * 
   * @return True if this word is dehyphenated; false otherwise.
   */
  boolean isDehyphenated();

  /**
   * Sets the boolean flag that indicates whether this word is dehyphenated.
   * 
   * @param isDehyphenated
   *        The boolean flag that indicates whether this word is dehyphenated.
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
