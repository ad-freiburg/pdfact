package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfWord;
import icecite.models.PdfWordList;

//TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfWordList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWordList extends PlainPdfElementList<PdfWord>
    implements PdfWordList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 7896204839340209965L;

  /**
   * Creates a new PlainPdfWordList.
   */
  @AssistedInject
  public PlainPdfWordList() {
    super();
  }

  /**
   * Creates a new PlainPdfWordList.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfWordList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}