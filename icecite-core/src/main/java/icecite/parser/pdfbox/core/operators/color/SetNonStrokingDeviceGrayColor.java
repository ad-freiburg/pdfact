package icecite.parser.pdfbox.core.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * g: Set the non-stroking color space to DeviceGray and set the gray level to
 * use for non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingDeviceGrayColor extends SetNonStrokingColor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    PDResources resources = this.engine.getResources();
    PDColorSpace cs = resources.getColorSpace(COSName.DEVICEGRAY);
    this.engine.getGraphicsState().setNonStrokingColorSpace(cs);
    super.process(op, args);
  }

  @Override
  public String getName() {
    return "g";
  }
}