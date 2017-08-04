package icecite.models;

/**
 * A word in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfWord extends HasCharacters, HasText, PdfElement {
  /**
   * Returns the boolean flag that indicates whether this word is hyphenated.
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
   * Returns true, if this word was dehyphenated.
   * 
   * @return True, if this word was dehyphenated, false otherwise.
   */
  boolean isDehyphenated();

  /**
   * Sets the boolean flag that indicates whether this word was dehyphenated.
   * 
   * @param isDehyphenated
   *        True, if this word was dehyphenated, false otherwise.
   */
  void setIsDehyphenated(boolean isDehyphenated);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfWord}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfWordFactory {
    /**
     * Creates a new instance of PdfWord.
     * 
     * @return A new instance of {@link PdfWord}.
     */
    PdfWord create();

    /**
     * Creates a new instance of PdfWord.
     * 
     * @param characters
     *        The characters of the word.
     * 
     * @return A new instance of {@link PdfWord}.
     */
    PdfWord create(PdfCharacterList characters);
  }
}
