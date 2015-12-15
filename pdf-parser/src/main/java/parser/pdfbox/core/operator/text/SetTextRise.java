package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Ts: Set the text rise to rise, which shall be a number expressed in
 * unscaled text space units.
 * 
 * @author Claudius Korzen
 */
public class SetTextRise extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.isEmpty()) {
      throw new MissingOperandException(operator, arguments);
    }
    
    COSNumber rise = (COSNumber) arguments.get(0);
    PDTextState textState = context.getGraphicsState().getTextState();
    textState.setRise(rise.floatValue());
  }

  @Override
  public String getName() {
    return "Ts";
  }
}
