package model;

import static de.freiburg.iif.math.MathUtils.isEqual;
import static de.freiburg.iif.math.MathUtils.isLarger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
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
  protected PdfRole role = PdfRole.UNKNOWN;

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
  public String getText(boolean includePunctuationMarks,
      boolean includeSubscripts, boolean includeSuperscripts) {
    StringBuilder result = new StringBuilder();

    for (PdfTextLine line : getTextLines()) {
      if (line != null && !line.ignore()) {
        if (result.length() > 0) {
          result.append(" ");
        }
        String text = line.getText(includePunctuationMarks, includeSubscripts,
            includeSuperscripts);
        if (text != null) {
          result.append(text);
        }
      }
    }

    return result.length() > 0 ? result.toString() : null;
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.paragraphs;
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
    for (PdfTextLine line : getTextLines()) {
      line.setRole(role);
    }
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
