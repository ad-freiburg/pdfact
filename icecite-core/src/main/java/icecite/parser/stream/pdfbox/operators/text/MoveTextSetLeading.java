package icecite.parser.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSNumber;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * TD: Move to the start of the next line, offset from the start of the current
 * line by (tx, ty). As a side effect, this operator shall set the leading
 * parameter in the text state. This operator shall have the same effect as
 * this code: -ty TL tx ty Td
 * 
 * @author Claudius Korzen
 */
public class MoveTextSetLeading extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 2) {
      throw new MissingOperandException(op, args);
    }

    // move text position and set leading
    COSNumber y = (COSNumber) args.get(1);

    ArrayList<COSBase> otherArgs = new ArrayList<COSBase>();
    args.add(new COSFloat(-1 * y.floatValue()));
    this.engine.processOperator("TL", otherArgs);
    this.engine.processOperator("Td", args);
  }

  @Override
  public String getName() {
    return "TD";
  }
}
