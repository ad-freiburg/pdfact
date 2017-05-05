package icecite.parser.pdfbox.core.operators.graphic;

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
import icecite.models.PdfColorFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShapeFactory;
import icecite.parser.pdfbox.core.operators.OperatorProcessor;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;

/**
 * S: Stroke the path.
 * 
 * @author Claudius Korzen
 */
public class StrokePath extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfShape}.
   */
  @Inject
  protected PdfShapeFactory pdfShapeFactory;

  /**
   * The factory to create instances of {@link PdfColor}.
   */
  @Inject
  protected PdfColorFactory pdfColorFactory;

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

    PdfColor color = this.pdfColorFactory.create(rgb);
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

      float[] linePathPosition = this.engine.getLinePathPosition();

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          float[] lastMoveTo = this.engine.getLinePathLastMoveToPosition();
          this.engine.setLinePathPosition(lastMoveTo);
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);

          // Rectangle rect = new SimpleRectangle();
          // rect.setMinX(Math.min(linePathPosition[0], curveEnd[0]));
          // rect.setMinY(Math.min(linePathPosition[1], curveEnd[1]));
          // rect.setMaxX(Math.max(linePathPosition[0], curveEnd[0]));
          // rect.setMaxY(Math.max(linePathPosition[1], curveEnd[1]));

          Rectangle rect = PlainRectangle.from2Vertices(
              new PlainPoint(linePathPosition[0], linePathPosition[1]),
              new PlainPoint(curveEnd[0], curveEnd[1]));

          PdfShape shape = this.pdfShapeFactory.create();
          shape.setBoundingBox(rect);
          shape.setColor(color);
          this.engine.handlePdfShape(shape);

          this.engine.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);

          // rect = new SimpleRectangle();
          // rect.setMinX(Math.min(linePathPosition[0], lineEnd[0]));
          // rect.setMinY(Math.min(linePathPosition[1], lineEnd[1]));
          // rect.setMaxX(Math.max(linePathPosition[0], lineEnd[0]));
          // rect.setMaxY(Math.max(linePathPosition[1], lineEnd[1]));

          rect = PlainRectangle.from2Vertices(
              new PlainPoint(linePathPosition[0], linePathPosition[1]),
              new PlainPoint(lineEnd[0], lineEnd[1]));

          shape = this.pdfShapeFactory.create();
          shape.setBoundingBox(rect);
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

          // rect = new SimpleRectangle();
          // rect.setMinX(Math.min(linePathPosition[0], quadEnd[0]));
          // rect.setMinY(Math.min(linePathPosition[1], quadEnd[1]));
          // rect.setMaxX(Math.max(linePathPosition[0], quadEnd[0]));
          // rect.setMaxY(Math.max(linePathPosition[1], quadEnd[1]));

          rect = PlainRectangle.from2Vertices(
              new PlainPoint(linePathPosition[0], linePathPosition[1]),
              new PlainPoint(quadEnd[0], quadEnd[1]));

          shape = this.pdfShapeFactory.create();
          shape.setBoundingBox(rect);
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
