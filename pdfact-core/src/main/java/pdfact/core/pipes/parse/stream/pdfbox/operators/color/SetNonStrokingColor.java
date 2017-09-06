package pdfact.core.pipes.parse.stream.pdfbox.operators.color;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * sc: Sets the color to use for non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColor extends SetColor {
  @Override
  protected void setColor(PDColor color) {
    this.engine.getGraphicsState().setNonStrokingColor(color);
  }

  @Override
  protected PDColorSpace getColorSpace() {
    return this.engine.getGraphicsState().getNonStrokingColorSpace();
  }

  @Override
  public String getName() {
    return "sc";
  }
}
