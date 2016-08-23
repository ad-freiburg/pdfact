package merge;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.text.StringUtils;
import model.Characters;
import model.Patterns;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextAlignment;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;
import model.TextLineStatistics;
import statistics.TextLineStatistician;

// TODO: This class uses most of the methods from ParagraphifyRule. Merge them.

public class PlainPdfBodyTextMerger implements PdfBodyTextMerger {
  protected boolean isParagraphSplitterInBetween = false;

  @Override
  public void mergeBodyText(PdfDocument document) {
    mergeParagraphs(document);
  }
  
  public void mergeParagraphs(PdfDocument document) {
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages();
    if (pages == null) {
      return;
    }
    
    PdfTextParagraph prevBodyTextParagraph = null;
    for (PdfPage page : pages) {
      if (page == null) {
        continue;
      }
      
      List<PdfTextParagraph> paragraphs = page.getParagraphs();
      if (paragraphs == null) {
        continue;
      }
      
      for (PdfTextParagraph paragraph : paragraphs) {
        if (paragraph == null) {
          continue;
        }
                  
        if (appendToPrevParagraph(paragraph, prevBodyTextParagraph)) {
          prevBodyTextParagraph.addTextLines(paragraph.getTextLines());
          paragraph.setIgnore(true);
        } else if (paragraph.getRole() == PdfRole.BODY_TEXT
            || paragraph.getRole() == PdfRole.ITEMIZE_ITEM) {
          prevBodyTextParagraph = paragraph;
        }
      }
    }
  }
  
  protected boolean appendToPrevParagraph(PdfTextParagraph paragraph, 
      PdfTextParagraph prevBodyTextParagraph) {
    if (paragraph == null || prevBodyTextParagraph == null) {
      return false;
    }
    
    PdfRole paraRole = paragraph.getRole();
    PdfRole prevParaRole = prevBodyTextParagraph.getRole();
          
    if (paraRole == PdfRole.FORMULA
        || paraRole == PdfRole.SECTION_HEADING
        || paraRole == PdfRole.ABSTRACT_HEADING
        || paraRole == PdfRole.REFERENCES_HEADING) {
      this.isParagraphSplitterInBetween = true;
      return false;
    }
    
    if (paraRole != PdfRole.BODY_TEXT && paraRole != PdfRole.ITEMIZE_ITEM) {
      return false;
    }
    
    if (paraRole != prevParaRole) {
      return false;
    }
    
    if (this.isParagraphSplitterInBetween) {
      this.isParagraphSplitterInBetween = false;
      return false;
    }
    
    this.isParagraphSplitterInBetween = false;
        
    PdfTextLine lastTextLine = prevBodyTextParagraph.getLastTextLine();
    if (lastTextLine == null) {
      return false;
    }
    
    PdfTextLine firstTextLine = paragraph.getFirstTextLine();
    if (firstTextLine == null) {
      return false;
    }
    
    if (isItemizeStart(lastTextLine, firstTextLine)) {
      return false;
    }
    
    if (lastTextLine.getAlignment() == PdfTextAlignment.CENTERED 
        && firstTextLine.getAlignment() != PdfTextAlignment.CENTERED) {
      return false;
    }
    
    if (lastTextLine.getAlignment() == PdfTextAlignment.CENTERED 
        && lastTextLine.getAlignment() != PdfTextAlignment.CENTERED) {
      return false;
    }
    
    if (lastTextLine.getAlignment() == PdfTextAlignment.RIGHT 
        && lastTextLine.getAlignment() != PdfTextAlignment.RIGHT) {
      return false;
    }
    
    if (firstTextLine.isIndented()) {
      return false;
    }
    
    if (lastTextLine.isIndented()
        && lastTextLine.getIndentLevel() == firstTextLine.getIndentLevel()) {
      return true;
    }
    
    String lastLineText = lastTextLine.getText(true, false, false);
    if (lastLineText != null) {
      // Append the paragraph to prevParagraph, if the prev paragraph
      // doesn't end with a punctuation mark.
      if (!StringUtils.endsWith(lastLineText, ".", "?", "!")) {
        return true; 
      }
    }
    
    String firstLineText = firstTextLine.getText(true, false, false);
    if (firstLineText != null && !firstLineText.isEmpty()) {
      // Append the paragraph to prevParagraph, if the paragraph
      // starts with a lowercase letter.
      if (Character.isLowerCase(firstLineText.charAt(0))) {
        return true;
      }
    }
    
    // TODO: For what reason this criterion was introduced? It yields to 
    // troubles on inline graphics, e.g. cond-mat0001246.
    if (lastTextLine.getPage().getPageNumber() 
        == firstTextLine.getPage().getPageNumber()
        && lastTextLine.getColumnXRange() 
        == firstTextLine.getColumnXRange()
        && linepitchIsTooLarge(paragraph, lastTextLine, firstTextLine)) {
      return false;
    }
    
    return true;
  }
  
  protected boolean isItemizeStart(PdfTextLine prevLine, PdfTextLine line) {
    List<Pattern> itemizeStartPatterns = Patterns.ITEMIZE_START_PATTERNS;
    
    boolean matches = false;
    for (Pattern pattern : itemizeStartPatterns) {
      Matcher m = pattern.matcher(line.getFirstWord().getUnicode());
        
      if (m.matches() && !m.group(1).isEmpty() && line.getWords().size() > 1) {
        matches = true;
        break;
      }
    }
    
    if (!matches) {
      return false;
    }
    
    // "Normal" lines could be started by a numbering, too. So, take also 
    // the line pitch into account (headings and itemizes must have a 
    // larger linepitch to previous line.
    PdfDocument doc = line.getPdfDocument();
    float mcPitch = doc.getTextLineStatistics().getMostCommonLinePitch();
    float linePitch = Float.MAX_VALUE;
    if (prevLine != null) {
      linePitch = TextLineStatistician.computeLinePitch(prevLine, line);  
    }
          
    return MathUtils.isLarger(linePitch, mcPitch, 0.5f);
  }
  
  /**
   * Returns true, if the previous line is too short.
   */
  protected boolean prevLineIsTooShort(PdfTextParagraph paragraph, 
      PdfTextLine prevLine, PdfTextLine line) {
    if (prevLine == null) {
      return false;
    }

    PdfDocument document = paragraph.getPdfDocument();

    if (document.getTextAlignment() != PdfTextAlignment.JUSTIFIED) {
      return false;
    }

    if (paragraph.getTextLineAlignment() == PdfTextAlignment.CENTERED) {
      return false;
    }
        
    float tolerance = paragraph.getDimensionStatistics().getMostCommonWidth();
    float prevLineMaxX = prevLine.getRectangle().getMaxX();
    float blockMaxX = prevLine.getColumnXRange().getEndX();
        
    return MathUtils.isSmaller(prevLineMaxX, blockMaxX, 5 * tolerance);
  }
  
  /**
   * Analyzes the linepitch and returns true, if the linepitch is too large.
   */
  protected static boolean linepitchIsTooLarge(PdfTextParagraph paragraph,
      PdfTextLine prevLine, PdfTextLine line) {
    PdfPage page = line.getPage();

    float pitch = TextLineStatistician.computeLinePitch(prevLine, line);
    float basePitch = TextLineStatistician.computeBaseLinePitch(prevLine, line);

    TextLineStatistics pageLineStatistics = page.getTextLineStatistics();
    float pageLinePitch = pageLineStatistics.getSmallestSignificantLinepitch();
    float pageBaselinePitch =
        pageLineStatistics.getSmallestSignificantBaselinepitch();

    if (MathUtils.isLarger(pitch, pageLinePitch, pageLinePitch)
        && MathUtils.isLarger(basePitch, pageBaselinePitch, 2f)) {

      return true;
    }

    if (paragraph.getTextLines().size() > 1) {
      TextLineStatistics paraStatistics = paragraph.getTextLineStatistics();
      float paragraphPitch = paraStatistics.getMostCommonLinePitch();
      float paragraphBasePitch = paraStatistics.getMostCommonBaselinePitch();

      // The line introduces a new paragraph, if the pitch to the previous line
      // is larger than the most common line pitch in the paragraph.
      if (MathUtils.isLarger(pitch, paragraphPitch, 0.5f * paragraphPitch)
          && MathUtils.isLarger(basePitch, paragraphBasePitch, 2f)) {
        return true;
      }
    }

    return false;
  }
}
