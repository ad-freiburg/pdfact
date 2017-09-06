package pdfact.core.util.list;

import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Word;

/**
 * A plain implementation of {@link WordList}.
 * 
 * @author Claudius Korzen
 */
public class PlainWordList extends PlainElementList<Word> implements WordList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 7896204839340209965L;

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainWordList() {
    super();
  }

  /**
   * Creates a new PlainPdfWordList.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainWordList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<WordList> cut(int index) {
    WordListView left = new WordListView(this, 0, index);
    WordListView right = new WordListView(this, index, this.size());
    return Arrays.asList(left, right);
  }
  
  @Override
  public WordList subList(int fromIndex, int toIndex) {
    return new WordListView(this, fromIndex, toIndex);
  }

  // ==========================================================================

  /**
   * A view of a {@link WordList}.
   * 
   * @author Claudius Korzen
   */
  class WordListView extends ElementListView<Word> implements WordList {

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
    WordListView(ElementList<Word> parent, int from, int to) {
      super(parent, from, to);
    }

    // ========================================================================

    @Override
    public List<WordList> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      WordListView v1 = new WordListView(this.parent, left, cut);
      WordListView v2 = new WordListView(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
    
    @Override
    public WordList subList(int fromIndex, int toIndex) {
      return new WordListView(this, fromIndex, toIndex);
    }
  }
}