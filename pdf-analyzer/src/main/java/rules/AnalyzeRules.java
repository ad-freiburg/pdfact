package rules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.text.StringUtils;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfFont;
import model.PdfNonTextParagraph;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfTextParagraph;

/**
 * Some assumptions on analyzing a pdf document.
 *
 * @author Claudius Korzen
 */
public class AnalyzeRules {
  /**
   * Returns true, if we assume that the given paragraph represents a heading.
   */
  // TODO: Find more robust criteria to identify a heading.
  public static boolean isHeading(PdfTextParagraph paragraph) {
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

    return text.length() > 5 && text.length() <= 50 
        && paragraphFontsize > documentFontsize;
  }

  /**
   * Returns true, if we assume, that the paragraph belongs to the body.
   */
  public static boolean isBodyParagraph(PdfTextParagraph paragraph) {
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
  public static boolean isSpecialParagraph(PdfTextParagraph paragraph) {
    if (!matchesMostCommonFont(paragraph)) {
      return true;
    }

    if (isLocatedInNonTextBlock(paragraph)) {
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

  // ___________________________________________________________________________

  /**
   * Returns true, if the font of the paragraph matches the most common font of
   * the document.
   */
  public static boolean matchesMostCommonFont(PdfTextParagraph paragraph) {
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

  /**
   * Returns true, if this paragraph is located in a non-text element.
   */
  public static boolean isLocatedInNonTextBlock(PdfTextParagraph paragraph) {
    PdfDocument document = paragraph.getPdfDocument();
    PdfPage page = paragraph.getPage();

    List<PdfNonTextParagraph> nonTextParagraphs =
        page.getNonTextParagraphsSurrounding(paragraph);

    return (!isImageBased(document) && !nonTextParagraphs.isEmpty());
  }

  /**
   * Returns true, if we consider this paragraph as paragraph with special
   * context.
   */
  public static boolean hasSpecialContext(PdfTextParagraph paragraph) {
    return SPECIAL_PARAGRAPH_HEADINGS_MAP.contains(paragraph.getContext());
  }

  /**
   * Return true, if this paragraph contains a symbol that suggests a special
   * paragraph.
   */
  public static boolean containsSpecialSymbol(PdfTextParagraph paragraph) {
    String text = paragraph.getUnicode().toLowerCase();
    return StringUtils.containsAny(text, "@", "©", "copyright", "doi:", "issn");
  }

  /**
   * Returns true, if we consider this paragraph as a caption.
   */
  public static boolean isCaption(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    PdfTextLine firstLine = paragraph.getFirstTextLine();
    if (firstLine == null) {
      return false;
    }

    String firstLineText = firstLine.getUnicode();
    if (firstLineText == null) {
      return false;
    }

    firstLineText = firstLineText.replaceAll(" ", "").toLowerCase();
    PdfPage page = paragraph.getPage();

    Matcher figCaptionMatcher = FIGURE_CAPTION_PATTERN.matcher(firstLineText);
    Matcher tableCaptionMatcher = TABLE_CAPTION_PATTERN.matcher(firstLineText);

    if (figCaptionMatcher.find() || tableCaptionMatcher.find()) {
      // Search for non text objects around the paragraph.
      Rectangle r = new SimpleRectangle(paragraph.getRectangle());
      r.setMinX(r.getMinX());
      r.setMaxX(r.getMaxX());
      r.setMinY(r.getMinY() - 75); // TODO
      r.setMaxY(r.getMaxY() + 75); // TODO

      List<PdfNonTextParagraph> paras = page.getNonTextParagraphsOverlapping(r);

      return !paras.isEmpty();
    }
    return false;
  }

  /**
   * Returns true, if we consider this paragraph as a formula.
   */
  public static boolean isFormula(PdfTextParagraph paragraph) {
    // Compute a (experimental) score to identify formulas.
    float asciiRatio = paragraph.getTextStatistics().getAsciiRatio();
    float nonDigitsRatio = 1 - paragraph.getTextStatistics().getDigitsRatio();
    float score = asciiRatio * nonDigitsRatio;
    if (score < 0.5f) { // TODO
      return true;
    }

    // Count the latin non-digit characters with a font equal to the document
    // font.
    // For formula identification.
    float numASCIILetters = 0;
    float numCharacters = paragraph.getTextCharacters().size();
    PdfDocument document = paragraph.getPdfDocument();
    PdfFont documentFont = document.getTextStatistics().getMostCommonFont();
    
    for (PdfCharacter character : paragraph.getTextCharacters()) {
      PdfFont characterFont = character.getFont();
      if (character.isAscii() && !character.isDigit() 
          && characterFont == documentFont) {
        numASCIILetters++;
      }
    }
    
    float ratioASCIILetters = numASCIILetters / numCharacters;
    if (ratioASCIILetters < 0.5) { // TODO
      return true;
    }
    return false;
  }

  /**
   * Returns true, if the given document is image based (OCR).
   */
  public static boolean isImageBased(PdfDocument document) {
    if (document == null) {
      return false;
    }
    
    float numImageBasedPages = 0;
    float numPages = document.getPages().size();
    
    if (numPages > 0) {
      for (PdfPage page : document.getPages()) {
        numImageBasedPages += isImageBased(page) ? 1 : 0;
      }
      
      return numImageBasedPages / numPages > 0.75f; // TODO
    }
    return false;
  }
  
  /**
   * Returns true, if the given page is image based (OCR).
   */
  public static boolean isImageBased(PdfPage page) {
    return page.getFiguresCoverage() > 0.75f; // TODO
  }
  
  /**
   * Pattern to find figure captions.
   */
  protected static final Pattern FIGURE_CAPTION_PATTERN = Pattern.compile(
      "^(fig(\\.?|ure)|abbildung)\\s*\\d+", Pattern.CASE_INSENSITIVE);

  /**
   * Pattern to find table captions.
   */
  protected static final Pattern TABLE_CAPTION_PATTERN = Pattern.compile(
      "^(table|tabelle)\\s*\\d+(\\.|:)", Pattern.CASE_INSENSITIVE);

  /**
   * Common special paragraph headings.
   */
  protected static final String[] SPECIAL_PARAGRAPH_HEADINGS = {
      "abstract",
      "abbreviations",
      "acknowledgements",
      "acknowledgement",
      "acknowledgment",
      "acknowledgments",
      "affiliations",
      "authors",
      "authoraffiliations",
      "competinginterests",
      "conflictsofinterest",
      "conflictofinterest",
      "conflictofinterests",
      "consent",
      "dates",
      "disclosures",
      "disclosure",
      "ethics",
      "ethicalprerequisite",
      "generalterms",
      "keywords",
      "listofabbreviations",
      "nomenclature",
      "postaladdress",
      "references",
      "referencesandnotes",
      "sourcesofsupport"
  };

  /**
   * The map of common special paragraph headings.
   */
  protected static final HashSet<String> SPECIAL_PARAGRAPH_HEADINGS_MAP =
      new HashSet<String>(Arrays.asList(SPECIAL_PARAGRAPH_HEADINGS));
}
