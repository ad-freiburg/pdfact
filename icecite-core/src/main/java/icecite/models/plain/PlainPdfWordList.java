package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfWord;
import icecite.models.PdfWordList;
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
  private static final long serialVersionUID = 7896204839340209965L;

  /**
   * Creates a new PlainPdfWordList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfWordList(RectangleFactory rectangleFactory) {
    super(rectangleFactory);
  }

  /**
   * Creates a new PlainPdfWordList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfWordList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
  }
}
