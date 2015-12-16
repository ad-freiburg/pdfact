package model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.text.StringUtils;
import statistics.TextLineStatistician;

/**
 * Implementation of a PdfParagraph.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutParagraph extends PdfXYCutTextArea 
    implements PdfParagraph {

  /**
   * The lines in this paragraph.
   */
  protected List<PdfTextLine> lines;

  /**
   * The text line statistics.
   */
  protected TextLineStatistics textLineStatistics;
  
  /**
   * Flag to indicate if the text line statistics is outdated.
   */
  protected boolean isTextLineStatisticsOutdated;
  
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
  public PdfXYCutParagraph(PdfPage page) {
    super(page);
    this.lines = new ArrayList<>();
  }

  /**
   * The default constructor.
   */
  public PdfXYCutParagraph(PdfPage page, PdfArea area) {
    this(page);
    for (HasRectangle element : area.getElements()) {
      addTextLine((PdfTextLine) element);
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.lines;
  }

  /**
   * Sets the words in this line.
   */
  public void setTextLines(List<? extends PdfTextLine> lines) {
    this.lines.clear();
    for (PdfTextLine line : lines) {
      addTextLine(line);
    }
  }

  /**
   * Adds the given word to this line.
   */
  public void addTextLine(PdfTextLine line) {
    this.lines.add(line);
    index(line);
  }
  
  @Override
  public String getUnicode() {
    StringBuilder text = new StringBuilder();

    for (PdfTextLine line : getTextLines()) {
      String lineText = line.getUnicode();
      
      if (!lineText.isEmpty()) {
        PdfWord lastWord = line.getWords().get(line.getWords().size() - 1);
        if (lastWord.isHyphenized()) {
          text.append(lineText.substring(0, lineText.length() - 1));
        } else {
          text.append(lineText);
          text.append(" ");
        }
      }
    }
    
    return text.toString();
  }
  
  /**
   * Returns the text line statistics.
   */
  public TextLineStatistics getTextLineStatistics() {
    if (this.isDimensionStatisticsOutdated) {
      computeTextLineStatistics();
    }
    return this.textLineStatistics;
  }
  
  /**
   * Returns the text line statistics.
   */
  public void computeTextLineStatistics() {
    this.textLineStatistics = TextLineStatistician.compute(getTextLines());
    this.isTextLineStatisticsOutdated = false;
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
}
