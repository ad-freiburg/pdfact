package parser.pdfbox.core.operator.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceColorSpace;

import parser.pdfbox.core.operator.OperatorProcessor;


/**
 * sc,scn,SC,SCN: Sets the color to use for stroking or non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public abstract class SetColor extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    PDColorSpace colorSpace = getColorSpace();
    if (colorSpace instanceof PDDeviceColorSpace 
        && arguments.size() < colorSpace.getNumberOfComponents()) {
      throw new MissingOperandException(operator, arguments);
    }
    COSArray array = new COSArray();
    array.addAll(arguments);
    setColor(new PDColor(array, colorSpace));
  }

  /**
   * Returns either the stroking or non-stroking color value.
   * 
   * @return The stroking or non-stroking color value.
   */
  protected abstract PDColor getColor();

  /**
   * Sets either the stroking or non-stroking color value.
   * 
   * @param color
   *          The stroking or non-stroking color value.
   */
  protected abstract void setColor(PDColor color);

  /**
   * Returns either the stroking or non-stroking color space.
   * 
   * @return The stroking or non-stroking color space.
   */
  protected abstract PDColorSpace getColorSpace();
}
