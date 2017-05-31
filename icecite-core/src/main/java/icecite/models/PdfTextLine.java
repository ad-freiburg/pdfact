package icecite.models;

import icecite.utils.geometric.Line;

/**
 * A text line in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLine
    extends PdfElement, HasCharacters, HasWords, HasText {
  /**
   * Returns the baseline of this text line. See
   * https://en.wikipedia.org/wiki/Baseline_(typography) for more details about
   * baselines.
   * 
   * @return The base line of this text line.
   */
  Line getBaseline();

  /**
   * Sets the baseline of this text line.
   * 
   * @param baseLine
   *        The base line of this text line.
   */
  void setBaseline(Line baseLine);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfTextLine}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineFactory {
    /**
     * Creates a PdfTextLine.
     * 
     * @param page
     *        The page in which the line is located.
     * @param characters
     *        The characters of the text line to create.
     * 
     * @return An instance of {@link PdfTextLine}.
     */
    PdfTextLine create(PdfPage page, PdfCharacterList characters);
  }
}
