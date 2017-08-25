package pdfact.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import com.google.inject.Inject;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.model.Point;
import pdfact.model.Rectangle;
import pdfact.model.Point.PointFactory;
import pdfact.model.Rectangle.RectangleFactory;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * d1: Set width and bounding box information for the glyph and declare that the
 * glyph description specifies only shape, not color.
 * 
 * Arguments: wx wy llx lly urx ur.
 * 
 * wx denotes the horizontal displacement in the glyph coordinate system wy
 * shall be 0 llx and lly denote the coordinates of the lower-left corner, and
 * urx and ury denote the upper-right corner, of the glyph bounding box.
 * 
 * @author Claudius Korzen
 */
public class SetType3GlyphWidthAndBoundingBox extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation
   * "SetType3GlyphWidthAndBoundingBox".
   * 
   * @param pointFactory
   *        The factory to create instances of Point.
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @Inject
  public SetType3GlyphWidthAndBoundingBox(PointFactory pointFactory,
      RectangleFactory rectangleFactory) {
    this.pointFactory = pointFactory;
    this.rectangleFactory = rectangleFactory;
  }

  // ==========================================================================

  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    if (args.size() < 6) {
      throw new MissingOperandException(op, args);
    }

    // Set glyph with and bounding box for type 3 font
    // COSNumber hDisplacement = (COSNumber) arguments.get(0);
    // COSNumber vDisplacement = (COSNumber) arguments.get(1);
    COSNumber llx = (COSNumber) args.get(2);
    COSNumber lly = (COSNumber) args.get(3);
    COSNumber urx = (COSNumber) args.get(4);
    COSNumber ury = (COSNumber) args.get(5);

    Point ll = this.pointFactory.create(llx.floatValue(), lly.floatValue());
    Point ur = this.pointFactory.create(urx.floatValue(), ury.floatValue());

    this.engine.transform(ll);
    this.engine.transform(ur);

    float minX = Math.min(ll.getX(), ur.getX());
    float minY = Math.min(ll.getY(), ur.getY());
    float maxX = Math.max(ll.getX(), ur.getX());
    float maxY = Math.max(ll.getY(), ur.getY());

    Rectangle boundBox = this.rectangleFactory.create(minX, minY, maxX, maxY);
    this.engine.setCurrentType3GlyphBoundingBox(boundBox);
  }

  @Override
  public String getName() {
    return "d1";
  }
}
