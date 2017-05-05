package icecite.parser.pdfbox.core.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * EMC: end marked content.
 * 
 * @author Claudius Korzen
 */
public class EndMarkedContent extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    // context.endMarkedContent();
  }

  @Override
  public String getName() {
    return "EMC";
  }
}
