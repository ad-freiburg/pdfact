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
 * y Append curved segment to path with final point replicated.
 *
 * @author Claudius Korzen
 */
public final class CurveToReplicateFinalPoint extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands) 
      throws IOException {
    COSNumber x1 = (COSNumber) operands.get(0);
    COSNumber y1 = (COSNumber) operands.get(1);
    COSNumber x3 = (COSNumber) operands.get(2);
    COSNumber y3 = (COSNumber) operands.get(3);

    Point point1 = new SimplePoint(x1.floatValue(), y1.floatValue());
    Point point3 = new SimplePoint(x3.floatValue(), y3.floatValue());
    
    context.transform(point1);
    context.transform(point3);

    context.getLinePath().curveTo(point1.getX(), point1.getY(), 
        point3.getX(), point3.getY(), point3.getX(), point3.getY());
  }

  @Override
  public String getName() {
    return "y";
  }
}
