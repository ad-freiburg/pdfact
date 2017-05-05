package icecite.parser.pdfbox.core.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * q: Save the current graphics state on the graphics state stack.
 * 
 * @author Claudius Korzen
 */
public class SaveGraphicsState extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    this.engine.saveGraphicsState();
  }

  @Override
  public String getName() {
    return "q";
  }
}
