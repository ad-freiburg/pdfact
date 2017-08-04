package pdfact.models;

import java.util.List;

// TODO: Accelerate the cut method in PdfElementList.

/**
 * A list of PDF elements.
 * 
 * @param <T>
 *        The type of the PDF elements.
 * 
 * @author Claudius Korzen
 */
public interface PdfElementList<T extends PdfElement> extends List<T> {
  /**
   * Swaps the elements at index i and j.
   * 
   * @param i
   *        The index of the first element to swap.
   * @param j
   *        The index of the second element to swap.
   */
  void swap(int i, int j);

  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * @return A list of length 2, containing the two resulting views.
   */
  List<? extends PdfElementList<T>> cut(int splitIndex);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfElementList}.
   * 
   * @param <T>
   *        The type of the PDF elements in this list.
   * 
   * @author Claudius Korzen
   */
  public interface PdfElementListFactory<T extends PdfElement> {
    /**
     * Creates a new instance of PdfElementList.
     * 
     * @return A new instance of {@link PdfElementList}.
     */
    PdfElementList<T> create();

    /**
     * Creates a new instance of PdfElementList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link PdfElementList}.
     */
    PdfElementList<T> create(int initialCapacity);
  }
}
