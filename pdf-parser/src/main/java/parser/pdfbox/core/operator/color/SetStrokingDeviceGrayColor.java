package parser.pdfbox.core.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.contentstream.operator.Operator;

import java.io.IOException;
import java.util.List;

/**
 * G: Set the stroking colour space to DeviceGray and set the gray level to 
 * use for stroking operations.
 *
 * @author John Hewson
 */
public class SetStrokingDeviceGrayColor extends SetStrokingColor {
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    PDColorSpace cs = context.getResources().getColorSpace(COSName.DEVICEGRAY);
    context.getGraphicsState().setStrokingColorSpace(cs);
    super.process(operator, arguments);
  }

  @Override
  public String getName() {
    return "G";
  }
}
