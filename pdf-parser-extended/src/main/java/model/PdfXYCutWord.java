package model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import org.json.JSONObject;

import de.freiburg.iif.text.StringUtils;

/**
 * Implementation of a PdfWord.
 *o
 * @author Claudius Korzen
 */
public class PdfXYCutWord extends PdfXYCutTextArea implements PdfWord {
  /** 
   * Flag that indicates if this word is hyphenized.
   */
  protected boolean isHyphenized;
  
  /**
   * The default constructor.
   */
  public PdfXYCutWord(PdfPage page) {
    super(page);
  }
  
  /**
   * The default constructor.
   */
  public PdfXYCutWord(PdfPage page, PdfArea area) {
    this(page);
    index(area.getElements());
  }
    
  @Override
  public boolean isHyphenized() {
    return this.isHyphenized;
  }

  @Override
  public void setIsHyphenized(boolean hyphenized) {
    this.isHyphenized = hyphenized;
  }
  
  @Override
  public PdfFeature getFeature() {
    return PdfFeature.words;
  }

  @Override
  public String toTsv() {
    StringBuilder tsv = new StringBuilder();
    
    tsv.append(getFeature().getField());
    tsv.append("\t");
    tsv.append(getUnicode().replaceAll("\t", " "));
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
    xml.append(">");
    xml.append(escapeXml11(getUnicode()));
    xml.append("</" + getFeature().getField() + ">"); 
    
    return xml.toString();
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    
    json.put("unicode", getUnicode());
    json.put("page", getPage().getPageNumber());
    json.put("minX", getRectangle().getMinX());
    json.put("minY", getRectangle().getMinY());
    json.put("maxX", getRectangle().getMaxX());
    json.put("maxY", getRectangle().getMaxY());
    json.put("font", getFont().getId());
    json.put("fontsize", getFontsize());
    json.put("color", getColor().getId());
    
    return json;
  }
}
