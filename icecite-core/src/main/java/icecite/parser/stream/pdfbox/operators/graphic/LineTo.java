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
 * l: Append straight line segment to path.
 * 
 * @author Claudius Korzen
 */
public class LineTo extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    // append straight line segment from the current point to the point.
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);

    Point point = new PlainPoint(x.floatValue(), y.floatValue());

    this.engine.transform(point);
    this.engine.getLinePath().lineTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "l";
  }
}
