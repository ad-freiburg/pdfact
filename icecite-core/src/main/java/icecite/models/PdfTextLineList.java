package icecite.models;

/**
 * A list of PDF text lines.
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
