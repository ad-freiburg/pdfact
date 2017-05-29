package icecite.models.plain;

import icecite.models.PdfElement;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfType;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElement implements PdfElement {
  /**
   * The page in which this PDF element is located.
   */
  protected PdfPage page;

  /**
   * The bounding box of this PDF element.
   */
  protected Rectangle boundingBox;

  /**
   * The role of this PDF element.
   */
  protected PdfRole role;

  // ==========================================================================

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public void setPage(PdfPage page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this.boundingBox;
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    this.boundingBox = boundingBox;
  }

  // ==========================================================================

  @Override
  public PdfRole getRole() {
    return this.role;
  }

  @Override
  public void setRole(PdfRole role) {
    this.role = role;
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return null;
  }
}
