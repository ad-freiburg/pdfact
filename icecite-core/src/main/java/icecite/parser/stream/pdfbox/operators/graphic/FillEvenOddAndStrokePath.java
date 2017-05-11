package icecite.parser.stream.pdfbox.operators.graphic;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * B*: Fill and then stroke the path, using the even-odd rule to determine the
 * region to fill.
 * 
 * @author Claudius Korzen
 */
public class FillEvenOddAndStrokePath extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    GeneralPath currentPath = (GeneralPath) this.engine.getLinePath().clone();

    this.engine.processOperator("f*", args);
    this.engine.setLinePath(currentPath);
    this.engine.processOperator("S", args);
  }

  @Override
  public String getName() {
    return "B*";
  }
}
