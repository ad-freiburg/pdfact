package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.Point2D;
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
 * v: Append curved segment to path with the initial point replicated.
 * 
 * @author Claudius Korzen
 */
public class CurveToReplicateInitialPoint extends OperatorProcessor {

  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    COSNumber x2 = (COSNumber) args.get(0);
    COSNumber y2 = (COSNumber) args.get(1);
    COSNumber x3 = (COSNumber) args.get(2);
    COSNumber y3 = (COSNumber) args.get(3);

    Point2D currentPoint = this.engine.getLinePath().getCurrentPoint();
    Point point2 = new Point(x2.floatValue(), y2.floatValue());
    Point point3 = new Point(x3.floatValue(), y3.floatValue());

    this.engine.transform(point2);
    this.engine.transform(point3);

    if (currentPoint == null) {
      this.engine.getLinePath().moveTo(point3.getX(), point3.getY());
    } else {
      this.engine.getLinePath().curveTo((float) currentPoint.getX(),
          (float) currentPoint.getY(), point2.getX(), point2.getY(),
          point3.getX(), point3.getY());
    }
  }

  @Override
  public String getName() {
    return "v";
  }
}