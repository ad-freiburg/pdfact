package icecite.models;

/**
 * A character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacter
    extends PdfElement, HasPage, HasColor, HasFont, HasText {
  /**
   * Returns the number of this character in the extraction order.
   *
   * @return The number of this character in the extraction order.
   */
  int getExtractionOrderNumber();

  /**
   * Sets the number of this character in the extraction order.
   *
   * @param num
   *        The number of this character in the extraction order.
   */
  void setExtractionOrderNumber(int num);

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
