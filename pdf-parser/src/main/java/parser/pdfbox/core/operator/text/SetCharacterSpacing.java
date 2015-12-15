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
 * Tc: Set the character spacing to charSpace, which shall be a number
 * expressed in unscaled text space units. 
 * 
 * @author Claudius Korzen
 */
public class SetCharacterSpacing extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.isEmpty()) {
      throw new MissingOperandException(operator, arguments);
    }

    // there are some documents which are incorrectly structured, and have
    // a wrong number of arguments to this, so we will assume the last argument
    // in the list
    Object charSpacing = arguments.get(arguments.size() - 1);
    if (charSpacing instanceof COSNumber) {
      COSNumber characterSpacing = (COSNumber) charSpacing;
      PDTextState textState = context.getGraphicsState().getTextState();
      textState.setCharacterSpacing(characterSpacing.floatValue());
    }
  }

  @Override
  public String getName() {
    return "Tc";
  }
}