package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import de.freiburg.iif.counter.ObjectCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.text.StringUtils;
import model.Comparators;
import model.Patterns;
import model.PdfDocument;
import model.PdfFont;
import model.PdfPage;
import model.PdfTextParagraph;
import model.PdfWord;

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

  /**
   * All (normalized) headings for an abstract.
   */
  static final HashSet<String> ABSTRACT_HEADINGS = new HashSet<>();

  /**
   * All (normalized) headings for the references.
   */
  static final HashSet<String> REFERENCES_HEADINGS = new HashSet<>();

  /**
   * All (normalized) headings for the acknowledgment.
   */
  static final HashSet<String> ACKNOWLEDGEMENT_HEADINGS = new HashSet<>();
  
  /**
   * All (normalized) headings for the appendix.
   */
  static final HashSet<String> APPENDIX_HEADINGS = new HashSet<>();
  
  /**
   * All (normalized) headings for the table of contents.
   */
  static final HashSet<String> TABLE_OF_CONTENTS_HEADINGS = new HashSet<>();
  
  /**
   * A lot of math symbols.
   */
  static final HashSet<String> MATH_SYMBOLS = new HashSet<>();
  
  static {
    WELL_KNOWN_SECTION_HEADINGS.add("introduction");
    WELL_KNOWN_SECTION_HEADINGS.add("introductionandsummary");
    WELL_KNOWN_SECTION_HEADINGS.add("relatedwork");
    WELL_KNOWN_SECTION_HEADINGS.add("references");
    WELL_KNOWN_SECTION_HEADINGS.add("summary");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgements");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgement");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgment");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowledgments");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowlegement");
    WELL_KNOWN_SECTION_HEADINGS.add("acknowlegements");
    WELL_KNOWN_SECTION_HEADINGS.add("referencesandnotes");
    WELL_KNOWN_SECTION_HEADINGS.add("bibliography");
    WELL_KNOWN_SECTION_HEADINGS.add("conclusion");
    WELL_KNOWN_SECTION_HEADINGS.add("concludingremarks");

    ABSTRACT_HEADINGS.add("abstract");
    
    APPENDIX_HEADINGS.add("appendix");
    APPENDIX_HEADINGS.add("tables");
    APPENDIX_HEADINGS.add("figures");

    REFERENCES_HEADINGS.add("reference");
    REFERENCES_HEADINGS.add("references");
    REFERENCES_HEADINGS.add("bibliography");

    ACKNOWLEDGEMENT_HEADINGS.add("acknowledgements");
    ACKNOWLEDGEMENT_HEADINGS.add("acknowledgement");
    ACKNOWLEDGEMENT_HEADINGS.add("acknowledgment");
    ACKNOWLEDGEMENT_HEADINGS.add("acknowledgments");
    ACKNOWLEDGEMENT_HEADINGS.add("acknowlegement");
    ACKNOWLEDGEMENT_HEADINGS.add("acknowlegements");
    
    TABLE_OF_CONTENTS_HEADINGS.add("content");
    TABLE_OF_CONTENTS_HEADINGS.add("contents");
    TABLE_OF_CONTENTS_HEADINGS.add("tableofcontents");
    
    MATH_SYMBOLS.add("+");
    MATH_SYMBOLS.add("-");
    MATH_SYMBOLS.add("/");
    MATH_SYMBOLS.add("*");
    MATH_SYMBOLS.add("=");
    MATH_SYMBOLS.add(">");
    MATH_SYMBOLS.add("<");
    MATH_SYMBOLS.add("∼");
    MATH_SYMBOLS.add("≃");
    MATH_SYMBOLS.add("%");
    MATH_SYMBOLS.add("(");
    MATH_SYMBOLS.add(")");
    MATH_SYMBOLS.add("0");
    MATH_SYMBOLS.add("1");
    MATH_SYMBOLS.add("2");
    MATH_SYMBOLS.add("3");
    MATH_SYMBOLS.add("4");
    MATH_SYMBOLS.add("5");
    MATH_SYMBOLS.add("6");
    MATH_SYMBOLS.add("7");
    MATH_SYMBOLS.add("8");
    MATH_SYMBOLS.add("9");
    MATH_SYMBOLS.add("α");
    MATH_SYMBOLS.add("β");
    MATH_SYMBOLS.add("γ");
    MATH_SYMBOLS.add("Δ");
    MATH_SYMBOLS.add("δ");
    MATH_SYMBOLS.add("ε");
    MATH_SYMBOLS.add("η");
    MATH_SYMBOLS.add("λ");
    MATH_SYMBOLS.add("μ");
    MATH_SYMBOLS.add("π");
    MATH_SYMBOLS.add("ρ");
    MATH_SYMBOLS.add("σ");
    MATH_SYMBOLS.add("Σ");
    MATH_SYMBOLS.add("τ");
    MATH_SYMBOLS.add("φ");
    MATH_SYMBOLS.add("χ");
    MATH_SYMBOLS.add("Φ");
    MATH_SYMBOLS.add("ω");
    MATH_SYMBOLS.add("Ω");
  }

  /**
   * The document to inspect.
   */
  protected PdfDocument document;

  /**
   * Flag that indicates if the characteristics were already computed.
   */
  protected boolean isCharacterized;

  /**
   * The common markup of section headings.
   */
  protected String sectionHeadingMarkup;

  /**
   * The common font of section headings.
   */
  protected PdfFont sectionHeadingFont;
  
  /**
   * The area of page headers.
   */
  protected Rectangle pageHeaderArea;

  /**
   * The area of page headers.
   */
  protected Rectangle pageFooterArea;

  /**
   * The counter for words of the pdf document.
   */
  protected ObjectCounter<String> documentWordsCounter;

  /**
   * The counter for hyphenated s of the pdf document.
   */
  protected ObjectCounter<String> hyphenatedWordsCounter;
  
  /**
   * Creates a new paragraph inspector for the given document.
   */
  public PdfParagraphCharacteristics(PdfDocument document) {
    this.document = document;
    this.documentWordsCounter = new ObjectCounter<>();
    this.hyphenatedWordsCounter = new ObjectCounter<>();
  }

  protected void characterize() {
    if (document == null) {
      return;
    }

    characterize(document.getPages());
  }

  protected void characterize(List<PdfPage> pages) {
    if (pages == null) {
      return;
    }

    List<Rectangle> potentialPageHeaders = new ArrayList<>();
    List<Rectangle> potentialPageFooters = new ArrayList<>();

    int numPagesToConsider = 0;
    
    for (PdfPage page : pages) {
      List<PdfTextParagraph> paragraphs = page.getParagraphs();

      if (paragraphs != null && !paragraphs.isEmpty()) {
        float minMinY = Float.MAX_VALUE;
        float maxMaxY = -Float.MAX_VALUE;
        Rectangle topMost = null;
        Rectangle lowerMost = null;
        
        // Find lower most and top most paragraph.
        for (PdfTextParagraph paragraph : paragraphs) {
          if (paragraph.getRectangle().getMinY() < minMinY
              && paragraph.getTextLines().size() < 3) {
            minMinY = paragraph.getRectangle().getMinY();
            lowerMost = new SimpleRectangle(paragraph.getRectangle());
          }
          if (paragraph.getRectangle().getMaxY() > maxMaxY
              && paragraph.getTextLines().size() < 3) {
            maxMaxY = paragraph.getRectangle().getMaxY();
            topMost = new SimpleRectangle(paragraph.getRectangle());
          }
        }
        
        if (topMost != null) {
          // Extend the area over whole page width.
          topMost.getRectangle().setMinX(page.getRectangle().getMinX());
          topMost.getRectangle().setMaxX(page.getRectangle().getMaxX());
          potentialPageHeaders.add(topMost);
        }

        if (lowerMost != null) {
          // Extend the area over whole page width.
          lowerMost.getRectangle().setMinX(page.getRectangle().getMinX());
          lowerMost.getRectangle().setMaxX(page.getRectangle().getMaxX());
          potentialPageFooters.add(lowerMost);
        }

        for (PdfTextParagraph para : paragraphs) {
          if (isWellKnownSectionHeading(para)) {
            if (this.sectionHeadingMarkup == null) {
              this.sectionHeadingMarkup = getMarkup(para);
            }  
            if (this.sectionHeadingFont == null) {
              this.sectionHeadingFont = para.getFont();
            }
          }

          for (PdfWord word : para.getWords()) {
            String wordStr =
                StringUtils.normalize(word.getUnicode(), false, false, true);
            // System.out.println(wordStr);
            if (!StringUtils.isStopWord(wordStr)) {
              documentWordsCounter.add(wordStr);
            }
            
            // Check if the word contains a hyphen somewhere in the middle.
            int indexOfHyphen = wordStr.indexOf("-");
            if (indexOfHyphen > 0 && indexOfHyphen < wordStr.length() - 1) {
              // Add the prefix + first character behind the hyphen.
              // Example: For the word "sugar-free", add "sugar-f"
              hyphenatedWordsCounter.add(wordStr.substring(0, indexOfHyphen + 1));
            }
          }
        }
        numPagesToConsider++;
      }
    }

    if (!potentialPageHeaders.isEmpty()) {
      Collections.sort(potentialPageHeaders,
          Collections.reverseOrder(new Comparators.MaxYComparator()));

      Rectangle pageHeaderArea = potentialPageHeaders.get(0);
      int numPageHeaderMembers = 1;

      for (int i = 1; i < potentialPageHeaders.size(); i++) {
        Rectangle potentialPageHeaderRect = potentialPageHeaders.get(i);
        if (pageHeaderArea.overlaps(potentialPageHeaderRect)
            && MathUtils.isEqual(potentialPageHeaderRect.getHeight(),
                pageHeaderArea.getHeight(),
                0.1f * pageHeaderArea.getHeight())) {
          pageHeaderArea = pageHeaderArea.union(potentialPageHeaderRect);
          numPageHeaderMembers++;
        } else {
          break;
        }
      }

      if (pages.size() < 5) {
        if (numPageHeaderMembers > 0.75f * numPagesToConsider) {
          this.pageHeaderArea = pageHeaderArea;
        }
      } else {
        if (numPageHeaderMembers > 0.5f * numPagesToConsider) {
          this.pageHeaderArea = pageHeaderArea;
        }
      }
    }

    if (!potentialPageFooters.isEmpty()) {
      Collections.sort(potentialPageFooters, new Comparators.MaxYComparator());

      Rectangle pageFooterArea = potentialPageFooters.get(0);
      int numPageFooterMembers = 1;

      for (int i = 1; i < potentialPageFooters.size(); i++) {
        Rectangle potentialPageFooterRect = potentialPageFooters.get(i);
        
        if (pageFooterArea.overlaps(potentialPageFooterRect)
            && MathUtils.isEqual(potentialPageFooterRect.getHeight(), 
                pageFooterArea.getHeight(), 
                0.1f * pageFooterArea.getHeight())) {
          pageFooterArea = pageFooterArea.union(potentialPageFooterRect);
          numPageFooterMembers++;
        }
      }
      
      if (pages.size() < 5) {
        if (numPageFooterMembers > 0.75f * numPagesToConsider) {
          this.pageFooterArea = pageFooterArea;
        }
      } else {
        if (numPageFooterMembers > 0.5f * numPagesToConsider) {
          this.pageFooterArea = pageFooterArea;
        }
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
    
    List<Pattern> itemizeStartPatterns = Patterns.ITEMIZE_START_PATTERNS;
    
    for (Pattern pattern : itemizeStartPatterns) {
      // Remove roman numerals.
      text = pattern.matcher(text).replaceFirst("");
    }
    
    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(text, true, true, true);
        
    return WELL_KNOWN_SECTION_HEADINGS.contains(text);
  }

  /**
   * Returns true, if the given paragraph is the heading of an abstract.
   */
  public boolean isAbstractHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    String text = paragraph.getUnicode();

    if (text == null) {
      return false;
    }

    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);
    
    return ABSTRACT_HEADINGS.contains(text);
  }

  /**
   * Returns true, if the given paragraph is the heading of an abstract.
   */
  public boolean isReferencesHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    String text = paragraph.getUnicode();

    if (text == null) {
      return false;
    }

    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);

    return REFERENCES_HEADINGS.contains(text);
  }
  
  /**
   * Returns true, if the given paragraph is the heading of an appendix.
   */
  public boolean isAppendixHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    String text = paragraph.getUnicode();

    if (text == null) {
      return false;
    }

    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);

    return APPENDIX_HEADINGS.contains(text);
  }
  
  /**
   * Returns true, if the given paragraph is the heading of acknowledgement.
   */
  public boolean isAcknowledgmentHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    String text = paragraph.getUnicode();

    if (text == null) {
      return false;
    }

    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);

    return ACKNOWLEDGEMENT_HEADINGS.contains(text);
  }

  /**
   * Returns true, if the given paragraph is the heading of table of contents.
   */
  public boolean isTableOfContentsHeading(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    String text = paragraph.getUnicode();

    if (text == null) {
      return false;
    }

    // Remove numbers, remove whitespaces and transform to lowercases.
    text = StringUtils.normalize(paragraph.getUnicode(), true, true, true);

    return TABLE_OF_CONTENTS_HEADINGS.contains(text);
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

  /**
   * Returns the section heading markup.
   */
  public PdfFont getSectionHeadingFont() {
    if (!isCharacterized) {
      characterize();
    }
    return this.sectionHeadingFont;
  }
  
  /**
   * Returns the page header area.
   */
  public Rectangle getPageHeaderArea() {
    if (!isCharacterized) {
      characterize();
    }
    return this.pageHeaderArea;
  }

  /**
   * Returns the page footer area.
   */
  public Rectangle getPageFooterArea() {
    if (!isCharacterized) {
      characterize();
    }
    return this.pageFooterArea;
  }

  /**
   * Returns the occurrence of the given word in the pdf document.
   */
  public int getOccurrence(String word) {
    if (!isCharacterized) {
      characterize();
    }
    return this.documentWordsCounter.get(word);
  }
}
