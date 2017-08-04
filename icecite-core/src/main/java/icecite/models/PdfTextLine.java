package icecite.models;

import icecite.utils.geometric.Line;

/**
 * A text line in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLine extends HasWords, HasText, PdfElement {
  /**
   * Returns the baseline of this text line. For more details about baselines,
   * see https://en.wikipedia.org/wiki/Baseline_(typography).
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
   * The factory to create instances of {@link PdfTextLine}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineFactory {
    /**
     * Creates a new instance of PdfTextLine.
     * 
     * @return A new instance of {@link PdfTextLine}.
     */
    PdfTextLine create();
  }
}
