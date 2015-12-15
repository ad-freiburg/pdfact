package parser.pdfbox.core.operator.graphics;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * B* Fill and then stroke the path, using the even-odd rule to determine the
 * region to fill.
 * 
 * @author Claudius Korzen
 */
public final class FillEvenOddAndStrokePath extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> operands)
    throws IOException {
    GeneralPath currentPath = (GeneralPath) context.getLinePath().clone();

    context.processOperator("f*", operands);
    context.setLinePath(currentPath);
    context.processOperator("S", operands);
  }

  @Override
  public String getName() {
    return "B*";
  }
}
