package parser.pdfbox.core.operator.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * rg: Set the non-stroking colour space to DeviceRGB and set the colour to use
 * for non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingDeviceRGBColor extends SetNonStrokingColor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    PDColorSpace cs = context.getResources().getColorSpace(COSName.DEVICERGB);
    context.getGraphicsState().setNonStrokingColorSpace(cs);
    super.process(operator, arguments);
  }

  @Override
  public String getName() {
    return "rg";
  }
}
