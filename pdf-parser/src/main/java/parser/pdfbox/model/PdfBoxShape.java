package parser.pdfbox.model;

import de.freiburg.iif.model.Rectangle;
import model.PdfColor;
import model.PdfFeature;
import model.PdfPage;
import model.PdfRole;
import model.PdfShape;

/**
 * Concrete implementation of a PdfShape using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxShape extends PdfBoxArea implements PdfShape {    
  /**
   * The rectangle of this shape.
   */
  protected Rectangle rectangle;
  
  /**
   * The color of this shape.
   */
  protected PdfBoxColor color;
   
  /**
   * The role of this shape.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
  /**
   * The extraction order number.
   */
  protected int extractionOrderNumber;
  
  /**
   * The default constructor.
   */
  public PdfBoxShape(PdfPage page) {
    super(page);
    
    addShape(this);
  }
  
  // ___________________________________________________________________________
  // Getters and Setters.
      
  @Override
  public PdfColor getColor() {
    return this.color;
  }
  
  /**
   * Sets the color of this shape.
   */
  public void setColor(PdfBoxColor color) {
    this.color = color;
  }
  
  @Override
  public Rectangle getRectangle() {
    return this.rectangle;
  }
  
  /**
   * Sets the rectangle of this shape.
   */
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }
  
  @Override
  public PdfFeature getFeature() {
    return PdfFeature.shapes;
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
