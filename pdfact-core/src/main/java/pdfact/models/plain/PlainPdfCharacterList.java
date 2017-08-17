package pdfact.models.plain;

import java.util.Arrays;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterList;
import pdfact.models.PdfElementList;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfCharacterList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterList extends PlainPdfElementList<PdfCharacter>
    implements PdfCharacterList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 6187582288718513001L;

  // ==========================================================================
  // Constructors.

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainPdfCharacterList() {
    super();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity
   *        The initial capacity of the list
   */
  @AssistedInject
  public PlainPdfCharacterList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<PdfCharacterList> cut(int index) {
    PdfCharacterView left = new PdfCharacterView(this, 0, index);
    PdfCharacterView right = new PdfCharacterView(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  /**
   * A view of a PdfCharacterList.
   * 
   * @author Claudius Korzen
   */
  class PdfCharacterView extends PdfElementView<PdfCharacter>
      implements PdfCharacterList {

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
    PdfCharacterView(PdfElementList<PdfCharacter> l, int from, int to) {
      super(l, from, to);
    }

    // ========================================================================

    @Override
    public List<PdfCharacterList> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      PdfCharacterView v1 = new PdfCharacterView(this.parent, left, cut);
      PdfCharacterView v2 = new PdfCharacterView(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}