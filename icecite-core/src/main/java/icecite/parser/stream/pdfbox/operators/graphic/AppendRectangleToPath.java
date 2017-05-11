package icecite.parser.stream.pdfbox.operators.graphic;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.plain.PlainPoint;

/**
 * re: Appends a rectangle to the path.
 * 
 * @author Claudius Korzen.
 */
public class AppendRectangleToPath extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);
    COSNumber w = (COSNumber) args.get(2);
    COSNumber h = (COSNumber) args.get(3);

    Point lowerLeft = new PlainPoint(x.floatValue(), y.floatValue());
    Point upperRight = new PlainPoint(w.floatValue() + lowerLeft.getX(),
        h.floatValue() + lowerLeft.getY());

    this.engine.transform(lowerLeft);
    this.engine.transform(upperRight);

    // To ensure that the path is created in the right direction,
    // we have to create it by combining single lines instead of
    // creating a simple rectangle
    GeneralPath path = this.engine.getLinePath();
    path.moveTo(lowerLeft.getX(), lowerLeft.getY());
    path.lineTo(upperRight.getX(), lowerLeft.getY());
    path.lineTo(upperRight.getX(), upperRight.getY());
    path.lineTo(lowerLeft.getX(), upperRight.getY());
    path.lineTo(lowerLeft.getX(), lowerLeft.getY());
  }

  @Override
  public String getName() {
    return "re";
  }
}
