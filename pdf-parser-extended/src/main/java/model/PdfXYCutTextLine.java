package model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import org.json.JSONObject;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.text.StringUtils;
import statistics.DimensionStatistician;
import statistics.TextStatistician;

/**
 * Implementation of a PdfTextLine.
 *
 * @author Claudius Korzen
 */
public class PdfXYCutTextLine extends PdfXYCutArea implements PdfTextLine {
  /**
   * The default constructor.
   */
  public PdfXYCutTextLine(PdfPage page) {
    super(page);
  }
  
  /**
   * The default constructor.
   */
  public PdfXYCutTextLine(PdfPage page, PdfArea area) {
    this(page);
    
    addAnyElements(area.getElements());
  }

  // ___________________________________________________________________________

  /**
   * Returns the unicode of this text line.
   */
  public String getUnicode() {
    return CollectionUtils.join(getWords()); 
  }

  @Override
  public PdfWord getFirstWord() {
    return getWords().size() > 0 ? getWords().get(0) : null;
  }

  @Override
  public PdfWord getLastWord() {
    return getWords().size() > 0 ? getWords().get(getWords().size() - 1) : null;
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

  @Override
  public DimensionStatistics computeDimensionStatistics() {
    return DimensionStatistician.accumulate(getTextCharacters());
  }
  
  @Override
  public TextStatistics computeTextStatistics() {    
    TextStatistics s = TextStatistician.accumulate(getTextCharacters());
    
//    for (PdfCharacter character : getTextCharacters()) {
//      System.out.println(character.getTextStatistics().getMostCommonFont());
//    }
//    System.out.println(s.getMostCommonFont());
    
    return s;
  }
  
  @Override
  public boolean isAscii() {
    for (PdfCharacter character : getTextCharacters()) {
      for (char chaar : character.getUnicode().toCharArray()) {
        if (chaar < 32 || chaar > 126) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isDigit() {
    if (getTextCharacters() == null) {
      return false;
    }

    if (getTextCharacters().isEmpty()) {
      return false;
    }

    for (PdfCharacter character : getTextCharacters()) {
      if (character.getCodePoint() < '0' || character.getCodePoint() > '9') {
        return false;
      }
    }
    return true;
  }
}
