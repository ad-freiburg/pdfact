package icecite.parser.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.plain.PlainPoint;

/**
 * c: Append curved segment to path.
 * 
 * @author Claudius Korzen
 */
public class CurveTo extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x1 = (COSNumber) args.get(0);
    COSNumber y1 = (COSNumber) args.get(1);
    COSNumber x2 = (COSNumber) args.get(2);
    COSNumber y2 = (COSNumber) args.get(3);
    COSNumber x3 = (COSNumber) args.get(4);
    COSNumber y3 = (COSNumber) args.get(5);

    Point point1 = new PlainPoint(x1.floatValue(), y1.floatValue());
    Point point2 = new PlainPoint(x2.floatValue(), y2.floatValue());
    Point point3 = new PlainPoint(x3.floatValue(), y3.floatValue());

    this.engine.transform(point1);
    this.engine.transform(point2);
    this.engine.transform(point3);

    if (this.engine.getLinePath().getCurrentPoint() == null) {
      this.engine.getLinePath().moveTo(point3.getX(), point3.getY());
    } else {
      this.engine.getLinePath().curveTo(point1.getX(), point1.getY(),
          point2.getX(), point2.getY(), point3.getX(), point3.getY());
    }
  }

  @Override
  public String getName() {
    return "c";
  }
}
