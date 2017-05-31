package icecite.models;

/**
 * A list of words.
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
     * Creates a PdfWordList.
     * 
     * @return An instance of {@link PdfWordList}.
     */
    PdfWordList create();

    /**
     * Creates a PdfWordList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return An instance of {@link PdfWordList}.
     */
    PdfWordList create(int initialCapacity);
  }
}
