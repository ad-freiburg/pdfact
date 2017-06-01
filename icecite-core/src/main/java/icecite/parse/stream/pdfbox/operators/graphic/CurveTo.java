package icecite.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import com.google.inject.Inject;

import icecite.parse.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;

/**
 * c: Append curved segment to path.
 * 
 * @author Claudius Korzen
 */
public class CurveTo extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  // ==========================================================================

  /**
   * Creates a new OperatorProcessor to process the operation "CurveTo".
   * 
   * @param pointFactory
   *        The factory to create instances of Point.
   */
  @Inject
  public CurveTo(PointFactory pointFactory) {
    this.pointFactory = pointFactory;
  }

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x1 = (COSNumber) args.get(0);
    COSNumber y1 = (COSNumber) args.get(1);
    COSNumber x2 = (COSNumber) args.get(2);
    COSNumber y2 = (COSNumber) args.get(3);
    COSNumber x3 = (COSNumber) args.get(4);
    COSNumber y3 = (COSNumber) args.get(5);

    Point point1 = this.pointFactory.create(x1.floatValue(), y1.floatValue());
    Point point2 = this.pointFactory.create(x2.floatValue(), y2.floatValue());
    Point point3 = this.pointFactory.create(x3.floatValue(), y3.floatValue());

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
