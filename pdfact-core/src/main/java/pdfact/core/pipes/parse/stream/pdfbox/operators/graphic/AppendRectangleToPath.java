package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * re: Appends a rectangle to the path.
 * 
 * @author Claudius Korzen.
 */
public class AppendRectangleToPath extends OperatorProcessor {

  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);
    COSNumber w = (COSNumber) args.get(2);
    COSNumber h = (COSNumber) args.get(3);

    float minX = x.floatValue();
    float minY = y.floatValue();
    float maxX = minX + w.floatValue();
    float maxY = minY + h.floatValue();

    Point ll = new Point(minX, minY);
    Point ur = new Point(maxX, maxY);

    this.engine.transform(ll);
    this.engine.transform(ur);

    // To ensure that the path is created in the right direction,
    // we have to create it by combining single lines instead of
    // creating a simple rectangle
    GeneralPath path = this.engine.getLinePath();
    path.moveTo(ll.getX(), ll.getY());
    path.lineTo(ur.getX(), ll.getY());
    path.lineTo(ur.getX(), ur.getY());
    path.lineTo(ll.getX(), ur.getY());
    path.lineTo(ll.getX(), ll.getY());
  }

  @Override
  public String getName() {
    return "re";
  }
}
