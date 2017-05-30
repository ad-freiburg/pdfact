package icecite.models;

/**
 * An interface that declares that the implementing object has text lines.
 *
 * @author Claudius Korzen
 */
public interface HasTextLines {
  /**
   * Returns the text lines.
   * 
   * @return The text lines.
   */
  PdfTextLineList getTextLines();

  /**
   * Sets the text lines.
   * 
   * @param lines
   *        The list of text lines to set.
   */
  void setTextLines(PdfTextLineList lines);

  /**
   * Adds the text lines.
   * 
   * @param lines
   *        The list of text lines to add.
   */
  void addTextLines(PdfTextLineList lines);

  /**
   * Adds the given text line.
   * 
   * @param line
   *        The text line to add.
   */
  void addTextLine(PdfTextLine line);
}