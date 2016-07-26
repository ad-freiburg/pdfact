package analyzer;

import static analyzer.MathSymbols.containsMathSymbol;
import static analyzer.MathSymbols.isMathSymbol;
import static model.Patterns.FIGURE_CAPTION_PATTERN;
import static model.Patterns.FORMULA_LABEL_PATTERN;
import static model.Patterns.TABLE_CAPTION_PATTERN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.text.StringUtils;
import model.Comparators;
import model.Patterns;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFont;
import model.PdfNonTextParagraph;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextAlignment;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;
import model.TextStatistics;

/**
 * The concrete implementation of a Pdf Analyzer.
 *
 * @author Claudius Korzen
 *
 */
public class PlainPdfAnalyzer implements PdfAnalyzer {
  /** Flag to indicate whether the heading of abstract was found. */
  protected boolean abstractHeadingFound;
  /** Flag to indicate whether the abstract was found. */
  protected boolean abstractFound;
  
  
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
    analyzeForOtherHeaderFields(document, characteristics);
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

    PdfTextParagraph titleParagraph = null;
    float titleParagraphScore = 0;
    
    // Analyze only the first two pages.
    for (PdfPage page : pages) {
      float fontsize = document.getTextStatistics().getMostCommonFontsize();

      for (PdfTextParagraph paragraph : page.getParagraphs()) {
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
        return;
      }  
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
    
    PdfFont docFont = document.getTextStatistics().getMostCommonFont();

    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }
 
        // TODO: Move this method to external class.
        String markup = PdfParagraphCharacteristics.getMarkup(paragraph);
                
        String sectionHeadingMarkup = characteristics.getSectionHeadingMarkup();
        PdfFont sectionHeadingFont = characteristics.getSectionHeadingFont();
        
        if (characteristics.isAbstractHeading(paragraph)) {
          paragraph.setRole(PdfRole.ABSTRACT_HEADING);
          this.abstractHeadingFound = true;
          continue;
        }

        if (characteristics.isReferencesHeading(paragraph)) {
          paragraph.setRole(PdfRole.REFERENCES_HEADING);
          continue;
        }
        
        if (characteristics.isAcknowledgmentHeading(paragraph)) {
          paragraph.setRole(PdfRole.SECTION_HEADING);
          continue;
        }
        
        if (markup != null && markup.equals(sectionHeadingMarkup)) {
          paragraph.setRole(PdfRole.SECTION_HEADING);
          continue;
        }

        PdfFont paraFont = paragraph.getFont();
        // Identify headings of subsections.
        if (sectionHeadingFont != docFont && paraFont == sectionHeadingFont) {
          paragraph.setRole(PdfRole.SECTION_HEADING);
          continue;
        }
        
        // Experimental: Identify headings of (subsub-) sections where the font
        // isn't equal to sectionHeadingFont.
        String text = paragraph.getUnicode();
        Matcher matcher = Patterns.ITEMIZE_START_PATTERN.matcher(text);
        if (matcher.find() && paraFont != docFont) {
          paragraph.setRole(PdfRole.SECTION_HEADING);
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a figure caption.
   */
  protected void analyzeForTables(PdfDocument document,
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
          List<PdfNonTextParagraph> nonTextParagraphsAbove =
              page.getNonTextParagraphsOverlapping(above);
          float areaNonTextParasAbove = 0;
          for (PdfNonTextParagraph para : nonTextParagraphsAbove) {
            areaNonTextParasAbove += para.getRectangle().getArea();
          }

          // Search for non text elements below the caption (in small excerpt).
          Rectangle below = new SimpleRectangle();
          below.setMinX(paragraph.getRectangle().getMinX());
          below.setMaxX(paragraph.getRectangle().getMaxX());
          below.setMinY(paragraph.getRectangle().getMinY() - 75); // TODO
          below.setMaxY(paragraph.getRectangle().getMinY() - 1);
          List<PdfNonTextParagraph> nonTextParagraphsBelow = 
              page.getNonTextParagraphsOverlapping(below);
          float areaNonTextParasBelow = 0;
          for (PdfNonTextParagraph para : nonTextParagraphsBelow) {
            areaNonTextParasBelow += para.getRectangle().getArea();
          }

          // Compare the number of non text elements in both areas.
          if (areaNonTextParasAbove == 0 && areaNonTextParasBelow == 0) {
            // No figures above and below the paragraph. So the paragraph is
            // not a caption.
            continue;
          }

          // There is a figure above or below the paragraph. So the paragraph
          // is a caption.
          paragraph.setRole(PdfRole.TABLE_CAPTION);

          // Try to identify all paragraphs of the figure.
          float width = MathUtils.round(paragraph.getRectangle().getWidth(), 0);

          if (areaNonTextParasAbove > areaNonTextParasBelow) {
            // Consume all paragraphs that are more smaller than the caption.
            List<PdfTextParagraph> paras = new ArrayList<>();
            for (PdfNonTextParagraph nonTextPara : nonTextParagraphsAbove) {
              paras.addAll(page.getParagraphsOverlapping(nonTextPara));
            }
            Collections.sort(paras, new Comparators.MinYComparator());

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
              }

              para.setRole(PdfRole.TABLE);
            }
          } else {
            // Consume all paragraphs that are more smaller than the caption.
            List<PdfTextParagraph> paras = new ArrayList<>();
            for (PdfNonTextParagraph nonTextPara : nonTextParagraphsBelow) {
              paras.addAll(page.getParagraphsOverlapping(nonTextPara));
            }
            Collections.sort(paras,
                Collections.reverseOrder(new Comparators.MinYComparator()));

            for (PdfTextParagraph para : paras) {
              if (para.getRole() != PdfRole.UNKNOWN) {
                continue;
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
  protected void analyzeForFigures(PdfDocument document,
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

          // Check if there is at least one element that is large enough.
          boolean containsLargeNonTextElementAbove = false;
          for (PdfElement element : nonTextElementsAbove) {
            Rectangle rect = element.getRectangle();
            
            // TODO: Don't consider simple lines in formulas
            if (rect.getWidth() > 1 && rect.getHeight() > 1) { 
              containsLargeNonTextElementAbove = true;
              break;
            }
          }
          
          // Search for non text elements below the caption (in small excerpt).
          Rectangle below = new SimpleRectangle();
          below.setMinX(paragraph.getRectangle().getMinX());
          below.setMaxX(paragraph.getRectangle().getMaxX());
          below.setMinY(paragraph.getRectangle().getMinY() - 75); // TODO
          below.setMaxY(paragraph.getRectangle().getMinY() - 1);
          List<? extends PdfElement> nonTextElementsBelow =
              page.getNonTextElementsOverlapping(below);
          int numNonTextElementsBelow = nonTextElementsBelow.size();
          
          // Check if there is at least one element that is large enough.
          boolean containsLargeNonTextElementBelow = false;
          for (PdfElement element : nonTextElementsBelow) {
            Rectangle rect = element.getRectangle();
            
            // TODO: Don't consider simple lines in formulas
            if (rect.getWidth() > 1 && rect.getHeight() > 1) {
              containsLargeNonTextElementBelow = true;
              break;
            }
          }
          
          // Compare the number of non text elements in both areas.
          // 4 because to respect rectangles that are "drawn by 4 edges.
          if (!containsLargeNonTextElementAbove && !containsLargeNonTextElementBelow) {
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
  protected void analyzeForSeparatedFormulas(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }
        
        float numNonMathChars = 0;
        float numMathChars = 0;

        for (PdfTextLine line : paragraph.getTextLines()) {
          for (PdfWord word : line.getWords()) {
            // TODO: Use StringUtils.normalize.
            String str = word.getUnicode().toLowerCase().trim().replaceAll("[\\.,]", "");
            
            if (line.getAlignment() == PdfTextAlignment.LEFT) {
              numNonMathChars++;
            } else if (isMathSymbol(str) || containsMathSymbol(str)
                || word.containsSubScript() || word.containsSuperScript()) {
              numMathChars += word.getTextCharacters().size();
            } else {
              numNonMathChars += word.getTextCharacters().size();
            }
          }
        }

        float mathWordsRatio = numMathChars / (numMathChars + numNonMathChars);
                
        if (mathWordsRatio > 0.75f) {
          paragraph.setRole(PdfRole.FORMULA);
          continue;
        }
        
        String text = paragraph.getText(true, true, true);
             
        if (text != null) {
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
  }

//  /**
//   * Returns true if we suppose that the given paragraph is an abstract.
//   */
//  protected void analyzeForAbstract(PdfDocument document,
//      PdfParagraphCharacteristics characteristics) {
//    boolean abstractHeaderAlreadySeen = false;
//    String abstractMarkup = null;
//    for (PdfPage page : document.getPages()) {
//      for (PdfTextParagraph paragraph : page.getParagraphs()) {        
//        if (abstractHeaderAlreadySeen) {
//          if (paragraph.getRole() != PdfRole.UNKNOWN) {
//            return;
//          }
//
//          String markup = paragraph.getMarkup();
//          if (abstractMarkup != null && abstractMarkup.equals(markup)) {
//            return;
//          }
//          
//          paragraph.setRole(PdfRole.ABSTRACT);
//          this.abstractFound = true;
//          abstractMarkup = paragraph.getMarkup();
//        }
//
//        if (paragraph.getRole() == PdfRole.ABSTRACT_HEADING) {
//          abstractHeaderAlreadySeen = true;
//        }
//      }
//    }
//  }
  
  /**
   * Returns true if we suppose that the given paragraph is an abstract.
   */
  protected void analyzeForAbstract(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    for (PdfPage page : document.getPages()) {
      PdfTextParagraph prevParagraph = null;
      for (PdfTextParagraph paragraph : page.getParagraphs()) {          
        Threeway t = isAbstract(prevParagraph, paragraph);
                
        switch (t) {
          case TRUE:
            paragraph.setRole(PdfRole.ABSTRACT);
            this.abstractFound = true;
            break;
          case FALSE:
            return;
          case CONTINUE:
          default:
            continue;
            
        }
                        
        prevParagraph = paragraph;
      }
    }
  }

  protected Threeway isAbstract(PdfTextParagraph prevParagraph, 
      PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return Threeway.FALSE;
    }
    
    // Abstract usually don't follow any section headings.
    if (paragraph.getRole() == PdfRole.SECTION_HEADING) {
      return Threeway.FALSE;
    }
    
    // Don't overwrite existing roles. 
    if (paragraph.getRole() != PdfRole.UNKNOWN) {
      return Threeway.CONTINUE;
    }
    
    // Ignore paragrpahs with less than 50 characters.
    if (paragraph.getWords().size() < 50) {
      return Threeway.CONTINUE;
    }
    
    if (prevParagraph != null) {
      if (prevParagraph.getRole() == PdfRole.ABSTRACT_HEADING) {
        return Threeway.TRUE;
      }
      
      if (prevParagraph.getRole() == PdfRole.ABSTRACT) {
        if (prevParagraph.getMarkup().equals(paragraph.getMarkup())) {
          return Threeway.TRUE;
        } else {
          return Threeway.FALSE;
        }
      }
    }
    
    PdfDocument document = paragraph.getPdfDocument();
    TextStatistics stats = document.getTextStatistics();
    
    PdfFont docFont = stats.getMostCommonFont();
    float docFontsize = MathUtils.round(stats.getMostCommonFontsize(), 0);
    
    PdfFont paraFont = paragraph.getFont();
    float paraFontsize = MathUtils.round(paragraph.getFontsize(), 1);
    
    if (paraFont != docFont) {
      return Threeway.TRUE;
    }
    
    if (paraFontsize < docFontsize) {
      return Threeway.TRUE;
    }
    
    return Threeway.FALSE;
  }
  
  /**
   * Returns true if we suppose that the given paragraph belongs to the 
   * document header.
   */
  protected void analyzeForOtherHeaderFields(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    
    // Mark all paragraphs before the abstract that wasn't annotated yet, 
    // as "HEADER_OTHER".
    
    // If no abstract was found, we don't know where to stop. Abort.
    if (!this.abstractHeadingFound && !this.abstractFound) {
      return;
    }
    
    for (PdfPage page : document.getPages()) {
      for (PdfTextParagraph paragraph : page.getParagraphs()) {
        // Abort if we have reached the abstract.
        if (paragraph.getRole() == PdfRole.ABSTRACT_HEADING 
            || paragraph.getRole() == PdfRole.ABSTRACT) {
          return;
        }
        
        // Don't overwrite existing roles.
        if (paragraph.getRole() != PdfRole.UNKNOWN) {
          continue;
        }
        
        paragraph.setRole(PdfRole.HEADER_OTHER);
      }
    }
  }
  
  /**
   * Returns true if we suppose that the given paragraph is an abstract.
   */
  protected void analyzeForReferences(PdfDocument document,
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
  protected void analyzeForBody(PdfDocument document,
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
  
  protected enum Threeway {
    TRUE, FALSE, CONTINUE
  }
}