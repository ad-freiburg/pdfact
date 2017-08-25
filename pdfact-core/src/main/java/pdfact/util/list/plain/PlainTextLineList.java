package pdfact.util.list.plain;

import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.TextLine;
import pdfact.util.list.ElementList;
import pdfact.util.list.TextLineList;

/**
 * A plain implementation of {@link TextLineList}.
 * 
 * @author Claudius Korzen
 */
public class PlainTextLineList extends PlainElementList<TextLine>
    implements TextLineList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 1759166517929546020L;

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainTextLineList() {
    super();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity
   *        The initial capacity of the list
   */
  @AssistedInject
  public PlainTextLineList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<TextLineList> cut(int index) {
    TextLineListView left = new TextLineListView(this, 0, index);
    TextLineListView right = new TextLineListView(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  /**
   * A view of a {@link TextLineList}.
   * 
   * @author Claudius Korzen
   */
  class TextLineListView extends ElementListView<TextLine> 
      implements TextLineList {
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
    TextLineListView(ElementList<TextLine> parent, int from, int to) {
      super(parent, from, to);
    }

    // ========================================================================

    @Override
    public List<TextLineList> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      TextLineListView v1 = new TextLineListView(this.parent, left, cut);
      TextLineListView v2 = new TextLineListView(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}