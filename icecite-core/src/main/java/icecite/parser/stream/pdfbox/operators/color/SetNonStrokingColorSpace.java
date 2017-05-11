package icecite.parser.stream.pdfbox.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * cs: Sets the non-stroking color space.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColorSpace extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    COSName name = (COSName) args.get(0);

    PDColorSpace cs = this.engine.getResources().getColorSpace(name);
    this.engine.getGraphicsState().setNonStrokingColorSpace(cs);
    this.engine.getGraphicsState().setNonStrokingColor(cs.getInitialColor());
  }

  @Override
  public String getName() {
    return "cs";
  }
}
