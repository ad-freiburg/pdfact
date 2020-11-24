package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.model.Point;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * l: Append straight line segment to path.
 * 
 * @author Claudius Korzen
 */
public class LineTo extends OperatorProcessor {

  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    // append straight line segment from the current point to the point.
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);

    Point point = new Point(x.floatValue(), y.floatValue());

    this.engine.transform(point);
    this.engine.getLinePath().lineTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "l";
  }
}
