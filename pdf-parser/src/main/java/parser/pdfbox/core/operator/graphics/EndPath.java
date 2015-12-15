package parser.pdfbox.core.operator.graphics;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * n Ends the current path without filling or stroking it.
 * 
 * @author Claudius Korzen
 */
public final class EndPath extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {

    GeneralPath linePath = context.getLinePath();

    if (context.getClippingWindingRule() != -1) {
      linePath.setWindingRule(context.getClippingWindingRule());
      context.getGraphicsState().intersectClippingPath(linePath);
      context.setClippingWindingRule(-1);
    }

    PathIterator itr;
    for (itr = linePath.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          context.setLinePathPosition(context.getLinePathLastMoveToPosition());
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);
          context.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);
          context.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          context.setLinePathLastMoveToPosition(Arrays.copyOf(coordinates, 2));
          context.setLinePathPosition(context.getLinePathLastMoveToPosition());
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);
          context.setLinePathPosition(quadEnd);
          break;
        default:
          break;
      }
    }
    linePath.reset();
  }

  @Override
  public String getName() {
    return "n";
  }
}
