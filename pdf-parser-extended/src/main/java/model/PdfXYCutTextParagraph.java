package model;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import static de.freiburg.iif.math.MathUtils.isEqual;
import static de.freiburg.iif.math.MathUtils.isLarger;
import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.text.StringUtils;
import statistics.DimensionStatistician;
import statistics.TextLineStatistician;
import statistics.TextStatistician;

/**
 * Implementation of a PdfParagraph.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutTextParagraph extends PdfXYCutArea
    implements PdfTextParagraph {
  /**
   * The context of this paragraph.
   */
  protected String context;

  /**
   * The role of this paragraph.
   */
  protected PdfRole role;

  protected Set<PdfTextAlignment> alignmentVariants;

  /**
   * The default constructor.
   */
  public PdfXYCutTextParagraph(PdfPage page) {
    super(page);
    this.alignmentVariants = new HashSet<>();
    this.alignmentVariants.addAll(Arrays.asList(PdfTextAlignment.values()));
  }

  /**
   * The default constructor.
   */
  public PdfXYCutTextParagraph(PdfPage page, PdfArea area) {
    this(page);
    for (HasRectangle element : area.getElements()) {
      addTextLine((PdfTextLine) element);
    }
  }

  /**
   * Sets the words in this line.
   */
  public void setTextLines(List<? extends PdfTextLine> lines) {
    this.textLinesIndex.clear();
    for (PdfTextLine line : lines) {
      addTextLine(line);
    }
  }

  /**
   * Adds the given word to this line.
   */
  public void addTextLine(PdfTextLine line) {
    PdfTextAlignment lineAlignment = computeTextAlignment(line);

    this.elementsIndex.insert(line.getTextCharacters());
    this.textElementsIndex.insert(line);
    this.textLinesIndex.insert(line);
    this.wordsIndex.insert(line.getWords());
    this.charactersIndex.insert(line.getTextCharacters());
    this.isDimensionStatisticsOutdated = true;
    this.isTextStatisticsOutdated = true;
    this.isTextLineStatisticsOutdated = true;

    if (lineAlignment != null) {
      switch (lineAlignment) {
        case CENTERED:
          alignmentVariants.remove(PdfTextAlignment.RIGHT);
          alignmentVariants.remove(PdfTextAlignment.LEFT);
          break;
        case RIGHT:
          alignmentVariants.remove(PdfTextAlignment.LEFT);
          alignmentVariants.remove(PdfTextAlignment.CENTERED);
          break;
        case LEFT:
          alignmentVariants.remove(PdfTextAlignment.CENTERED);
          alignmentVariants.remove(PdfTextAlignment.RIGHT);
          break;
        default:
          break;
      }
    }
  }

  @Override
  public String toString() {
    return getUnicode();
  }
  
  /**
   * Returns the unicode of this text line.
   */
  public String getUnicode() {
    StringBuilder result = new StringBuilder();

    for (PdfTextLine line : getTextLines()) {
      if (line != null && !line.ignore()) {
        if (result.length() > 0) {
          result.append(" ");
        }
        result.append(line.toString());
      }
    }

    return result.toString();
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
    if (getContext() != null) {
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
  public void setRole(PdfRole role) {
    this.role = role;
  }

  @Override
  public PdfRole getRole() {
    return this.role;
  }

  @Override
  public PdfTextLine getFirstTextLine() {
    return !getTextLines().isEmpty() ? getTextLines().get(0) : null;
  }

  @Override
  public PdfTextLine getLastTextLine() {
    return !getTextLines().isEmpty() ? getTextLines().get(
        getTextLines().size() - 1) : null;
  }

  @Override
  public DimensionStatistics computeDimensionStatistics() {
    return DimensionStatistician.accumulate(getTextLines());
  }

  @Override
  public TextStatistics computeTextStatistics() {
    return TextStatistician.accumulate(getTextLines());
  }

  @Override
  public TextLineStatistics computeTextLineStatistics() {
    return TextLineStatistician.compute(getTextLines());
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

  @Override
  public Set<PdfTextAlignment> getTextAlignmentVariants() {
    return this.alignmentVariants;
  }

  public PdfTextAlignment computeTextAlignment(PdfTextLine line) {
    PdfTextAlignment lineAlignment = null;
    Rectangle rect = getRectangle();
    Rectangle lineRect = line.getRectangle();

    float tolerance = 0.5f * getDimensionStatistics().getAverageWidth();

    if (rect != null && line.getRectangle() != null) {
      float leftMargin = lineRect.getMinX() - rect.getMinX();
      float rightMargin = rect.getMaxX() - lineRect.getMaxX();
      float absLeftMargin = Math.abs(leftMargin);
      float absRightMargin = Math.abs(rightMargin);

      if (isEqual(absRightMargin, absLeftMargin, tolerance)
          && absLeftMargin > tolerance) {
        lineAlignment = PdfTextAlignment.CENTERED;
      } else if (isLarger(leftMargin, rightMargin, tolerance)) {
        lineAlignment = PdfTextAlignment.RIGHT;
      } else {
        lineAlignment = PdfTextAlignment.LEFT;
      }
    }

    return lineAlignment;
  }
}
