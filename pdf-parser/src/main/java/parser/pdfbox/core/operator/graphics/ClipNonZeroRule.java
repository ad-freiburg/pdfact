package parser.pdfbox.core.operator.graphics;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * W : Set the clipping path using non zero winding rule.
 * 
 * @author Claudius Korzen
 */
public class ClipNonZeroRule extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    context.setClippingWindingRule(GeneralPath.WIND_NON_ZERO);
  }
  
  @Override
  public String getName() {
    return "W";
  }
}
