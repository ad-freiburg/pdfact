package pdfact.models.plain;

import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfElementList;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextLineList;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfTextLineList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineList extends PlainPdfElementList<PdfTextLine>
    implements PdfTextLineList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 1759166517929546020L;

  // ==========================================================================

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainPdfTextLineList() {
    super();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity
   *        The initial capacity of the list
   */
  @AssistedInject
  public PlainPdfTextLineList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<PdfTextLineList> cut(int index) {
    PdfTextLineView left = new PdfTextLineView(this, 0, index);
    PdfTextLineView right = new PdfTextLineView(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  /**
   * A view of a PdfTextLineList.
   * 
   * @author Claudius Korzen
   */
  class PdfTextLineView extends PdfElementView<PdfTextLine>
      implements PdfTextLineList {

    /**
     * The serial id.
     */
    protected static final long serialVersionUID = 884367879377788123L;

    // ========================================================================
    // Constructors.

    /**
     * Creates a new view based on the given parent list.
     * 
     * @param l
     *        The parent list.
     * @param from
     *        The start index in the parent list.
     * @param to
     *        The end index in the parent list.
     */
    public PdfTextLineView(PdfElementList<PdfTextLine> l, int from, int to) {
      super(l, from, to);
    }

    // ========================================================================

    @Override
    public List<PdfTextLineList> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      PdfTextLineView v1 = new PdfTextLineView(this.parent, left, cut);
      PdfTextLineView v2 = new PdfTextLineView(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}