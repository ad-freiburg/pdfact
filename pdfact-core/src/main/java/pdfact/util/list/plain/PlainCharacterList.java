package pdfact.util.list.plain;

import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.Character;
import pdfact.util.list.CharacterList;
import pdfact.util.list.ElementList;

/**
 * A plain implementation of {@link CharacterList}.
 * 
 * @author Claudius Korzen
 */
public class PlainCharacterList extends PlainElementList<Character>
    implements CharacterList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 6187582288718513001L;

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainCharacterList() {
    super();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity
   *        The initial capacity of the list
   */
  @AssistedInject
  public PlainCharacterList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<CharacterList> cut(int index) {
    CharacterListView left = new CharacterListView(this, 0, index);
    CharacterListView right = new CharacterListView(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  /**
   * A view of a {@link CharacterList}.
   * 
   * @author Claudius Korzen
   */
  class CharacterListView extends ElementListView<Character> 
      implements CharacterList {

    /**
     * The serial id.
     */
    protected static final long serialVersionUID = 884367879377788123L;

    /**
     * Creates a new view based on the given parent list.
     * 
     * @param parent
     *        The parent list.
     * @param from
     *        The start index in the parent list.
     * @param to
     *        The end index in the parent list.
     */
    CharacterListView(ElementList<Character> parent, int from, int to) {
      super(parent, from, to);
    }

    // ========================================================================

    @Override
    public List<CharacterList> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      CharacterListView v1 = new CharacterListView(this.parent, left, cut);
      CharacterListView v2 = new CharacterListView(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}