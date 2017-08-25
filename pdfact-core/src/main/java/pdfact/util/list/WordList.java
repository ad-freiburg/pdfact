package pdfact.util.list;

import java.util.List;

import pdfact.model.Word;

//TODO: Is WordList still needed?

/**
 * A list of words in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface WordList extends ElementList<Word> {
  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * 
   * @return A list of length 2, containing the two resulting views.
   */
  List<WordList> cut(int splitIndex);
  
  // ==========================================================================
  
  /**
   * The factory to create instances of {@link WordList}.
   * 
   * @author Claudius Korzen
   */
  public interface WordListFactory {
    /**
     * Creates a new instance of {@link WordList}.
     * 
     * @return A new instance of {@link WordList}.
     */
    WordList create();

    /**
     * Creates a new instance of {@link WordList}.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link WordList}.
     */
    WordList create(int initialCapacity);
  }
}
