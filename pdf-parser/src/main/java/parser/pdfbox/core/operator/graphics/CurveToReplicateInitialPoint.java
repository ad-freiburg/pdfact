package parser.pdfbox.core.operator.graphics;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.simple.SimplePoint;
import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * v Append curved segment to path with the initial point replicated.
 * 
 * @author Claudius Korzen
 */
public class CurveToReplicateInitialPoint extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {
    COSNumber x2 = (COSNumber) operands.get(0);
    COSNumber y2 = (COSNumber) operands.get(1);
    COSNumber x3 = (COSNumber) operands.get(2);
    COSNumber y3 = (COSNumber) operands.get(3);

    Point2D currentPoint = context.getLinePath().getCurrentPoint();
    Point point2 = new SimplePoint(x2.floatValue(), y2.floatValue());
    Point point3 = new SimplePoint(x3.floatValue(), y3.floatValue());
    
    context.transform(point2);
    context.transform(point3);

    if (currentPoint == null) {
      context.getLinePath().moveTo(point3.getX(), point3.getY());
    } else {
      context.getLinePath().curveTo(
          (float) currentPoint.getX(), (float) currentPoint.getY(),
          point2.getX(), point2.getY(), point3.getX(), point3.getY());
    }
  }

  @Override
  public String getName() {
    return "v";
  }
}