package model;

import org.json.JSONObject;

import de.freiburg.iif.text.StringUtils;

/**
 * Implementation of a PdfParagraph.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutNonTextParagraph extends PdfXYCutArea 
    implements PdfNonTextParagraph {  
  /**
   * The context of this paragraph.
   */
  protected String context;
  
  /**
   * The role of this paragraph.
   */
  protected String role;
  
  /**
   * The default constructor.
   */
  public PdfXYCutNonTextParagraph(PdfPage page) {
    super(page);
  }

  /**
   * The default constructor.
   */
  public PdfXYCutNonTextParagraph(PdfPage page, PdfArea area) {
    this(page);
    
    setTextLines(area.getTextLines());
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.paragraphs;
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
    tsv.append(getFont().getId());
    tsv.append("\t");
    tsv.append(getFontsize());
    tsv.append("\t");
    tsv.append(getColor().getId());
    if (getContext()!= null) {
      tsv.append("\t");
      tsv.append(getContext());
    }
    if (getRole() != null) {
      tsv.append("\t");
      tsv.append(getRole());
    }
    
    return tsv.toString();
  }

  @Override
  public String toXml(int indentLevel, int indentLength) {
    StringBuilder xml = new StringBuilder();
    
    String indent = StringUtils.repeat(" ", indentLevel * indentLength);
    
    xml.append(indent);
    xml.append("<");
    xml.append(getFeature().getField());
    xml.append(" page=\"" + getPage().getPageNumber() + "\"");
    xml.append(" minX=\"" + getRectangle().getMinX() + "\"");
    xml.append(" minY=\"" + getRectangle().getMinY() + "\"");
    xml.append(" maxX=\"" + getRectangle().getMaxX() + "\"");
    xml.append(" maxY=\"" + getRectangle().getMaxY() + "\"");
    xml.append(" font=\"" + getFont().getId() + "\"");
    xml.append(" fontsize=\"" + getFontsize() + "\"");
    xml.append(" color=\"" + getColor().getId() + "\"");
    if (getContext() != null) {
      xml.append(" context=\"" + getContext() + "\"");
    }
    if (getRole() != null) {
      xml.append(" role=\"" + getRole() + "\"");
    }
    xml.append("/>");
    
    return xml.toString();
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    
    json.put("page", getPage().getPageNumber());
    json.put("minX", getRectangle().getMinX());
    json.put("minY", getRectangle().getMinY());
    json.put("maxX", getRectangle().getMaxX());
    json.put("maxY", getRectangle().getMaxY());
    json.put("font", getFont().getId());
    json.put("fontsize", getFontsize());
    json.put("color", getColor().getId());
    if (getContext() != null) {
      json.put("context", getContext());
    }
    if (getRole() != null) {
      json.put("role", getRole());
    }
    
    return json;
  }

  @Override
  public void setContext(String context) {
    this.context = context;
  }

  @Override
  public String getContext() {
    return this.context;
  }
  
  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String getRole() {
    return this.role;
  }

  @Override
  public void addNonTextElement(PdfElement element) {
    this.elementsIndex.insert(element);
    this.nonTextElementsIndex.insert(element);
  }
}
