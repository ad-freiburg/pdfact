package parser.pdfbox.model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import org.apache.pdfbox.util.Matrix;
import org.json.JSONObject;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.text.StringUtils;
import model.PdfCharacter;
import model.PdfFeature;
import model.PdfPage;
import model.TextStatistics;
import statistics.TextStatistician;

/**
 * Concrete implementation of a PdfTextCharacter using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxCharacter extends PdfBoxTextArea implements PdfCharacter {
  /**
   * The rectangle.
   */
  protected Rectangle rectangle;

  /**
   * The textual content of this character in unicode.
   */
  protected String unicode;

  /**
   * The code of the character.
   */
  protected int code;

  /**
   * The color of this character.
   */
  protected PdfBoxColor color;

  /**
   * The font of this character.
   */
  protected PdfBoxFont font;

  /**
   * The font size in pt of this character.
   */
  protected float fontsize;

  /**
   * The extraction order number of this character.
   */
  protected int extractionOrderNumber;

  /**
   * The text rendering matrix. TODO: Do we really need this?
   */
  protected Matrix textRenderingMatrix;

  /**
   * The text statistics about this area.
   */
  protected TextStatistics textStatistics;

  /**
   * The orientation.
   */
  protected float orientation = Float.MAX_VALUE;
  
  // ___________________________________________________________________________
  // Constructor.

  /**
   * Creates an new PdfBoxTextCharacter from given unicode.
   */
  public PdfBoxCharacter(PdfPage page, String unicode) {
    super(page);
    this.unicode = unicode;
  }

  // ___________________________________________________________________________
  // Getters and setters.

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
  public String getUnicode() {
    return unicode;
  }

  @Override
  public PdfBoxColor getColor() {
    return this.color;
  }

  /**
   * Sets the color of this shape.
   */
  public void setColor(PdfBoxColor color) {
    this.color = color;
  }

  @Override
  public PdfBoxFont getFont() {
    return this.font;
  }

  /**
   * Sets the font of this shape.
   */
  public void setFont(PdfBoxFont font) {
    this.font = font;
  }

  /**
   * Returns the character code.
   */
  public int getCharCode() {
    return code;
  }

  /**
   * Sets the character code.
   */
  public void setCharCode(int code) {
    this.code = code;
  }

  /**
   * Returns the font size in pt of this character.
   */
  public float getFontsize() {
    return fontsize;
  }

  /**
   * Sets the font size of this character (in pt).
   */
  public void setFontsize(float fontsize) {
    this.fontsize = fontsize;
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

  /**
   * Returns the text rendering matrix of this character.
   */
  public Matrix getTextRenderingMatrix() {
    return textRenderingMatrix;
  }

  /**
   * Sets the text rendering matrix of this character.
   */
  public void setTextRenderingMatrix(Matrix textRenderingMatrix) {
    this.textRenderingMatrix = textRenderingMatrix;
  }

  @Override
  public TextStatistics getTextStatistics() {
    if (this.textStatistics == null) {
      this.textStatistics = TextStatistician.compute(this);
    }
    return this.textStatistics;
  }

  @Override
  public float getOrientation() {
    if (orientation == Float.MAX_VALUE) {
      orientation = computeOrientation();
    }
    return orientation;
  }
  
  /**
   * Computes the orientation of this character.
   */
  protected float computeOrientation() {
    Matrix trm = getTextRenderingMatrix();
    float xScale = trm.getScaleX();
    float rotation = (float) Math.acos(trm.getValue(0, 0) / xScale);
    if (trm.getValue(0, 1) < 0 && trm.getValue(1, 0) > 0) {
      rotation = (-1) * rotation;
    }
    return (float) (rotation * 180 / Math.PI);
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.characters;
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