package pdfact.parse.stream.pdfbox.operators.graphic;

import static pdfact.parse.PdfParserSettings.FLOATING_NUMBER_PRECISION;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import com.google.inject.Inject;

import pdfact.models.PdfColor;
import pdfact.models.PdfPage;
import pdfact.models.PdfPosition;
import pdfact.models.PdfShape;
import pdfact.models.PdfPosition.PdfPositionFactory;
import pdfact.models.PdfShape.PdfShapeFactory;
import pdfact.parse.stream.pdfbox.convert.PDColorConverter;
import pdfact.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.utils.geometric.Point;
import pdfact.utils.geometric.Point.PointFactory;
import pdfact.utils.math.MathUtils;

/**
 * S: Stroke the path.
 * 
 * @author Claudius Korzen
 */
public class StrokePath extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfShape}.
   */
  protected PdfShapeFactory shapeFactory;

  /**
   * The converter to convert PDColor objects into PdfColor objects.
   */
  protected PDColorConverter colorConverter;

  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  /**
   * The factory to create instances of {@link PdfPosition}.
   */
  protected PdfPositionFactory positionFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation "StrokePath".
   * 
   * @param colorConverter
   *        The converter to convert PDColor objects into PdfColor objects.
   * @param shapeFactory
   *        The factory to create instances of {@link PdfShape}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param positionFactory
   *        The factory to create instances of {@link PdfPosition}.
   */
  @Inject
  public StrokePath(PDColorConverter colorConverter,
      PdfShapeFactory shapeFactory, PointFactory pointFactory,
      PdfPositionFactory positionFactory) {
    this.colorConverter = colorConverter;
    this.shapeFactory = shapeFactory;
    this.pointFactory = pointFactory;
    this.positionFactory = positionFactory;
  }

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    int windingRule = -1;
    if (args.size() > 0) {
      windingRule = ((COSNumber) args.get(0)).intValue();
    }

    // Compute the bounding box of path to stroke.
    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;

    PDColor c;
    PDColorSpace cs;
    if (windingRule < 0) {
      c = this.engine.getGraphicsState().getStrokingColor();
      cs = this.engine.getGraphicsState().getStrokingColorSpace();
    } else {
      c = this.engine.getGraphicsState().getNonStrokingColor();
      cs = this.engine.getGraphicsState().getNonStrokingColorSpace();
    }

    // Convert the color.
    PdfColor color = this.colorConverter.convert(c, cs);

    GeneralPath linePath = this.engine.getLinePath();

    PathIterator itr;
    int previousSegment = -1;
    for (itr = linePath.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      // Don't consider a MOVETO operation, if it is the last operation in
      // path.
      if (previousSegment == PathIterator.SEG_MOVETO
          && currentSegment != PathIterator.SEG_MOVETO) {
        if (this.engine.getLinePathLastMoveToPosition()[0] < minX) {
          minX = this.engine.getLinePathLastMoveToPosition()[0];
        }
        if (this.engine.getLinePathLastMoveToPosition()[0] > maxX) {
          maxX = this.engine.getLinePathLastMoveToPosition()[0];
        }
        if (this.engine.getLinePathLastMoveToPosition()[1] < minY) {
          minY = this.engine.getLinePathLastMoveToPosition()[1];
        }
        if (this.engine.getLinePathLastMoveToPosition()[1] > maxY) {
          maxY = this.engine.getLinePathLastMoveToPosition()[1];
        }
      }

      float[] pathPosition = this.engine.getLinePathPosition();
      PdfPage pdfPage = this.engine.getCurrentPdfPage();

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          float[] lastMoveTo = this.engine.getLinePathLastMoveToPosition();
          this.engine.setLinePathPosition(lastMoveTo);
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);

          Point ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          Point ur = this.pointFactory.create(curveEnd[0], curveEnd[1]);

          // Round the values.
          ll.setX(MathUtils.round(ll.getX(), FLOATING_NUMBER_PRECISION));
          ll.setY(MathUtils.round(ll.getY(), FLOATING_NUMBER_PRECISION));
          ur.setX(MathUtils.round(ur.getX(), FLOATING_NUMBER_PRECISION));
          ur.setY(MathUtils.round(ur.getY(), FLOATING_NUMBER_PRECISION));

          PdfPosition position = this.positionFactory.create(pdfPage, ll, ur);

          PdfShape shape = this.shapeFactory.create();
          shape.setPosition(position);
          shape.setColor(color);
          this.engine.handlePdfShape(shape);

          this.engine.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);

          ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          ur = this.pointFactory.create(lineEnd[0], lineEnd[1]);

          // Round the values.
          ll.setX(MathUtils.round(ll.getX(), FLOATING_NUMBER_PRECISION));
          ll.setY(MathUtils.round(ll.getY(), FLOATING_NUMBER_PRECISION));
          ur.setX(MathUtils.round(ur.getX(), FLOATING_NUMBER_PRECISION));
          ur.setY(MathUtils.round(ur.getY(), FLOATING_NUMBER_PRECISION));

          position = this.positionFactory.create(pdfPage, ll, ur);

          shape = this.shapeFactory.create();
          shape.setPosition(position);
          shape.setColor(color);
          this.engine.handlePdfShape(shape);

          this.engine.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          float[] pos = Arrays.copyOf(coordinates, 2);
          this.engine.setLinePathLastMoveToPosition(pos);
          this.engine.setLinePathPosition(pos);
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);

          ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          ur = this.pointFactory.create(quadEnd[0], quadEnd[1]);

          // Round the values.
          ll.setX(MathUtils.round(ll.getX(), FLOATING_NUMBER_PRECISION));
          ll.setY(MathUtils.round(ll.getY(), FLOATING_NUMBER_PRECISION));
          ur.setX(MathUtils.round(ur.getX(), FLOATING_NUMBER_PRECISION));
          ur.setY(MathUtils.round(ur.getY(), FLOATING_NUMBER_PRECISION));

          position = this.positionFactory.create(pdfPage, ll, ur);

          shape = this.shapeFactory.create();
          shape.setPosition(position);
          shape.setColor(color);
          this.engine.handlePdfShape(shape);

          this.engine.setLinePathPosition(quadEnd);
          break;
        default:
          break;
      }
      previousSegment = currentSegment;
    }
    linePath.reset();

  }

  @Override
  public String getName() {
    return "S";
  }
}
