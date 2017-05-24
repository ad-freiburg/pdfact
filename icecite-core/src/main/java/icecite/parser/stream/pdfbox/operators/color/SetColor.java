package icecite.parser.stream.pdfbox.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceColorSpace;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * sc,scn,SC,SCN: Sets the color to use for stroking or non-stroking
 * operations.
 * 
 * @author Claudius Korzen
 */
public abstract class SetColor extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    PDColorSpace cs = getColorSpace();
    if (cs instanceof PDDeviceColorSpace) {
      if (args.size() < cs.getNumberOfComponents()) {
        throw new MissingOperandException(op, args);
      }
    }
    COSArray array = new COSArray();
    array.addAll(args);

    // TODO: Use PdfColor here.
    setColor(new PDColor(array, cs));
  }

  /**
   * Sets either the stroking or non-stroking color value.
   * 
   * @param color
   *        The stroking or non-stroking color value.
   */
  protected abstract void setColor(PDColor color);

  /**
   * Returns either the stroking or non-stroking color space.
   * 
   * @return The stroking or non-stroking color space.
   */
  protected abstract PDColorSpace getColorSpace();
}
