package icecite.models;

/**
 * A list of words in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordList extends PdfElementList<PdfWord> {
  /**
   * The factory to create instances of {@link PdfWordList}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfWordListFactory {
    /**
     * Creates a new instance of PdfWordList.
     * 
     * @return A new instance of {@link PdfWordList}.
     */
    PdfWordList create();

    /**
     * Creates a new instance of PdfWordList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link PdfWordList}.
     */
    PdfWordList create(int initialCapacity);
  }
}
