package icecite.models;

import java.util.List;

/**
 * A list of characters in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterList extends PdfElementList<PdfCharacter> {
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
  List<PdfCharacterList> cut(int splitIndex);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfCharacterList}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterListFactory {
    /**
     * Creates a new instance of PdfCharacterList.
     * 
     * @return A new instance of {@link PdfCharacterList}.
     */
    PdfCharacterList create();

    /**
     * Creates a new instance of PdfCharacterList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link PdfCharacterList}.
     */
    PdfCharacterList create(int initialCapacity);
  }
}