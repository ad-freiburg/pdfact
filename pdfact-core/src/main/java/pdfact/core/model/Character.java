package pdfact.core.model;

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
  int getExtractionRank();

  /**
   * Sets the rank of this character.
   *
   * @param rank
   *        The rank in the extraction order.
   */
  void setExtractionRank(int rank);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Character}.
   * 
   * @author Claudius Korzen
   */
  public interface CharacterFactory {
    /**
     * Creates a new instance of {@link Character}.
     * 
     * @return A new instance of {@link Character}.
     */
    Character create();
  }
}
