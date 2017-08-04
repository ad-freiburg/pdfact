package icecite.models;

/**
 * An interface that is implemented by PDF elements that consist of text lines.
 *
 * @author Claudius Korzen
 */
public interface HasTextLines extends HasTextLineStatistic,
    HasCharacterStatistic {
  /**
   * Returns the text lines of the element.
   * 
   * @return The text lines.
   */
  PdfTextLineList getTextLines();

  /**
   * Returns the first text line of the element.
   * 
   * @return The first text line.
   */
  PdfTextLine getFirstTextLine();

  /**
   * Returns the last text line of the element.
   * 
   * @return The last text line.
   */
  PdfTextLine getLastTextLine();

  // ==========================================================================

  /**
   * Sets the text lines of the element.
   * 
   * @param lines
   *        The text lines to set.
   */
  void setTextLines(PdfTextLineList lines);

  /**
   * Adds text lines to the element.
   * 
   * @param lines
   *        The text lines to add.
   */
  void addTextLines(PdfTextLineList lines);

  /**
   * Adds a text line to the element.
   * 
   * @param line
   *        The text line to add.
   */
  void addTextLine(PdfTextLine line);
}