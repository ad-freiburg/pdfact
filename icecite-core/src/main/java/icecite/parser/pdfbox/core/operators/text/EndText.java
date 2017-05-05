package icecite.parser.pdfbox.core.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * ET: End a text object, discarding the text matrix.
 * 
 * @author Claudius Korzen.
 */
public class EndText extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    this.engine.setTextMatrix(null);
    this.engine.setTextLineMatrix(null);
    // context.endText();
  }

  @Override
  public String getName() {
    return "ET";
  }
}