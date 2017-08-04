package pdfact.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import pdfact.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * Tz: Set the horizontal scaling, to (scale / 100). scale shall be a number
 * specifying the percentage of the normal width. Initial value: 100
 * 
 * @author Claudius Korzen
 */
public class SetTextHorizontalScaling extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 1) {
      throw new MissingOperandException(op, args);
    }

    COSNumber scaling = (COSNumber) args.get(0);
    PDTextState textState = this.engine.getGraphicsState().getTextState();
    textState.setHorizontalScaling(scaling.floatValue());
  }

  @Override
  public String getName() {
    return "Tz";
  }
}
