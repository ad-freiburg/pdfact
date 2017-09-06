package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import com.google.inject.Inject;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.model.Point.PointFactory;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * l: Append straight line segment to path.
 * 
 * @author Claudius Korzen
 */
public class LineTo extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation "LineTo".
   * 
   * @param pointFactory
   *        The factory to create instances of Point.
   */
  @Inject
  public LineTo(PointFactory pointFactory) {
    this.pointFactory = pointFactory;
  }

  // ==========================================================================

  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    // append straight line segment from the current point to the point.
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);

    Point point = this.pointFactory.create(x.floatValue(), y.floatValue());

    this.engine.transform(point);
    this.engine.getLinePath().lineTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "l";
  }
}
