package parser.pdfbox.model;

import de.freiburg.iif.model.Rectangle;
import model.PdfFeature;
import model.PdfFigure;
import model.PdfPage;
import model.PdfRole;

/**
 * Concrete implementation of a PdfFigure using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxFigure extends PdfBoxArea implements PdfFigure {
  /**
   * The rectangle of this figure.
   */
  protected Rectangle rectangle;

  /**
   * The role.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
  protected int extractionOrderNumber;
  
  /**
   * The default constructor.
   */
  public PdfBoxFigure(PdfPage page) {
    super(page);
    
    addFigure(this);
  }

  @Override
  public Rectangle getRectangle() {
    return this.rectangle;
  }

  /**
   * Sets the rectangle of this character.
   */
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }
  
  @Override
  public PdfFeature getFeature() {
    return PdfFeature.figures;
  }
  
  @Override
  public void setRole(PdfRole role) {
    this.role = role;
  }
  
  @Override
  public PdfRole getRole() {
    return role;
  }
  
  /**
   * Returns the extraction order number of this character.
   */
  public int getExtractionOrderNumber() {
    return extractionOrderNumber;
  }

  /**
   * Sets the extraction order number of this character.
   */
  public void setExtractionOrderNumber(int extractionOrderNumber) {
    this.extractionOrderNumber = extractionOrderNumber;
  }
}
