package pdfact.model;

/**
 * A text line in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface TextLine extends Element, HasCharacters, HasWords, HasText,
    HasPosition {
  /**
   * Returns the baseline of this text line. For the concept of baselines, see
   * https://en.wikipedia.org/wiki/Baseline_(typography).
   * 
   * @return The base line of this text line.
   */
  Line getBaseline();

  /**
   * Sets the baseline of this text line.
   * 
   * @param baseLine
   *        The base line.
   */
  void setBaseline(Line baseLine);

  // ==========================================================================

  /**
   * The factory to create instances of {@link TextLine}.
   * 
   * @author Claudius Korzen
   */
  public interface TextLineFactory {
    /**
     * Creates a new instance of {@link TextLine}.
     * 
     * @return A new instance of {@link TextLine}.
     */
    TextLine create();
  }
}
