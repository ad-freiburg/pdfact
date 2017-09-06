package pdfact.core.pipes.parse.stream.pdfbox.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;

/**
 * RG: Set the stroking color space to DeviceRGB and set the color to use for
 * stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingDeviceRGBColor extends SetStrokingColor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    PDResources resources = this.engine.getResources();
    PDColorSpace cs = resources.getColorSpace(COSName.DEVICERGB);
    this.engine.getGraphicsState().setStrokingColorSpace(cs);
    super.process(pdf, page, op, args);
  }

  @Override
  public String getName() {
    return "RG";
  }
}
