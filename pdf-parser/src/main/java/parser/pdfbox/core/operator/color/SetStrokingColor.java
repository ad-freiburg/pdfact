package parser.pdfbox.core.operator.color;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * SC: Sets the colour to use for stroking stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingColor extends SetColor {
  /**
   * Returns the stroking color.
   * 
   * @return The stroking color.
   */
  @Override
  protected PDColor getColor() {
    return context.getGraphicsState().getStrokingColor();
  }

  /**
   * Sets the stroking color.
   * 
   * @param color The new stroking color.
   */
  @Override
  protected void setColor(PDColor color) {
    context.getGraphicsState().setStrokingColor(color);
  }

  /**
   * Returns the stroking color space.
   * 
   * @return The stroking color space.
   */
  @Override
  protected PDColorSpace getColorSpace() {
    return context.getGraphicsState().getStrokingColorSpace();
  }

  @Override
  public String getName() {
    return "SC";
  }
}
