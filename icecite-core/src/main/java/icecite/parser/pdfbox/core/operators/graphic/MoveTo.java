package icecite.parser.pdfbox.core.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.plain.PlainPoint;

/**
 * m: Begins a new subpath.
 * 
 * @author Claudius Korzen
 */
public class MoveTo extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);

    Point point = new PlainPoint(x.floatValue(), y.floatValue());

    this.engine.transform(point);
    this.engine.getLinePath().moveTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "m";
  }
}
