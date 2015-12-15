package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * d1: Set width and bounding box information for the glyph and declare
 * that the glyph description specifies only shape, not colour. 
 * 
 * Arguments: wx wy llx lly urx ur.
 * 
 * wx denotes the horizontal displacement in the glyph coordinate system
 * wy shall be 0
 * llx and lly denote the coordinates of the lower-left corner, and urx
 * and ury denote the upper-right corner, of the glyph bounding box. 
 * 
 * @author Claudius Korzen
 */
public class SetType3GlyphWidthAndBoundingBox extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.size() < 6) {
      throw new MissingOperandException(operator, arguments);
    }
    
    // Set glyph with and bounding box for type 3 font
//    COSNumber hDisplacement = (COSNumber) arguments.get(0);
//    COSNumber vDisplacement = (COSNumber) arguments.get(1);
    COSNumber llx = (COSNumber) arguments.get(2);
    COSNumber lly = (COSNumber) arguments.get(3);
    COSNumber urx = (COSNumber) arguments.get(4);
    COSNumber ury = (COSNumber) arguments.get(5);
      
    Point lowerLeft = new SimplePoint(llx.floatValue(), lly.floatValue());
    Point upperRight = new SimplePoint(urx.floatValue(), ury.floatValue());
      
    context.transform(lowerLeft);
    context.transform(upperRight);
    
    float minX = Math.min(lowerLeft.getX(), upperRight.getX());
    float minY = Math.min(lowerLeft.getY(), upperRight.getY());
    float maxX = Math.max(lowerLeft.getX(), upperRight.getX());
    float maxY = Math.max(lowerLeft.getY(), upperRight.getY());
    
    Rectangle boundingBox = new SimpleRectangle(minX, minY, maxX, maxY);
    context.setCurrentType3GlyphBoundingBox(boundingBox);
  }

  @Override
  public String getName() {
    return "d1";
  }
}
