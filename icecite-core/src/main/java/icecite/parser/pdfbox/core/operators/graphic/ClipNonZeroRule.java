package icecite.parser.pdfbox.core.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * W: Set the clipping path using non zero winding rule.
 * 
 * @author Claudius Korzen
 */
public class ClipNonZeroRule extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    this.engine.setClippingWindingRule(Path2D.WIND_NON_ZERO);
  }

  @Override
  public String getName() {
    return "W";
  }
}
