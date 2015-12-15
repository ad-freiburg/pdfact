package parser.pdfbox.core.operator.text;

import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Tw: Set the word spacing to wordSpace, which shall be a number expressed in 
 * unscaled text space units. 
 * 
 * @author Claudius Korzen
 */
public class SetWordSpacing extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments) {
    COSNumber wordSpacing = (COSNumber) arguments.get(0);
    PDTextState textState = context.getGraphicsState().getTextState(); 
    textState.setWordSpacing(wordSpacing.floatValue());
  }

  @Override
  public String getName() {
    return "Tw";
  }
}
