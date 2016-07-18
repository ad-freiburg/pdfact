package analyzer;

import static analyzer.MathSymbols.containsMathSymbol;
import static analyzer.MathSymbols.isMathSymbol;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.text.StringUtils;
import model.Comparators;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFont;
import model.PdfPage;
import model.PdfRole;
import model.PdfShape;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;

/**
 * The concrete implementation of a Pdf Analyzer.
 *
 * @author Claudius Korzen
 *
 */
public class PlainPdfAnalyzer implements PdfAnalyzer {
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
   * Pattern to find lables of formulas.
   */
  protected static final Pattern FORMULA_LABEL_PATTERN = Pattern.compile(
      "\\([0-9]{1,3}\\)$", Pattern.CASE_INSENSITIVE);

  @Override
  public void analyze(PdfDocument document) {
    PdfParagraphCharacteristics characteristics =
        new PdfParagraphCharacteristics(document);

    analyzeForTitle(document, characteristics);
    analyzeForKeywords(document, characteristics);
    analyzeForSectionHeadings(document, characteristics);
    analyzeForPageHeadersAndFooters(document, characteristics);
    analyzeForTables(document, characteristics);
    analyzeForFigures(document, characteristics);
    analyzeForSeparatedFormulas(document, characteristics);
    analyzeForAbstract(document, characteristics);
    analyzeForReferences(document, characteristics);
    analyzeForBody(document, characteristics);
  }

  // ___________________________________________________________________________

  /**
   * Analyzes the document for the title.
   */
  protected void analyzeForTitle(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    if (document == null) {
      return;
    }

    List<PdfPage> pages = document.getPages();
    if (pages == null || pages.isEmpty()) {
      return;
    }

    // Analyze only the first page.
    PdfPage firstPage = pages.get(0);
    float fontsize = document.getTextStatistics().getMostCommonFontsize();

    PdfTextParagraph titleParagraph = null;
    float titleParagraphScore = 0;

    for (PdfTextParagraph paragraph : firstPage.getParagraphs()) {
      if (paragraph.getRole() != PdfRole.UNKNOWN) {
        continue;
      }

      // Don't consider paragraphs with fontsize <= most common fontsize.
      if (paragraph.getFontsize() <= fontsize) {
        continue;
      }

      // Compute score for the paragraph: the average word occurrences.
      int sumOcurrences = 0;
      float numWords = paragraph.getWords().size();

      if (numWords > 0) {
        for (PdfWord word : paragraph.getWords()) {
          String wordStr = word.getUnicode();
          wordStr = StringUtils.normalize(wordStr, false, false, true);

          sumOcurrences += characteristics.getOccurrence(wordStr);
        }

        float score = sumOcurrences / numWords;

        if (score > titleParagraphScore) {
          titleParagraphScore = score;
          titleParagraph = paragraph;
        }
      }
    }

    if (titleParagraph != null) {
      titleParagraph.setRole(PdfRole.TITLE);
    }
  }

  /**
   * Analyzes the document for the title.
   */
  protected void analyzeForKeywords(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    if (document == null) {
      return;
    }

    List<PdfPage> pages = document.getPages();
    if (pages == null || pages.isEmpty()) {
      return;
    }

    // Analyze only the first page.
    PdfPage firstPage = pages.get(0);

    for (PdfTextParagraph paragraph : firstPage.getParagraphs()) {
      if (paragraph.getRole() != PdfRole.UNKNOWN) {
        continue;
      }

      String str = paragraph.getUnicode();
      str = StringUtils.normalize(str, true, true, true);

      if (str.startsWith("keyword")) {
        paragraph.setRole(PdfRole.KEYWORDS);
      }
    }
  }

  // protected void analyzeForPageHeadersAndFooters(PdfDocument document,
  // PdfParagraphCharacteristics characteristics) {
  // for (PdfPage page : document.getPages()) {
  // for (PdfTextParagraph paragraph : page.getParagraphs()) {
  // if (paragraph.getRole() != null) {
  // continue;
  // }
  //
  // Rectangle rect = paragraph.getRectangle();
  //
  // if (rect.overlaps(characteristics.getPageHeaderArea())) {
  // paragraph.setRole(PdfRole.PAGE_HEADER);
  // } else if (rect.overlaps(characteristics.getPageFooterArea())) {
  // paragraph.setRole(PdfRole.PAGE_FOOTER);
  // }
  // }
  // }
  // }

  protected void analyzeForPageHeadersAndFooters(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }

        Rectangle rect = paragraph.getRectangle();
        Rectangle pageHeaderArea = characteristics.getPageHeaderArea();
        Rectangle pageFooterArea = characteristics.getPageFooterArea();

        
        if (pageHeaderArea != null) {
          if (rect.overlaps(pageHeaderArea)
              && MathUtils.isEqual(rect.getHeight(), pageHeaderArea.getHeight(),
                  0.1f * pageHeaderArea.getHeight())) {
            paragraph.setRole(PdfRole.PAGE_HEADER);
          }
        }

        if (pageFooterArea != null) {
          if (rect.overlaps(pageFooterArea)
              && MathUtils.isEqual(rect.getHeight(), pageFooterArea.getHeight(),
                  0.1f * pageFooterArea.getHeight())) {
            paragraph.setRole(PdfRole.PAGE_FOOTER);
          }
        }
      }
    }
  }

  protected void analyzeForSectionHeadings(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    if (document == null) {
      return;
    }

    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }

        // TODO: Move this method to external class.
        String markup = PdfParagraphCharacteristics.getMarkup(paragraph);
        String sectionHeadingMarkup = characteristics.getSectionHeadingMarkup();

        if (characteristics.isAbstractHeading(paragraph)) {
          paragraph.setRole(PdfRole.ABSTRACT_HEADING);
          continue;
        }

        if (characteristics.isReferencesHeading(paragraph)) {
          paragraph.setRole(PdfRole.REFERENCES_HEADING);
          continue;
        }

        if (markup != null && markup.equals(sectionHeadingMarkup)) {
          paragraph.setRole(PdfRole.SECTION_HEADING);
          continue;
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a figure caption.
   */
  protected static void analyzeForTables(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }

        String text = paragraph.getUnicode();

        if (text == null || page == null) {
          continue;
        }

        // Check if the paragraph could be a caption.
        Matcher tableCaptionMatcher = TABLE_CAPTION_PATTERN.matcher(text);
        if (tableCaptionMatcher.find()) {
          // The paragraph is potential caption. To acknowledge, search for a
          // figure above and below the caption.

          // Search for non text elements above the caption (in small excerpt).
          Rectangle above = new SimpleRectangle();
          above.setMinX(paragraph.getRectangle().getMinX());
          above.setMaxX(paragraph.getRectangle().getMaxX());
          above.setMinY(paragraph.getRectangle().getMaxY() + 1);
          above.setMaxY(paragraph.getRectangle().getMaxY() + 75); // TODO
          List<PdfShape> shapesAbove = page.getShapesOverlapping(above);
          int numShapesAbove = shapesAbove.size();

          // Search for non text elements below the caption (in small excerpt).
          Rectangle below = new SimpleRectangle();
          below.setMinX(paragraph.getRectangle().getMinX());
          below.setMaxX(paragraph.getRectangle().getMaxX());
          below.setMinY(paragraph.getRectangle().getMinY() - 75); // TODO
          below.setMaxY(paragraph.getRectangle().getMinY() - 1);
          List<PdfShape> shapesBelow = page.getShapesOverlapping(below);
          int numShapesBelow = shapesBelow.size();

          // Compare the number of non text elements in both areas.
          if (numShapesAbove == 0 && numShapesBelow == 0) {
            // No figures above and below the paragraph. So the paragraph is
            // not a caption.
            continue;
          }

          // There is a figure above or below the paragraph. So the paragraph
          // is a caption.
          paragraph.setRole(PdfRole.TABLE_CAPTION);

          // Try to identify all paragraphs of the figure.
          float width = MathUtils.round(paragraph.getRectangle().getWidth(), 0);

          if (numShapesAbove > numShapesBelow) {
            // Extend the search area above the caption to upper bound of page.
            above.setMaxY(page.getRectangle().getMaxY());

            // Consume all paragraphs that are more smaller than the caption.
            List<PdfTextParagraph> paras = page.getParagraphsOverlapping(above);
            Collections.sort(paras, new Comparators.MinYComparator());

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
              }

              Rectangle rect = para.getRectangle();
              float paraWidth = MathUtils.round(rect.getWidth(), 0);

              // Abort if we see a paragraph that is wider than the caption.
              if (paraWidth >= width) {
                return;
              }

              para.setRole(PdfRole.TABLE);
            }
          } else {
            // Extend the search area below the caption to lower bound of page.
            below.setMinY(page.getRectangle().getMinY());

            // Consume all paragraphs that are more smaller than the caption.
            List<PdfTextParagraph> paras = page.getParagraphsOverlapping(below);

            Collections.sort(paras,
                Collections.reverseOrder(new Comparators.MinYComparator()));

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
              }

              Rectangle rect = para.getRectangle();
              float paraWidth = MathUtils.round(rect.getWidth(), 0);

              // Abort if we see a paragraph that is wider than the caption.
              if (paraWidth >= width) {
                return;
              }

              para.setRole(PdfRole.TABLE);
            }
          }
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a figure caption.
   */
  protected static void analyzeForFigures(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }

        String text = paragraph.getUnicode();

        if (text == null || page == null) {
          continue;
        }
       
        // Check if the paragraph could be a caption.
        Matcher tableCaptionMatcher = FIGURE_CAPTION_PATTERN.matcher(text);
        if (tableCaptionMatcher.find()) {
          
          // The paragraph is potential caption. To acknowledge, search for a
          // figure above and below the caption.

          // Search for non text elements above the caption (in small excerpt).
          Rectangle above = new SimpleRectangle();
          above.setMinX(paragraph.getRectangle().getMinX());
          above.setMaxX(paragraph.getRectangle().getMaxX());
          above.setMinY(paragraph.getRectangle().getMaxY() + 1);
          above.setMaxY(paragraph.getRectangle().getMaxY() + 75); // TODO
          List<? extends PdfElement> nonTextElementsAbove =
              page.getNonTextElementsOverlapping(above);
          int numNonTextElementsAbove = nonTextElementsAbove.size();

          // Search for non text elements below the caption (in small excerpt).
          Rectangle below = new SimpleRectangle();
          below.setMinX(paragraph.getRectangle().getMinX());
          below.setMaxX(paragraph.getRectangle().getMaxX());
          below.setMinY(paragraph.getRectangle().getMinY() - 75); // TODO
          below.setMaxY(paragraph.getRectangle().getMinY() - 1);
          List<? extends PdfElement> nonTextElementsBelow =
              page.getNonTextElementsOverlapping(below);
          int numNonTextElementsBelow = nonTextElementsBelow.size();
          
          // Compare the number of non text elements in both areas.
          if (numNonTextElementsAbove == 0 && numNonTextElementsBelow == 0) {
            // No figures above and below the paragraph. So the paragraph is
            // not a caption.
            continue;
          }

          // There is a figure above or below the paragraph. So the paragraph
          // is a caption.
          paragraph.setRole(PdfRole.FIGURE_CAPTION);

          // Try to identify all paragraphs of the figure.
          float minX = MathUtils.round(paragraph.getRectangle().getMinX(), 1);
          float maxX = MathUtils.round(paragraph.getRectangle().getMaxX(), 1);

          if (numNonTextElementsAbove > numNonTextElementsBelow) {
            // Extend the search area above the caption to upper bound of page.
            above.setMaxY(page.getRectangle().getMaxY());

            // Consume all paragraphs that are smaller than the caption.
            List<PdfTextParagraph> paras = page.getParagraphsOverlapping(above);
            Collections.sort(paras, new Comparators.MinYComparator());

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
              }

              Rectangle rect = para.getRectangle();
              float paraMinX = MathUtils.round(rect.getMinX(), 1);
              float paraMaxX = MathUtils.round(rect.getMaxX(), 1);
              
              // Abort if we see a paragraph that exceeds the left or right 
              // border of the caption.
              if (!MathUtils.isLarger(paraMinX, minX, 1f) 
                  || !MathUtils.isSmaller(paraMaxX, maxX, 1f)) {
                break;
              }

              para.setRole(PdfRole.FIGURE);
            }
          } else {
            // Extend the search area below the caption to lower bound of page.
            below.setMinY(page.getRectangle().getMinY());

            // Consume all paragraphs that are smaller than the caption.
            List<PdfTextParagraph> paras = page.getParagraphsOverlapping(below);
            Collections.sort(paras,
                Collections.reverseOrder(new Comparators.MinYComparator()));

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
              }

              Rectangle rect = para.getRectangle();
              float paraMinX = MathUtils.round(rect.getMinX(), 1);
              float paraMaxX = MathUtils.round(rect.getMaxX(), 1);

              // Abort if we see a paragraph that exceeds the left or right 
              // border of the caption.
              if (!MathUtils.isLarger(paraMinX, minX, 1f) 
                  || !MathUtils.isSmaller(paraMaxX, maxX, 1f)) {
                break;
              }

              para.setRole(PdfRole.FIGURE);
            }
          }
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a formula.
   */
  protected static void analyzeForSeparatedFormulas(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }

        float numNonMathWords = 0;
        float numMathWords = 0;

        for (PdfTextLine line : paragraph.getTextLines()) {
          for (PdfWord word : line.getWords()) {
            String str = word.getUnicode().toLowerCase().trim();

            if (isMathSymbol(str) || containsMathSymbol(str)
                || word.containsSubScript() || word.containsSuperScript()) {
              numMathWords++;
            } else {
              numNonMathWords++;
            }
          }
        }

        float mathWordsRatio = numMathWords / (numMathWords + numNonMathWords);
        if (mathWordsRatio > 0.75f) {
          paragraph.setRole(PdfRole.FORMULA);
          continue;
        }
        
        String text = paragraph.getText(true, true, true);
        
        Matcher m = FORMULA_LABEL_PATTERN.matcher(text);
        // As before, mathWordRatio must exceed a given threshold such that 
        // lines like "After a few manipulations, we now obtain with (35)" 
        // aren't classified as formula
        // formula.
        if (m.find() && mathWordsRatio > 0.5f) {
          paragraph.setRole(PdfRole.FORMULA);
          continue;
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is an abstract.
   */
  protected static void analyzeForAbstract(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    boolean abstractHeaderAlreadySeen = false;
    String abstractMarkup = null;
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (abstractHeaderAlreadySeen) {
          if (paragraph.getRole() != PdfRole.UNKNOWN) {
            return;
          }

          String markup = paragraph.getMarkup();
          if (abstractMarkup != null && abstractMarkup.equals(markup)) {
            return;
          }
          
          paragraph.setRole(PdfRole.ABSTRACT);
          abstractMarkup = paragraph.getMarkup();
        }

        if (paragraph.getRole() == PdfRole.ABSTRACT_HEADING) {
          abstractHeaderAlreadySeen = true;
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is an abstract.
   */
  protected static void analyzeForReferences(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    boolean referencesHeaderAlreadySeen = false;
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (referencesHeaderAlreadySeen) {
          if (paragraph.getRole() == PdfRole.SECTION_HEADING) {
            return;
          }

          if (paragraph.getRole() == PdfRole.UNKNOWN) {
            paragraph.setRole(PdfRole.REFERENCE);
          }
        }

        if (paragraph.getRole() == PdfRole.REFERENCES_HEADING) {
          referencesHeaderAlreadySeen = true;
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is an abstract.
   */
  protected static void analyzeForBody(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    PdfFont font = document.getTextStatistics().getMostCommonFont();
    PdfColor fontColor = document.getTextStatistics().getMostCommonFontColor();
    float fontsize =
        Math.round(document.getTextStatistics().getMostCommonFontsize());

    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() == PdfRole.UNKNOWN) {
          PdfFont paraFont = paragraph.getTextStatistics().getMostCommonFont();
          PdfColor paraFontColor =
              paragraph.getTextStatistics().getMostCommonFontColor();
          float paraFontsize =
              Math.round(paragraph.getTextStatistics().getMostCommonFontsize());

          if (paraFont == font && paraFontColor == fontColor
              && paraFontsize == fontsize) {
            paragraph.setRole(PdfRole.BODY_TEXT);
          }
        }
      }
    }
  }
}