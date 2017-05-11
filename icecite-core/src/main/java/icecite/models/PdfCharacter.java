package icecite.models;

/**
 * A character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacter extends PdfElement, HasColor, HasFont, HasText {
  /**
   * Returns the font size of this character.
   * 
   * @return The font size of this character.
   */
  float getFontSize();

  /**
   * Sets the font size of this character.
   * 
   * @param fontsize
   *        The font size of this character.
   */
  void setFontSize(float fontsize);
  
  /**
   * The factory to create instances of {@link PdfCharacter}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterFactory {
    /**
     * Creates a PdfCharacter.
     * 
     * @return An instance of {@link PdfCharacter}.
     */
    PdfCharacter create();
  }
}
