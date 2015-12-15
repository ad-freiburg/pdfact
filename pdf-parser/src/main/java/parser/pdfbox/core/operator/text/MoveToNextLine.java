package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * T*: Move to the start of the next line. This operator has the same effect 
 * as the code
 * 0 -Tl Td
 * where Tl denotes the current leading parameter in the text state. The 
 * negative of Tl is used here because Tl is the text leading expressed as a
 * positive number. Going to the next line entails decreasing the y coordinate.
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLine extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    ArrayList<COSBase> args = new ArrayList<COSBase>();
    
    PDTextState textState = context.getGraphicsState().getTextState();
    args.add(new COSFloat(0f));
    args.add(new COSFloat(-1 * textState.getLeading()));
    context.processOperator("Td", args);
  }

  @Override
  public String getName() {
    return "T*";
  }
}
