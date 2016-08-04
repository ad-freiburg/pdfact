package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                  
    // Put all the subscripts and all the superscripts in an individual
    // string builder to avoid to mix up both types.
    List<PdfCharacter> subScriptText = new ArrayList<>();
    List<PdfCharacter> superScriptText = new ArrayList<>();
    List<PdfCharacter> normalScriptText = new ArrayList<>();
       
    for (PdfCharacter character : getTextCharacters()) {      
      if (character == null) {
        continue;
      }
      
      if (character.ignore()) {
        continue;
      }
      
      if (character.isSuperScript()) {
        superScriptText.add(character);
        continue;
      }
      
      if (character.isSubScript()) {
        subScriptText.add(character);
        continue;
      }
                  
      if (!superScriptText.isEmpty() || !subScriptText.isEmpty()) {
        boolean introduceWhitespace = true;
        if (!superScriptText.isEmpty()) {
          boolean includeSuperScript = includeSuperScripts(normalScriptText, subScriptText, superScriptText);
          if (includeSuperScript) {
            for (PdfCharacter superChar : superScriptText) {
              result.append(superChar.getUnicode());  
            }
            introduceWhitespace = false;
          }
        }
        
        if (!subScriptText.isEmpty()) {
          boolean includeSubScript = includeSubScripts(normalScriptText, subScriptText, superScriptText);
          if (includeSubScript) {
            for (PdfCharacter subChar : subScriptText) {
              result.append(subChar.getUnicode());  
            }
          }
          introduceWhitespace = false;
        }
        
        if (introduceWhitespace) {
          // If word is "H²O" and superscript should be ignored, don't merge
          // the word to H0, but to "H 0".
          result.append(" ");
        }
        
        normalScriptText.clear();
      }

      result.append(character.getUnicode());
      normalScriptText.add(character);
      subScriptText.clear();
      superScriptText.clear();
    }
        
    if (!superScriptText.isEmpty() || !subScriptText.isEmpty()) {
      boolean introduceWhitespace = true;
      if (!superScriptText.isEmpty()) {
        if (includeSuperScripts(normalScriptText, subScriptText, superScriptText)) {
          for (PdfCharacter superChar : superScriptText) {
            result.append(superChar.getUnicode());  
          }
          introduceWhitespace = false;
        }
      }
        
      if (!subScriptText.isEmpty()) {
        if (includeSubScripts(normalScriptText, subScriptText, superScriptText)) {
          for (PdfCharacter subChar : subScriptText) {
            result.append(subChar.getUnicode());  
          }
          introduceWhitespace = false;
        }
      }    
       
      if (introduceWhitespace) {
        // If word is "H²O" and superscript should be ignored, don't merge
        // the word to HO, but to "H O".
        result.append(" ");
      }
    }
    
    return result.length() > 0 ? result.toString() : null;
  }

  protected boolean includeSuperScripts(List<PdfCharacter> normalScripts, 
      List<PdfCharacter> subScripts, List<PdfCharacter> superScripts) {
    if (normalScripts == null) {
      return false;
    }
    
    // Don't include the superscript if it is placed "behind" subscript.
    if (!normalScripts.isEmpty() && !subScripts.isEmpty() 
        && !superScripts.isEmpty()) {
      Collections.sort(subScripts, new Comparators.MinXComparator());
      Collections.sort(superScripts, new Comparators.MinXComparator());
      
      PdfCharacter lastSubChar = subScripts.get(subScripts.size() - 1);
      PdfCharacter firstSuperChar = superScripts.get(0);
      
      float lastSubCharMaxX = lastSubChar.getRectangle().getMaxX();
      float firstSuperCharMinX = firstSuperChar.getRectangle().getMinX();
            
      if (firstSuperCharMinX >= lastSubCharMaxX) {
        return false;
      }
    }
    
    if (normalScripts.size() < 3) {
      return true;
    }
            
    // Include superscripts in formulas.
    if (getRole() == PdfRole.FORMULA) {
      return true;
    }
    
    float numChars = normalScripts.size();
    float numLatinChars = 0;
    
    // Count the number of lowercase characters with the most common font.
    for (PdfCharacter character : normalScripts) {      
      if (!Characters.isLatinLetter(character)
          && !Characters.isPunctuationMark(character)) {
        continue;
      }
      
      numLatinChars++;
    }
    
    if (numLatinChars == numChars) {
      return false;
    }
       
    return true;
  }
  
  protected boolean includeSubScripts(List<PdfCharacter> normalScripts, 
      List<PdfCharacter> subScripts, List<PdfCharacter> superScripts) {
    if (normalScripts == null) {
      return false;
    }
    // Don't include the subscript if subscript is placed "behind" superscript.
    if (!normalScripts.isEmpty() && !subScripts.isEmpty() 
        && !superScripts.isEmpty()) {
      Collections.sort(subScripts, new Comparators.MinXComparator());
      Collections.sort(superScripts, new Comparators.MinXComparator());
      
      PdfCharacter firstSubChar = subScripts.get(0);
      PdfCharacter lastSuperChar = superScripts.get(superScripts.size() - 1);
      
      float firstSubCharMinX = firstSubChar.getRectangle().getMinX();
      float lastSuperCharMaxX = lastSuperChar.getRectangle().getMaxX();
      
      if (firstSubCharMinX >= lastSuperCharMaxX) {
        return false;
      }
    }
    
    if (normalScripts.size() < 3) {
      return true;
    }
            
    // Include superscripts in formulas.
    if (getRole() == PdfRole.FORMULA) {
      return true;
    }
    
    float numChars = normalScripts.size();
    float numLatinChars = 0;
    
    // Count the number of lowercase characters with the most common font.
    for (PdfCharacter character : normalScripts) {      
      if (!Characters.isLatinLetter(character)
          && !Characters.isPunctuationMark(character)) {
        continue;
      }
      
      numLatinChars++;
    }
    
    if (numLatinChars == numChars) {
      return false;
    }    
    return true;
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
