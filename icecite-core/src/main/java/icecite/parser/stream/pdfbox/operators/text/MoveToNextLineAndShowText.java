package icecite.parser.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * ': Move to the next line and show a text string. This operator shall have
 * the same effect as the code T* string T
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLineAndShowText extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    this.engine.processOperator("T*", null);
    this.engine.processOperator("Tj", args);
  }

  @Override
  public String getName() {
    return "'";
  }
}
