package model;

import de.freiburg.iif.model.Line;
import statistics.DimensionStatistician;
import statistics.TextStatistician;

/**
 * Implementation of a PdfTextLine.
 *
 * @author Claudius Korzen
 */
public class PdfXYCutTextLine extends PdfXYCutArea implements PdfTextLine {
  /**
   * The mean line of this text line.
   */
  protected Line meanLine;
  /**
   * The base line of this text line.
   */
  protected Line baseLine;
  /**
   * The role.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
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

  @Override
  public String toString() {
    return getUnicode();
  }

  /**
   * Returns the unicode of this text line.
   */
  public String getUnicode() {
    StringBuilder result = new StringBuilder();

    for (PdfWord word : getWords()) {
      if (word != null && !word.ignore()) {
        if (result.length() > 0) {
          result.append(" ");
        }
        result.append(word.toString());
      }
    }

    return result.toString();
  }

  @Override
  public String getText(boolean includePunctuationMarks,
      boolean includeSubscripts, boolean includeSuperscripts) {
    StringBuilder result = new StringBuilder();

    for (PdfWord word : getWords()) {
      if (word != null && !word.ignore()) {
        if (result.length() > 0) {
          result.append(" ");
        }
        String text = word.getText(includePunctuationMarks, includeSubscripts,
            includeSuperscripts);
        if (text != null) {
          result.append(text);
        }
      }
    }

    return result.length() > 0 ? result.toString() : null;
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
  public DimensionStatistics computeDimensionStatistics() {
    return DimensionStatistician.accumulate(getTextCharacters());
  }

  @Override
  public TextStatistics computeTextStatistics() {
    return TextStatistician.accumulate(getTextCharacters());
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
  public Line getMeanLine() {
    return meanLine;
  }

  @Override
  public void setMeanLine(Line meanLine) {
    this.meanLine = meanLine;
  }

  @Override
  public Line getBaseLine() {
    return baseLine;
  }

  @Override
  public void setBaseLine(Line baseLine) {
    this.baseLine = baseLine;
  }

  @Override
  public void setRole(PdfRole role) {
    this.role = role;
    for (PdfWord word : getWords()) {
      word.setRole(role);
    }
  }

  @Override
  public PdfRole getRole() {
    return role;
  }
}
