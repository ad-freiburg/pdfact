package pdfact.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * n: Ends the current path without filling or stroking it.
 * 
 * @author Claudius Korzen
 */
public class EndPath extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    GeneralPath linePath = this.engine.getLinePath();
    if (this.engine.getClippingWindingRule() != -1) {
      linePath.setWindingRule(this.engine.getClippingWindingRule());
      this.engine.getGraphicsState().intersectClippingPath(linePath);
      this.engine.setClippingWindingRule(-1);
    }

    PathIterator itr;
    for (itr = linePath.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          float[] lastMoveTo = this.engine.getLinePathLastMoveToPosition();
          this.engine.setLinePathPosition(lastMoveTo);
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);
          this.engine.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);
          this.engine.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          float[] pos = Arrays.copyOf(coordinates, 2);
          this.engine.setLinePathLastMoveToPosition(pos);
          this.engine.setLinePathPosition(pos);
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);
          this.engine.setLinePathPosition(quadEnd);
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
