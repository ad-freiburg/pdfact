package model;

import statistics.DimensionStatistician;
import statistics.TextStatistician;

/**
 * Implementation of a PdfWord. o
 * 
 * @author Claudius Korzen
 */
public class PdfXYCutWord extends PdfXYCutArea implements PdfWord {
  /**
   * Flag that indicates if this word is hyphenized.
   */
  protected boolean isHyphenized;

  /**
   * Flag that indicates if this word contains a superscript.
   */
  protected boolean containsSuperScript;

  /**
   * Flag that indicates if this word contains a subscript.
   */
  protected boolean containsSubScript;

  /**
   * Flag that indicates if this word contains a punctuation mark.
   */
  protected boolean containsPunctuationMark;

  /**
   * The role.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
  protected int extractionOrderNumber;
  
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

    addAnyElements(area.getElements());
  }

  @Override
  public PdfCharacter getFirstTextCharacter() {
    if (getTextCharacters().size() > 0) {
      return getTextCharacters().get(0);
    }
    return null;
  }

  @Override
  public PdfCharacter getLastTextCharacter() {
    if (getTextCharacters().size() > 0) {
      return getTextCharacters().get(getTextCharacters().size() - 1);
    }
    return null;
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.words;
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
  public String toString() {
    return getUnicode();
  }

  @Override
  public String getUnicode() {
    StringBuilder result = new StringBuilder();

    for (PdfCharacter character : getTextCharacters()) {
      if (character != null && !character.ignore()) {
        result.append(character.toString());
      }
    }

    return result.toString();
  }

  @Override
  public String getText(boolean includePunctuationMarks,
      boolean includeSubscripts, boolean includeSuperscripts) {
    StringBuilder result = new StringBuilder();

    String prevText = null;
    for (PdfCharacter character : getTextCharacters()) {
      if (character != null && !character.ignore()) {        
        String text = character.getText(includePunctuationMarks,
            includeSubscripts, includeSuperscripts);
        if (text != null) {
          // Sub- and superscripts are replaced by whitespaces. Append a white-
          // space only if the previous characters is a non-whitespace.
          if (" ".equals(text)) {
            if (prevText != null && !" ".equals(prevText)) {
              result.append(text);
            }
          } else {
            result.append(text);
          }
        }
        prevText = text;
      }
    }

    return result.length() > 0 ? result.toString() : null;
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
  public boolean isHyphenated() {
    return false;
  }

  @Override
  public boolean containsSubScript() {
    return containsSubScript;
  }

  @Override
  public void setContainsSubScript(boolean containsSubscript) {
    this.containsSubScript = containsSubscript;
  }

  @Override
  public boolean containsSuperScript() {
    return containsSuperScript;
  }

  @Override
  public void setContainsSuperScript(boolean containsSuperscript) {
    this.containsSuperScript = containsSuperscript;
  }

  @Override
  public boolean containsPunctuationMark() {
    return containsPunctuationMark;
  }

  @Override
  public void setContainsPunctuationMark(boolean containsPunctuationMark) {
    this.containsPunctuationMark = containsPunctuationMark;
  }

  @Override
  public void setRole(PdfRole role) {
    this.role = role;
    for (PdfCharacter character : getTextCharacters()) {
      character.setRole(role);
    }
  }

  @Override
  public PdfRole getRole() {
    return role;
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
}
