package parser.pdfbox.core.operator.graphics;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.OperatorProcessor;
import parser.pdfbox.model.PdfBoxColor;
import parser.pdfbox.model.PdfBoxShape;

/**
 * S Stroke the path.
 * 
 * @author Claudius Korzen
 */
public final class StrokePath extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {

    int windingRule = -1;
    if (operands.size() > 0) {
      windingRule = ((COSNumber) operands.get(0)).intValue();
    }

    // Compute the bounding box of path to stroke.
    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;

    PDColor c = null;
    if (windingRule < 0) {
      c = context.getGraphicsState().getStrokingColor();
    } else {
      c = context.getGraphicsState().getNonStrokingColor();
    }
    
    PdfBoxColor color = PdfBoxColor.create(c);

    GeneralPath linePath = context.getLinePath();

    PathIterator itr;
    int previousSegment = -1;
    for (itr = linePath.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      // Don't consider a MOVETO operation, if it is the last operation in path.
      if (previousSegment == PathIterator.SEG_MOVETO
          && currentSegment != PathIterator.SEG_MOVETO) {
        if (context.getLinePathLastMoveToPosition()[0] < minX) {
          minX = context.getLinePathLastMoveToPosition()[0];
        }
        if (context.getLinePathLastMoveToPosition()[0] > maxX) {
          maxX = context.getLinePathLastMoveToPosition()[0];
        }
        if (context.getLinePathLastMoveToPosition()[1] < minY) {
          minY = context.getLinePathLastMoveToPosition()[1];
        }
        if (context.getLinePathLastMoveToPosition()[1] > maxY) {
          maxY = context.getLinePathLastMoveToPosition()[1];
        }
      }

      float[] linePathPosition = context.getLinePathPosition();

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          context.setLinePathPosition(context.getLinePathLastMoveToPosition());
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);

//          Rectangle rect = new SimpleRectangle();
//          rect.setMinX(Math.min(linePathPosition[0], curveEnd[0]));
//          rect.setMinY(Math.min(linePathPosition[1], curveEnd[1]));
//          rect.setMaxX(Math.max(linePathPosition[0], curveEnd[0]));
//          rect.setMaxY(Math.max(linePathPosition[1], curveEnd[1]));
              
          Rectangle rect = SimpleRectangle.from2Vertices(
              new SimplePoint(linePathPosition[0], linePathPosition[1]),
              new SimplePoint(curveEnd[0], curveEnd[1]));
          
          PdfBoxShape shape = new PdfBoxShape(context.getCurrentPage());
          shape.setRectangle(rect);
          shape.setColor(color);
          context.showShape(shape);

          context.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);

//          rect = new SimpleRectangle();
//          rect.setMinX(Math.min(linePathPosition[0], lineEnd[0]));
//          rect.setMinY(Math.min(linePathPosition[1], lineEnd[1]));
//          rect.setMaxX(Math.max(linePathPosition[0], lineEnd[0]));
//          rect.setMaxY(Math.max(linePathPosition[1], lineEnd[1]));
          
          rect = SimpleRectangle.from2Vertices(
              new SimplePoint(linePathPosition[0], linePathPosition[1]),
              new SimplePoint(lineEnd[0], lineEnd[1]));
          
          shape = new PdfBoxShape(context.getCurrentPage());          
          shape.setRectangle(rect);
          shape.setColor(color);
          context.showShape(shape);
          
          context.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          context.setLinePathLastMoveToPosition(Arrays.copyOf(coordinates, 2));

          context.setLinePathPosition(context.getLinePathLastMoveToPosition());
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);

//          rect = new SimpleRectangle();
//          rect.setMinX(Math.min(linePathPosition[0], quadEnd[0]));
//          rect.setMinY(Math.min(linePathPosition[1], quadEnd[1]));
//          rect.setMaxX(Math.max(linePathPosition[0], quadEnd[0]));
//          rect.setMaxY(Math.max(linePathPosition[1], quadEnd[1]));

          rect = SimpleRectangle.from2Vertices(
              new SimplePoint(linePathPosition[0], linePathPosition[1]),
              new SimplePoint(quadEnd[0], quadEnd[1]));
          
          shape = new PdfBoxShape(context.getCurrentPage());
          shape.setRectangle(rect);
          shape.setColor(color);
          context.showShape(shape);

          context.setLinePathPosition(quadEnd);
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
