package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * ET: End a text object, discarding the text matrix.
 * 
 * @author Claudius Korzen.
 */
public class EndText extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    context.setTextMatrix(null);
    context.setTextLineMatrix(null);
    // context.endText();
  }

  @Override
  public String getName() {
    return "ET";
  }
}