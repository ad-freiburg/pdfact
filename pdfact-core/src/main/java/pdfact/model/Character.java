package pdfact.model;

/**
 * A single character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Character extends Element, HasText, HasPosition, HasFontFace,
    HasColor {
  /**
   * Returns the rank of this character in the extraction order of PDF elements.
   *
   * @return The rank in the extraction order.
   */
  int getSequenceNumber();

  /**
   * Sets the rank of this character.
   *
   * @param num
   *        The sequence number.
   */
  void setSequenceNumber(int num);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Character}.
   * 
   * @author Claudius Korzen
   */
  public interface CharacterFactory {
    /**
     * Creates a new {@link Character}.
     * 
     * @return An instance of {@link Character}.
     */
    Character create();
  }
}
