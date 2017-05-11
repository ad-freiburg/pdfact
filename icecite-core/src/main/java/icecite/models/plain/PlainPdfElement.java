package icecite.models.plain;

import icecite.models.PdfElement;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElement implements PdfElement {
  /**
   * The bounding box of this PDF element.
   */
  protected Rectangle boundingBox;

  // ==========================================================================

  @Override
  public Rectangle getBoundingBox() {
    return this.boundingBox;
  }

  @Override
  public void setBoundingBox(Rectangle boundingBox) {
    this.boundingBox = boundingBox;
  }
}
