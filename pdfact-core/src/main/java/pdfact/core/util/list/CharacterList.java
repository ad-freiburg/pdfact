package pdfact.core.util.list;

import java.util.List;

import pdfact.core.model.Character;

// TODO: Is CharacterList still needed?

/**
 * A list of characters in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface CharacterList extends ElementList<Character> {
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
  List<CharacterList> cut(int splitIndex);

  @Override
  CharacterList subList(int fromIndex, int toIndex);
  
  // ==========================================================================

  /**
   * The factory to create instances of {@link CharacterList}.
   * 
   * @author Claudius Korzen
   */
  public interface CharacterListFactory {
    /**
     * Creates a new instance of {@link CharacterList}.
     * 
     * @return A new instance of {@link CharacterList}.
     */
    CharacterList create();

    /**
     * Creates a new instance of {@link CharacterList}.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link CharacterList}.
     */
    CharacterList create(int initialCapacity);
  }
}