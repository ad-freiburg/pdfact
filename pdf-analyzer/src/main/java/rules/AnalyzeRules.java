package rules;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.text.StringUtils;
import model.PdfDocument;
import model.PdfFont;
import model.PdfParagraph;

/**
 * Some assumptions on analyzing a pdf document.
 *
 * @author Claudius Korzen
 *
 */
public class AnalyzeRules {
  /**
   * Returns true, if we assume that the given paragraph represents a heading.
   */
  // TODO: Find more robust criteria to identify a heading.
  public static boolean isHeading(PdfParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }
    
    PdfDocument document = paragraph.getPdfDocument();
    
    if (document == null) {
      return false;
    }
    
    String text = paragraph.getUnicode().toLowerCase();
    text = StringUtils.removeWhitespaces(text);

    float paragraphFontsize = paragraph.getFontsize();
    float documentFontsize = document.getFontsize();
    
    return text.length() > 5 && text.length() <= 50 && 
        paragraphFontsize > documentFontsize;
  }
  
  /**
   * Returns true, if we assume, that the paragraph belongs to the body.
   */
  public static boolean isBodyParagraph(PdfParagraph paragraph) {
    return !isSpecialParagraph(paragraph);
  }
  
  /**
   * Returns true, if the given paragraph should be considered as special.
   * 
   * @param document
   *          the text document.
   * @param page
   *          the page.
   * @param para
   *          the paragraph to analyze.
   * @return true, if the given paragraph should be considered as special.
   */
  public static boolean isSpecialParagraph(PdfParagraph paragraph) {
    // The paragraph is special, if its font isn't equal to the most common
    // font.
    if (!matchesMostCommonFont(paragraph)) {
      return true;
    }
    
    if (isLocatedInNonTextElement(paragraph)) {
      return true;
    }

    if (hasSpecialContext(paragraph)) {
      return true;
    }
    
    if (containsSpecialSymbol(paragraph)) {
      return true;
    }
    
    if (isCaption(paragraph)) {
      return true;
    }
    
    if (isFormula(paragraph)) {
      return true;
    }
    
    return false;
  }
    
  /**
   * Return true, if this paragraph contains a symbol that suggests a special
   * paragraph.
   */
  protected static boolean containsSpecialSymbol(PdfParagraph paragraph) {
    //////TODO: PERFORMANCE!
    // String text = paragraph.getUnicode().toLowerCase();
    // if (text.contains("@") && text.length() < 500) {
    //   return true;
    // }
    // if (text.contains("�") && text.length() < 500) {
    //   return true;
    // }
    // if (text.contains("copyright") && text.length() < 500) {
    //   return true;
    // }
    // if (text.contains("doi:") && text.length() < 500) {
    //   return true;
    // }
    // if (text.contains("issn") && text.length() < 500) {
    //   return true;
    // }
    return false;
  }

  /**
   * Returns true, if we consider this paragraph as a formula.
   */
  protected static boolean isFormula(PdfParagraph paragraph) {
//    // Compute a (experimental) score to identify formulas.
//    float asciiRatio = para.getTextStatistics().getASCIIRatio();
//    float nonDigitsRatio = 1 - para.getTextStatistics().getDigitsRatio();
//    float score = asciiRatio * nonDigitsRatio;
//    if (score < 0.5f) {
//      return true;
//    }
//
//    // Count the latin non-digit characters with a font equal to the document
//    // font.
//    // For formula identification.
//    float numASCIILetters = 0;
//    for (PdfTextCharacter character : para.getIndexElements()) {
//      PdfFont characterFont = character.getTextStatistics().getMostCommonFont();
//      if (character.isASCIILetter() && !character.isDigit()
//          && characterFont == documentFont) {
//        numASCIILetters++;
//      }
//    }
//    float ratioASCIILetters =
//        numASCIILetters / (float) para.getIndexElements().size();
//
//    if (ratioASCIILetters < 0.5) {
//      return true;
//    }
    return false;
  }

  /**
   * Returns true, if we consider this paragraph as a caption.
   */
  protected static boolean isCaption(PdfParagraph paragraph) {
//    // Check, if the paragraph is a caption.
//    String firstLineText =
//        para.getTextLines().get(0).getText().replaceAll(" ", "").toLowerCase();
//
//    Matcher figureCaptionMatcher =
//        FIGURE_CAPTION_PATTERN.matcher(firstLineText);
//    Matcher tableCaptionMatcher = TABLE_CAPTION_PATTERN.matcher(firstLineText);
//    if (figureCaptionMatcher.find() || tableCaptionMatcher.find()) {
//      // Search for non text objects around the paragraph.
//      Rectangle area = new SimpleRectangle(para.getRectangle());
//      area.setMinX(area.getMinX());
//      area.setMaxX(area.getMaxX());
//      area.setMinY(area.getMinY() - 75);
//      area.setMaxY(area.getMaxY() + 75);
//
//      List<PdfNonTextElement> nonTextElements =
//          page.getNonTextElementsIndex().overlappedBy(area);
//      if (!nonTextElements.isEmpty()) {
//        return true;
//      }
//    }
    return false;
  }

  /**
   * Returns true, if we consider this paragraph as paragraph with special 
   * context.
   */
  protected static boolean hasSpecialContext(PdfParagraph paragraph) {
//    if (SPECIAL_PARAGRAPH_HEADINGS_MAP.contains(para.getRole())) {
//      return true;
//    }
    return false;
  }

  /**
   * Returns true, if this paragraph is located in a non-text element.
   */
  protected static boolean isLocatedInNonTextElement(PdfParagraph paragraph) {
//    // TODO: EXPERIMENTAL: Dont consider the paragraph, if it is contained in a
//    // non-text-element.
//    // List<PdfNonTextElement> elements =
//    // page.getNonTextElementsIndex().contain(para.getRectangle());
//    List<PdfNonTextBlock> blocks = page.getNonTextObjectContainer(para);
//    if (!blocks.isEmpty() && page.getFiguresCoverage() < 0.75f) {
//      return true;
//    }
    return false;
  }

  /**
   * Returns true, if the font of the paragraph matches the most common font
   * of the document.
   */
  protected static boolean matchesMostCommonFont(PdfParagraph paragraph) {
    PdfFont paragraphFont = paragraph.getFont();
    PdfDocument document = paragraph.getPdfDocument();
    PdfFont documentFont = document.getTextStatistics().getMostCommonFont();
        
    if (!paragraphFont.getName().equals(documentFont.getName())) {
      return false;
    }

    float paragraphFontsize = paragraph.getFontsize();
    float docFontsize = document.getTextStatistics().getMostCommonFontsize();

    if (!MathUtils.isEqual(paragraphFontsize, docFontsize, 0.5f)) {
      return false;
    }
    return true;
  }
}
