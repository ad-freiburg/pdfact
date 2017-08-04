package pdfact.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import com.google.inject.Inject;

import pdfact.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.utils.geometric.Point;
import pdfact.utils.geometric.Point.PointFactory;

/**
 * y: Append curved segment to path with final point replicated.
 *
 * @author Claudius Korzen
 */
public class CurveToReplicateFinalPoint extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  // ==========================================================================

  /**
   * Creates a new OperatorProcessor to process the operation
   * "CurveToReplicateFinalPoint".
   * 
   * @param pointFactory
   *        The factory to create instances of Point.
   */
  @Inject
  public CurveToReplicateFinalPoint(PointFactory pointFactory) {
    this.pointFactory = pointFactory;
  }

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x1 = (COSNumber) args.get(0);
    COSNumber y1 = (COSNumber) args.get(1);
    COSNumber x3 = (COSNumber) args.get(2);
    COSNumber y3 = (COSNumber) args.get(3);

    Point point1 = this.pointFactory.create(x1.floatValue(), y1.floatValue());
    Point point3 = this.pointFactory.create(x3.floatValue(), y3.floatValue());

    this.engine.transform(point1);
    this.engine.transform(point3);

    this.engine.getLinePath().curveTo(point1.getX(), point1.getY(),
        point3.getX(), point3.getY(), point3.getX(), point3.getY());
  }

  @Override
  public String getName() {
    return "y";
  }
}
