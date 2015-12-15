package parser.pdfbox.core.operator.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * cs: Sets the non-stroking color space.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColorSpace extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    COSName name = (COSName) arguments.get(0);

    PDColorSpace cs = context.getResources().getColorSpace(name);
    context.getGraphicsState().setNonStrokingColorSpace(cs);
    context.getGraphicsState().setNonStrokingColor(cs.getInitialColor());
  }

  @Override
  public String getName() {
    return "cs";
  }
}
