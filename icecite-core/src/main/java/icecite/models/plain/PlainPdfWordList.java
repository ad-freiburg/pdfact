package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

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
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   */
  @AssistedInject
  public PlainPdfWordList(RectangleFactory rectangleFactory) {
    super(rectangleFactory);
  }

  /**
   * Creates a new PlainPdfWordList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfWordList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
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
