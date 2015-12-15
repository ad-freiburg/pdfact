package parser.pdfbox.core.operator.graphics;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.simple.SimplePoint;
import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * l Append straight line segment to path.
 * 
 * @author Claudius Korzen
 */
public class LineTo extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {
    // append straight line segment from the current point to the point.
    COSNumber x = (COSNumber) operands.get(0);
    COSNumber y = (COSNumber) operands.get(1);
    
    Point point = new SimplePoint(x.floatValue(), y.floatValue());
    
    context.transform(point);
    context.getLinePath().lineTo(point.getX(), point.getY());  
  }

  @Override
  public String getName() {
    return "l";
  }
}
