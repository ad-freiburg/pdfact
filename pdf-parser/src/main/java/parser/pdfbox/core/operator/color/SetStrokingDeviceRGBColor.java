package parser.pdfbox.core.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.contentstream.operator.Operator;

import java.io.IOException;
import java.util.List;

/**
 * RG: Set the stroking colour space to DeviceRGB and set the colour to use for stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingDeviceRGBColor extends SetStrokingColor {
  /**
   * RG Set the stroking colour space to DeviceRGB and set the colour to
   * use for stroking operations.
   *
   * @param operator The operator that is being executed.
   * @param arguments List
   * @throws IOException If the color space cannot be read.
   */
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    PDColorSpace cs = context.getResources().getColorSpace(COSName.DEVICERGB);
    context.getGraphicsState().setStrokingColorSpace(cs);
    super.process(operator, arguments);
  }

  @Override
  public String getName() {
    return "RG";
  }
}
