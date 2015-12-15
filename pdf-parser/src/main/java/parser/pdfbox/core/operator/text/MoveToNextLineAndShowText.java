package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * ': Move to the next line and show a text string. This operator shall have the
 * same effect as the code
 * T*
 * string T
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLineAndShowText extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    context.processOperator("T*", null);
    context.processOperator("Tj", arguments);
  }

  @Override
  public String getName() {
    return "'";
  }
}
