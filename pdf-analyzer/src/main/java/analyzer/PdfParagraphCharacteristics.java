package analyzer;

import java.util.HashSet;
import java.util.List;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.text.StringUtils;
import model.PdfDocument;
import model.PdfFont;
import model.PdfPage;
import model.PdfTextParagraph;

/**
 * Class to compute some characteristics about the paragraphs of a pdf document.
 * 
 * @author Claudius Korzen
 *
 */
public class PdfParagraphCharacteristics {
  /**
   * All well known (normalized) section headings. 
   */
  static final HashSet<String> WELL_KNOWN_SECTION_HEADINGS = new HashSet<>();
  
  static {
    WELL_KNOWN_SECTION_HEADINGS.add("introduction");
    WELL_KNOWN_SECTION_HEADINGS.add("relatedwork");
    WELL_KNOWN_SECTION_HEADINGS.add("references");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgements");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgement");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgment");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgments");
    WELL_KNOWN_SECTION_HEADINGS.add("referencesandnotes");
    WELL_KNOWN_SECTION_HEADINGS.add("bibliography");
    WELL_KNOWN_SECTION_HEADINGS.add("conclusion");
    WELL_KNOWN_SECTION_HEADINGS.add("concludingremarks");
  }
  
  /**
   * The document to inspect.
   */
  protected PdfDocument document;
   
  /**
   * Flag that indicates if the characteristics were already computed.
   */
  protected boolean isCharacterized;
  
  protected String sectionHeadingMarkup;
  
  /**
   * Creates a new paragraph inspector for the given document.
   */
  public PdfParagraphCharacteristics(PdfDocument document) {
    this.document = document;
  }
  
  protected void characterize() {    
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages();
    if (pages == null) {
      return;
    }
    
    for (PdfPage page : pages) {
      characterize(page.getParagraphs());
    }
  }
  
  protected void characterize(List<PdfTextParagraph> paragraphs) {
    if (paragraphs == null) {
      return;
    }
    
    for (PdfTextParagraph para : paragraphs) {
      if (sectionHeadingMarkup == null && isWellKnownSectionHeading(para)) {
        this.sectionHeadingMarkup = getMarkup(para);
      }
    }
    
    this.isCharacterized = true;
  }
  
  /**
   * Returns true, if the given paragraph is a well known section heading.
   */
  protected boolean isWellKnownSectionHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }
    
    String text = paragraph.getUnicode();
    
    if (text == null) {
      return false;
    }
    
    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);
    
    return WELL_KNOWN_SECTION_HEADINGS.contains(text);
  }
  
  // ___________________________________________________________________________
  // Static methods.
  
  /**
   * Returns the textual markup of the given paragraph.
   */
  public static String getMarkup(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return null;
    }
    
    PdfFont font = paragraph.getFont();
    float fontsize = MathUtils.round(paragraph.getFontsize(), 0);
    
    if (font != null) {
      return font.getFullName() + "-" + fontsize;
    }
    
    return null;
  }
  
  // ___________________________________________________________________________
  
  /**
   * Returns the section heading markup.
   */
  public String getSectionHeadingMarkup() {
    if (!isCharacterized) {
      characterize();
    }
    return this.sectionHeadingMarkup;
  }
}
