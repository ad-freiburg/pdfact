package analyzer;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.Comparators;
import model.PdfDocument;
import model.PdfElement;
import model.PdfPage;
import model.PdfRole;
import model.PdfShape;
import model.PdfTextParagraph;

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

  @Override
  public void analyze(PdfDocument document) {
    PdfParagraphCharacteristics characteristics =
        new PdfParagraphCharacteristics(document);

    analyzeDocument(document, characteristics);
  }

  protected void analyzeDocument(PdfDocument document,
      PdfParagraphCharacteristics characteristics) {
    if (document == null) {
      return;
    }

    analyzePages(document.getPages(), characteristics);
  }

  protected void analyzePages(List<PdfPage> pages,
      PdfParagraphCharacteristics characteristics) {
    if (pages == null) {
      return;
    }

    for (PdfPage page : pages) {
      analyzePage(page, characteristics);
    }
  }

  protected void analyzePage(PdfPage page,
      PdfParagraphCharacteristics characteristics) {
    if (page == null) {
      return;
    }

    analyzeParagraphs(page.getParagraphs(), characteristics);
  }

  protected void analyzeParagraphs(List<PdfTextParagraph> paragraphs,
      PdfParagraphCharacteristics characteristics) {
    if (paragraphs == null) {
      return;
    }

    for (PdfTextParagraph para : paragraphs) {
      analyzeParagraph(para, characteristics);
    }
  }

  protected void analyzeParagraph(PdfTextParagraph paragraph,
      PdfParagraphCharacteristics characteristics) {
    if (paragraph == null) {
      return;
    }

    if (paragraph.getRole() != null) {
      return;
    }

    analyzeForSectionHeading(paragraph, characteristics);
    analyzeForTableCaption(paragraph, characteristics);
    analyzeForFigureCaption(paragraph, characteristics);
    // analyzeForFigure(paragraph, characteristics);
    // analyzeForTable(paragraph, characteristics);
  }

  // ___________________________________________________________________________

  protected void analyzeForSectionHeading(PdfTextParagraph paragraph,
      PdfParagraphCharacteristics characteristics) {
    // TODO: Move this method to external class.
    String markup = PdfParagraphCharacteristics.getMarkup(paragraph);
    String sectionHeadingMarkup = characteristics.getSectionHeadingMarkup();

    if (markup != null && markup.equals(sectionHeadingMarkup)) {
      paragraph.setRole(PdfRole.SECTION_HEADING);
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a figure caption.
   */
  protected static void analyzeForTableCaption(PdfTextParagraph paragraph,
      PdfParagraphCharacteristics characteristics) {
    if (paragraph == null) {
      return;
    }

    String text = paragraph.getUnicode();
    PdfPage page = paragraph.getPage();

    if (text == null || page == null) {
      return;
    }

    // Check if the paragraph could be a caption.
    Matcher tableCaptionMatcher = TABLE_CAPTION_PATTERN.matcher(text);
    if (tableCaptionMatcher.find()) {
      // The paragraph is potential caption. To acknowledge, search for a figure
      // above and below the caption.
      
      // Search for non text elements above the caption (in a small excerpt).
      Rectangle above = new SimpleRectangle();
      above.setMinX(paragraph.getRectangle().getMinX());
      above.setMaxX(paragraph.getRectangle().getMaxX());
      above.setMinY(paragraph.getRectangle().getMaxY() + 1);
      above.setMaxY(paragraph.getRectangle().getMaxY() + 75); // TODO 
      List<PdfShape> shapesAbove = page.getShapesOverlapping(above);
      int numShapesAbove = shapesAbove.size();

      // Search for non text elements below the caption (in a small excerpt).
      Rectangle below = new SimpleRectangle();
      below.setMinX(paragraph.getRectangle().getMinX());
      below.setMaxX(paragraph.getRectangle().getMaxX());
      below.setMinY(paragraph.getRectangle().getMinY() - 75); // TODO
      below.setMaxY(paragraph.getRectangle().getMinY() - 1);
      List<PdfShape> shapesBelow = page.getShapesOverlapping(below);
      int numShapesBelow = shapesBelow.size();
      
      // Compare the number of non text elements in both areas.
      if (numShapesAbove == 0 && numShapesBelow == 0) {
        // No figures above and below the paragraph. So the paragraph is not a 
        // caption.
        return;
      }
      
      // There is a figure above or below the paragraph. So the paragraph is a
      // caption.
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
          float paraWidth = MathUtils.round(para.getRectangle().getWidth(), 0);
          
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
          float paraWidth = MathUtils.round(para.getRectangle().getWidth(), 0);
          
          // Abort if we see a paragraph that is wider than the caption.
          if (paraWidth >= width) {
            return;
          }
          
          para.setRole(PdfRole.TABLE);
        }
      }
    }
  }

  /**
   * Returns true if we suppose that the given paragraph is a figure caption.
   */
  protected static void analyzeForFigureCaption(PdfTextParagraph paragraph,
      PdfParagraphCharacteristics characteristics) {
    if (paragraph == null) {
      return;
    }

    String text = paragraph.getUnicode();
    PdfPage page = paragraph.getPage();

    if (text == null || page == null) {
      return;
    }

    // Check if the paragraph could be a caption.
    Matcher tableCaptionMatcher = FIGURE_CAPTION_PATTERN.matcher(text);
    if (tableCaptionMatcher.find()) {
      // The paragraph is potential caption. To acknowledge, search for a figure
      // above and below the caption.
      
      // Search for non text elements above the caption (in a small excerpt).
      Rectangle above = new SimpleRectangle();
      above.setMinX(paragraph.getRectangle().getMinX());
      above.setMaxX(paragraph.getRectangle().getMaxX());
      above.setMinY(paragraph.getRectangle().getMaxY() + 1);
      above.setMaxY(paragraph.getRectangle().getMaxY() + 75); // TODO 
      List<? extends PdfElement> nonTextElementsAbove =
          page.getNonTextElementsOverlapping(above);
      int numNonTextElementsAbove = nonTextElementsAbove.size();

      // Search for non text elements below the caption (in a small excerpt).
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
        // No figures above and below the paragraph. So the paragraph is not a 
        // caption.
        return;
      }
      
      // There is a figure above or below the paragraph. So the paragraph is a
      // caption.
      paragraph.setRole(PdfRole.FIGURE_CAPTION);

      // Try to identify all paragraphs of the figure.
      float width = MathUtils.round(paragraph.getRectangle().getWidth(), 0);
      
      if (numNonTextElementsAbove > numNonTextElementsBelow) {
        // Extend the search area above the caption to upper bound of page.
        above.setMaxY(page.getRectangle().getMaxY());

        // Consume all paragraphs that are more smaller than the caption.
        List<PdfTextParagraph> paras = page.getParagraphsOverlapping(above);
        Collections.sort(paras, new Comparators.MinYComparator());
        
        for (PdfTextParagraph para : paras) {          
          float paraWidth = MathUtils.round(para.getRectangle().getWidth(), 0);
          
          // Abort if we see a paragraph that is wider than the caption.
          if (paraWidth >= width) {
            return;
          }
          
          para.setRole(PdfRole.FIGURE);
        }
      } else {
        // Extend the search area below the caption to lower bound of page.
        below.setMinY(page.getRectangle().getMinY());

        // Consume all paragraphs that are more smaller than the caption.
        List<PdfTextParagraph> paras = page.getParagraphsOverlapping(below);
        Collections.sort(paras,
            Collections.reverseOrder(new Comparators.MinYComparator()));
        
        for (PdfTextParagraph para : paras) {
          float paraWidth = MathUtils.round(para.getRectangle().getWidth(), 0);
          
          // Abort if we see a paragraph that is wider than the caption.
          if (paraWidth >= width) {
            return;
          }
          
          para.setRole(PdfRole.FIGURE);
        }
      }
    }
  }
}