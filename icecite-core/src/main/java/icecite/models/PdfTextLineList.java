package icecite.models;

/**
 * A list of text lines in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineList extends PdfElementList<PdfTextLine> {
  /**
   * The factory to create instances of {@link PdfTextLineList}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineListFactory {
    /**
     * Creates a new instance of PdfTextLineList.
     * 
     * @return A new instance of {@link PdfTextLineList}.
     */
    PdfTextLineList create();

    /**
     * Creates a new instance of PdfTextLineList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link PdfTextLineList}.
     */
    PdfTextLineList create(int initialCapacity);
  }
}
