package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.utils.geometric.Rectangle.RectangleFactory;

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
  private static final long serialVersionUID = 1759166517929546020L;

  /**
   * Creates a new PlainPdfTextLineList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfTextLineList(RectangleFactory rectangleFactory) {
    super(rectangleFactory);
  }

  /**
   * Creates a new PlainPdfTextLineList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfTextLineList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
  }
}