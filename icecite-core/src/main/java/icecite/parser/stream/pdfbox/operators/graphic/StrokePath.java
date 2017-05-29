package icecite.parser.stream.pdfbox.operators.graphic;

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
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;

import com.google.inject.Inject;

import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfColorRegistry;
import icecite.models.PdfPage;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

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
   * The factory to create instances of {@link PdfColor}.
   */
  protected PdfColorFactory colorFactory;

  /**
   * The registry to manage {@link PdfColor} objects.
   */
  protected PdfColorRegistry colorRegistry;

  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation "StrokePath".
   * 
   * @param colorFactory
   *        The factory to create instances of {@link PdfColor}.
   * @param colorRegistry
   *        The registry to manage {@link PdfColor} objects.
   * @param shapeFactory
   *        The factory to create instances of PdfShapeFactory.
   * @param pointFactory
   *        The factory to create instances of Point.
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @Inject
  public StrokePath(PdfColorFactory colorFactory,
      PdfColorRegistry colorRegistry, PdfShapeFactory shapeFactory,
      PointFactory pointFactory, RectangleFactory rectangleFactory) {
    this.colorFactory = colorFactory;
    this.colorRegistry = colorRegistry;
    this.shapeFactory = shapeFactory;
    this.pointFactory = pointFactory;
    this.rectangleFactory = rectangleFactory;
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

    float[] rgb = new float[3];
    if (windingRule < 0) {
      PDColor c = this.engine.getGraphicsState().getStrokingColor();
      PDColorSpace cs = this.engine.getGraphicsState().getStrokingColorSpace();
      rgb = cs.toRGB(c.getComponents());
    } else {
      PDGraphicsState graphicsState = this.engine.getGraphicsState();
      PDColor c = graphicsState.getNonStrokingColor();
      PDColorSpace cs = graphicsState.getNonStrokingColorSpace();
      rgb = cs.toRGB(c.getComponents());
    }

    // TODO set the properties of the color.
    PdfColor color = this.colorRegistry.getColor(Arrays.toString(rgb));
    if (color == null) {
      color = this.colorFactory.create();
      color.setName(Arrays.toString(rgb));
      color.setRGB(rgb);
      this.colorRegistry.registerColor(color);
    }

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

          // TODO: Check if ll and ur is indeed ll and ur.
          Point ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          Point ur = this.pointFactory.create(curveEnd[0], curveEnd[1]);
          Rectangle rect = this.rectangleFactory.create(ll, ur);

          PdfShape shape = this.shapeFactory.create(pdfPage);
          shape.setRectangle(rect);
          shape.setColor(color);
          this.engine.handlePdfShape(shape);

          this.engine.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);

          // TODO: Check if ll and ur is indeed ll and ur.
          ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          ur = this.pointFactory.create(lineEnd[0], lineEnd[1]);
          rect = this.rectangleFactory.create(ll, ur);

          shape = this.shapeFactory.create(pdfPage);
          shape.setRectangle(rect);
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

          // TODO: Check if ll and ur is indeed ll and ur.
          ll = this.pointFactory.create(pathPosition[0], pathPosition[1]);
          ur = this.pointFactory.create(quadEnd[0], quadEnd[1]);
          rect = this.rectangleFactory.create(ll, ur);

          shape = this.shapeFactory.create(pdfPage);
          shape.setRectangle(rect);
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
