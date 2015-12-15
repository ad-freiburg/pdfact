package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * ": Move to the next line and show a text string, using aw as the word spacing
 * and ac as the character spacing (setting the corresponding parameters in
 * the text state). aw and ac shall be numbers expressed in unscaled text
 * space units. This operator shall have the same effect as this code:
 * aw Tw
 * ac Tc
 * string '
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLineAndShowTextWithSpacing extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.size() < 3) {
      throw new MissingOperandException(operator, arguments);
    }
    
    context.processOperator("Tw", arguments.subList(0, 1));
    context.processOperator("Tc", arguments.subList(1, 2));
    context.processOperator("'", arguments.subList(2, 3));
  }

  @Override
  public String getName() {
    return "\"";
  }
}
