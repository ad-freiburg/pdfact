package parser.pdfbox.core.operator.graphics;

import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * q: Save the current graphics state on the graphics state stack.
 * 
 * @author Claudius Korzen
 */
public class SaveGraphicsState extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments) {
    context.saveGraphicsState();
  }

  @Override
  public String getName() {
    return "q";
  }
}
