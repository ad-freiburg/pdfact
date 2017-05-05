package icecite.parser.pdfbox.core.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * Tw: Set the word spacing to wordSpace, which shall be a number expressed in
 * unscaled text space units.
 * 
 * @author Claudius Korzen
 */
public class SetWordSpacing extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSNumber wordSpacing = (COSNumber) args.get(0);
    PDTextState textState = this.engine.getGraphicsState().getTextState();
    textState.setWordSpacing(wordSpacing.floatValue());
  }

  @Override
  public String getName() {
    return "Tw";
  }
}
