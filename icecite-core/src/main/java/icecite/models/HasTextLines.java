package icecite.models;

/**
 * An interface that declares that the implementing object has text lines.
 *
 * @author Claudius Korzen
 */
public interface HasTextLines extends HasTextLineStatistics,
    HasCharacterStatistics {
  /**
   * Returns the text lines.
   * 
   * @return The text lines.
   */
  PdfTextLineList getTextLines();

  /**
   * Returns the first text line.
   * 
   * @return the first text line.
   */
  PdfTextLine getFirstTextLine();

  /**
   * Returns the last text line.
   * 
   * @return the last text line.
   */
  PdfTextLine getLastTextLine();

  // ==========================================================================

  /**
   * Sets the given text lines.
   * 
   * @param lines
   *        The list of text lines.
   */
  void setTextLines(PdfTextLineList lines);

  /**
   * Adds the given text lines.
   * 
   * @param lines
   *        The text lines.
   */
  void addTextLines(PdfTextLineList lines);

  /**
   * Adds the given given text line.
   * 
   * @param line
   *        The text line.
   */
  void addTextLine(PdfTextLine line);
}