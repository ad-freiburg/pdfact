package parser.pdfbox.core.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.contentstream.operator.Operator;

import java.io.IOException;
import java.util.List;

/**
 * K: Set the stroking colour space to DeviceCMYK and set the colour to use for stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingDeviceCMYKColor extends SetStrokingColor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    PDColorSpace cs = context.getResources().getColorSpace(COSName.DEVICECMYK);
    context.getGraphicsState().setStrokingColorSpace(cs);
    super.process(operator, arguments);
  }

  @Override
  public String getName() {
    return "K";
  }
}
