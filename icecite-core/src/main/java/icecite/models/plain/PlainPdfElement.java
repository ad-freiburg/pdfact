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
   * The bounding box of this PDF element.
   */
  protected Rectangle boundingBox;

  /**
   * The page in which this element is included.
   */
  protected PdfPage page;

  /**
   * The role of this PDF element.
   */
  protected PdfRole role;
  
  // ==========================================================================

  @Override
  public Rectangle getBoundingBox() {
    return this.boundingBox;
  }

  @Override
  public void setBoundingBox(Rectangle boundingBox) {
    this.boundingBox = boundingBox;
  }

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
