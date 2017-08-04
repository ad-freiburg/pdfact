package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;

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
   * Creates a new text line list.
   */
  @AssistedInject
  public PlainPdfTextLineList() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new text line list.
   * 
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfTextLineList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return super.toString();
  }
  
  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}