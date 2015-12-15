package parser.pdfbox.core.operator.graphics;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.simple.SimplePoint;
import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * re Appends a rectangle to the path.
 * 
 * @author Claudius Korzen.
 */
public final class AppendRectangleToPath extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {
    COSNumber x = (COSNumber) operands.get(0);
    COSNumber y = (COSNumber) operands.get(1);
    COSNumber w = (COSNumber) operands.get(2);
    COSNumber h = (COSNumber) operands.get(3);

    Point lowerLeft = new SimplePoint(x.floatValue(), y.floatValue());
    Point upperRight = new SimplePoint(w.floatValue() + lowerLeft.getX(), 
        h.floatValue() + lowerLeft.getY());
    
    context.transform(lowerLeft);
    context.transform(upperRight);
    
    // To ensure that the path is created in the right direction,
    // we have to create it by combining single lines instead of
    // creating a simple rectangle
    GeneralPath path = context.getLinePath();
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
