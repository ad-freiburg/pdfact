package icecite.parser.pdfbox.core.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * h: Close path.
 * 
 * @author Claudius Korzen
 */
public class ClosePath extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    this.engine.getLinePath().closePath();
  }

  @Override
  public String getName() {
    return "h";
  }
}
