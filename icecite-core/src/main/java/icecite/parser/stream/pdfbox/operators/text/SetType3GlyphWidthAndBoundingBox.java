package icecite.parser.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;

/**
 * d1: Set width and bounding box information for the glyph and declare that
 * the glyph description specifies only shape, not colour.
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
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
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

    Point lowerLeft = new PlainPoint(llx.floatValue(), lly.floatValue());
    Point upperRight = new PlainPoint(urx.floatValue(), ury.floatValue());

    this.engine.transform(lowerLeft);
    this.engine.transform(upperRight);

    float minX = Math.min(lowerLeft.getX(), upperRight.getX());
    float minY = Math.min(lowerLeft.getY(), upperRight.getY());
    float maxX = Math.max(lowerLeft.getX(), upperRight.getX());
    float maxY = Math.max(lowerLeft.getY(), upperRight.getY());

    Rectangle boundingBox = new PlainRectangle(minX, minY, maxX, maxY);
    this.engine.setCurrentType3GlyphBoundingBox(boundingBox);
  }

  @Override
  public String getName() {
    return "d1";
  }
}
