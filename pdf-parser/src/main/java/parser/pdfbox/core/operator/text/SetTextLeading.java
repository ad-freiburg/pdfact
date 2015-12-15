package parser.pdfbox.core.operator.text;

import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * TL: Set the text leading to leading, which shall be a number expressed in
 * unscaled text space units. 
 * 
 * @author Claudius Korzen
 */
public class SetTextLeading extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws MissingOperandException {
    if (arguments.isEmpty()) {
      throw new MissingOperandException(operator, arguments);
    }
    
    COSNumber leading = (COSNumber) arguments.get(0);
    PDTextState textState = context.getGraphicsState().getTextState();
    textState.setLeading(leading.floatValue());
  }

  @Override
  public String getName() {
    return "TL";
  }
}
