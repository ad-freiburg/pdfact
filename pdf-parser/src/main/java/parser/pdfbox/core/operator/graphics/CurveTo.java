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
 * c Append curved segment to path.
 * 
 * @author Claudius Korzen
 */
public class CurveTo extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {
    COSNumber x1 = (COSNumber) operands.get(0);
    COSNumber y1 = (COSNumber) operands.get(1);
    COSNumber x2 = (COSNumber) operands.get(2);
    COSNumber y2 = (COSNumber) operands.get(3);
    COSNumber x3 = (COSNumber) operands.get(4);
    COSNumber y3 = (COSNumber) operands.get(5);

    Point point1 = new SimplePoint(x1.floatValue(), y1.floatValue());
    Point point2 = new SimplePoint(x2.floatValue(), y2.floatValue());
    Point point3 = new SimplePoint(x3.floatValue(), y3.floatValue());
    
    context.transform(point1);
    context.transform(point2);
    context.transform(point3);
    
    if (context.getLinePath().getCurrentPoint() == null) {
      context.getLinePath().moveTo(point3.getX(), point3.getY());
    } else {
      context.getLinePath().curveTo(point1.getX(), point1.getY(), 
          point2.getX(), point2.getY(), point3.getX(), point3.getY());
    }
  }

  @Override
  public String getName() {
    return "c";
  }
}
