package icecite.models.plain;

import icecite.models.PdfColor;
import icecite.models.PdfShape;

/**
 * A plain implementation of {@link PdfShape}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfShape extends PlainPdfElement implements PdfShape {
  /**
   * The color of this shape.
   */
  protected PdfColor color;

  @Override
  public PdfColor getColor() {
    return this.color;
  }

  @Override
  public void setColor(PdfColor color) {
    this.color = color;
  }
}
