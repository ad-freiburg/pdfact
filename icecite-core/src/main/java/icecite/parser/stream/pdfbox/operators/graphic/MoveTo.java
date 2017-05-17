package icecite.parser.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import com.google.inject.Inject;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;

/**
 * m: Begins a new subpath.
 * 
 * @author Claudius Korzen
 */
public class MoveTo extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  // ==========================================================================
  // Constructors.
  
  /**
   * Creates a new OperatorProcessor to process the operation "MoveTo".
   * 
   * @param pointFactory
   *        The factory to create instances of Point.
   */
  @Inject
  public MoveTo(PointFactory pointFactory) {
    this.pointFactory = pointFactory;
  }
  
  // ==========================================================================
  
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);

    Point point = this.pointFactory.create(x.floatValue(), y.floatValue());

    this.engine.transform(point);
    this.engine.getLinePath().moveTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "m";
  }
}
