package parser.pdfbox.model;

import org.json.JSONObject;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.text.StringUtil;
import model.PdfColor;
import model.PdfFeature;
import model.PdfPage;
import model.PdfShape;

/**
 * Concrete implementation of a PdfShape using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxShape extends PdfBoxTextArea 
    implements PdfShape {    
  /**
   * The rectangle of this shape.
   */
  protected Rectangle rectangle;
  
  /**
   * The color of this shape.
   */
  protected PdfBoxColor color;
   
  /**
   * The default constructor.
   */
  public PdfBoxShape(PdfPage page) {
    super(page);
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
  public String toTsv() {
    StringBuilder tsv = new StringBuilder();
    
    tsv.append(getFeature().getField());
    tsv.append("\t");
    tsv.append(getPage().getPageNumber());
    tsv.append("\t");
    tsv.append(getRectangle());
    tsv.append("\t");
    tsv.append(getColor().getId());
    
    return tsv.toString();
  }

  @Override
  public String toXml(int indentLevel, int indentLength) {
    StringBuilder xml = new StringBuilder();
    
    String indent = StringUtil.repeat(" ", indentLevel * indentLength);
            
    xml.append(indent);
    xml.append("<");
    xml.append(getFeature().getField());
    xml.append(" minX=\"" + getRectangle().getMinX() + "\"");
    xml.append(" minY=\"" + getRectangle().getMinY() + "\"");
    xml.append(" maxX=\"" + getRectangle().getMaxX() + "\"");
    xml.append(" maxY=\"" + getRectangle().getMaxY() + "\"");
    xml.append(" color=\"" + getColor().getId() + "\"");
    xml.append(" />"); 
    
    return xml.toString();
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    
    json.put("minX", getRectangle().getMinX());
    json.put("minY", getRectangle().getMinY());
    json.put("maxX", getRectangle().getMaxX());
    json.put("maxY", getRectangle().getMaxY());
    json.put("color", getColor().getId());
    
    return json;
  }
}
