package icecite.models;

/**
 * A list of PDF text lines.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineList extends PdfElementList<PdfTextLine> {
  /**
   * Returns the most common line pitch of lines with the same font face
   * (font/font size) as of the given line.
   * 
   * @param line
   *        The text line to process.
   * 
   * @return The most common line pitch of lines with the same font face as of
   *         the given line.
   */
  float getMostCommonLinePitch(PdfTextLine line);

  /**
   * Returns the average width of white spaces between the words within the
   * lines.
   * 
   * @return The average width of white spaces between the words within the
   * lines.
   */
  float getAverageWhitespaceWidth();

  /**
   * The factory to create instances of {@link PdfTextLineList}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineListFactory {
    /**
     * Creates a PdfTextLineList.
     * 
     * @return An instance of {@link PdfTextLineList}.
     */
    PdfTextLineList create();

    /**
     * Creates a PdfTextLineList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return An instance of {@link PdfTextLineList}.
     */
    PdfTextLineList create(int initialCapacity);
  }
}
