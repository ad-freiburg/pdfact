package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSNumber;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * TD: Move to the start of the next line, offset from the start of the current 
 * line by (tx, ty). As a side effect, this operator shall set the leading 
 * parameter in the text state. This operator shall have the same effect as 
 * this code: 
 * -ty TL
 * tx ty Td
 * 
 * @author Claudius Korzen
 */
public class MoveTextSetLeading extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.size() < 2) {
      throw new MissingOperandException(operator, arguments);
    }

    // move text position and set leading
    COSNumber y = (COSNumber) arguments.get(1);

    ArrayList<COSBase> args = new ArrayList<COSBase>();
    args.add(new COSFloat(-1 * y.floatValue()));
    context.processOperator("TL", args);
    context.processOperator("Td", arguments);
  }

  @Override
  public String getName() {
    return "TD";
  }
}
