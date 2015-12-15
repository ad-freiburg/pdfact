package model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.freiburg.iif.collections.CollectionsUtil;
import de.freiburg.iif.text.StringUtil;

/**
 * Implementation of a PdfTextLine.
 *
 * @author Claudius Korzen
 */
public class PdfXYCutTextLine extends PdfXYCutTextArea implements PdfTextLine {
  /**
   * The words in this line.
   */
  protected List<PdfWord> words;

  /**
   * The default constructor.
   */
  public PdfXYCutTextLine(PdfPage page) {
    super(page);
    this.words = new ArrayList<>();
  }
  
  /**
   * The default constructor.
   */
  public PdfXYCutTextLine(PdfPage page, PdfArea area) {
    this(page);
    index(area.getElements());
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfWord> getWords() {
    return this.words;
  }

  /**
   * Sets the words in this line.
   */
  public void setWords(List<? extends PdfWord> words) {
    this.words.clear();
    for (PdfWord word : words) {
      this.words.add(word);
      index(word.getElements());
    }
  }

  /**
   * Adds the given word to this line.
   */
  public void addWord(PdfWord word) {
    this.words.add(word);
    index(word);
  }
  
  /**
   * Returns the unicode of this text line.
   */
  public String getUnicode() {
    return CollectionsUtil.join(getWords()); 
  }

  @Override
  public PdfWord getFirstWord() {
    return words.size() > 0 ? words.get(0) : null;
  }

  @Override
  public PdfWord getLastWord() {
    return words.size() > 0 ? words.get(words.size() - 1) : null;
  }
  
  @Override
  public PdfFeature getFeature() {
    return PdfFeature.lines;
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
    
    String indent = StringUtil.repeat(" ", indentLevel * indentLength);
    
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
