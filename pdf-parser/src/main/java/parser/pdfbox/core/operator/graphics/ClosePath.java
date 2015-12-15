package parser.pdfbox.core.operator.graphics;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * h : Close path.
 * 
 * @author Claudius Korzen
 */
public class ClosePath extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    context.getLinePath().closePath();
  }
  
  @Override
  public String getName() {
    return "h";
  }
}
