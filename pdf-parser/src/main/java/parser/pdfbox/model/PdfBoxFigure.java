package parser.pdfbox.model;

import org.json.JSONObject;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.text.StringUtil;
import model.PdfFeature;
import model.PdfFigure;
import model.PdfPage;

/**
 * Concrete implementation of a PdfFigure using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxFigure extends PdfBoxTextArea implements PdfFigure {
  /**
   * The rectangle of this figure.
   */
  protected Rectangle rectangle;

  /**
   * The default constructor.
   */
  public PdfBoxFigure(PdfPage page) {
    super(page);
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
  public String toTsv() {
    StringBuilder tsv = new StringBuilder();
    
    tsv.append(getFeature().getField());
    tsv.append("\t");
    tsv.append(getPage().getPageNumber());
    tsv.append("\t");
    tsv.append(getRectangle());
    
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
    
    return json;
  }
}
