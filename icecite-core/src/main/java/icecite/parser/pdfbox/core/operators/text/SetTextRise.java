package icecite.parser.pdfbox.core.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * Ts: Set the text rise to rise, which shall be a number expressed in unscaled
 * text space units.
 * 
 * @author Claudius Korzen
 */
public class SetTextRise extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.isEmpty()) {
      throw new MissingOperandException(op, args);
    }

    COSNumber rise = (COSNumber) args.get(0);
    PDTextState textState = this.engine.getGraphicsState().getTextState();
    textState.setRise(rise.floatValue());
  }

  @Override
  public String getName() {
    return "Ts";
  }
}
