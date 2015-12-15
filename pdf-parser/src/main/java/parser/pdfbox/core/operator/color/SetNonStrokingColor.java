package parser.pdfbox.core.operator.color;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * sc: Sets the colour to use for stroking non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColor extends SetColor {
  /**
   * Returns the non-stroking color.
   * 
   * @return The non-stroking color.
   */
  @Override
  protected PDColor getColor() {
    return context.getGraphicsState().getNonStrokingColor();
  }

  /**
   * Sets the non-stroking color.
   * 
   * @param color
   *          The new non-stroking color.
   */
  @Override
  protected void setColor(PDColor color) {
    context.getGraphicsState().setNonStrokingColor(color);
  }

  /**
   * Returns the non-stroking color space.
   * 
   * @return The non-stroking color space.
   */
  @Override
  protected PDColorSpace getColorSpace() {
    return context.getGraphicsState().getNonStrokingColorSpace();
  }

  @Override
  public String getName() {
    return "sc";
  }
}
