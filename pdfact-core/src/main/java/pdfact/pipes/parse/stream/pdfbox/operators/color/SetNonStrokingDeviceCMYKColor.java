package pdfact.pipes.parse.stream.pdfbox.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import pdfact.model.Page;
import pdfact.model.PdfDocument;

/**
 * k: Set the non-stroking color space to DeviceCMYK and set the color to use
 * for non-stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetNonStrokingDeviceCMYKColor extends SetNonStrokingColor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    PDResources resources = this.engine.getResources();
    // TODO: Use PdfColor here.
    PDColorSpace cs = resources.getColorSpace(COSName.DEVICECMYK);
    this.engine.getGraphicsState().setNonStrokingColorSpace(cs);
    super.process(pdf, page, op, args);
  }

  @Override
  public String getName() {
    return "k";
  }
}
