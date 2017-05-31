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