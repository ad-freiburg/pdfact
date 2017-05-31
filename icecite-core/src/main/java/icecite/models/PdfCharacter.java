package icecite.models;

/**
 * A character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacter
    extends PdfElement, HasPage, HasColor, HasFont, HasText {
  /**
   * Returns the position of this character in the sequence of extracted PDF
   * elements.
   *
   * @return The position.
   */
  int getPositionInExtractionOrder();

  /**
   * Sets the position of this character in the sequence of extracted PDF
   * elements.
   *
   * @param pos
   *        The position.
   */
  void setPositionInExtractionOrder(int pos);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfCharacter}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterFactory {
    /**
     * Creates a PdfCharacter.
     * 
     * @param page
     *        The page in which the characters is located.
     * 
     * @return An instance of {@link PdfCharacter}.
     */
    PdfCharacter create(PdfPage page);
  }
}
