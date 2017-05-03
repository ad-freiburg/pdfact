package parser.pdfbox.core.operator.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * CS: Set color space for stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingColorSpace extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    COSName name = (COSName) arguments.get(0);

    PDColorSpace cs = context.getResources().getColorSpace(name);
    context.getGraphicsState().setStrokingColorSpace(cs);
    context.getGraphicsState().setStrokingColor(cs.getInitialColor());
  }

  @Override
  public String getName() {
    return "CS";
  }
}