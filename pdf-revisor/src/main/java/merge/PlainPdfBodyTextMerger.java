package merge;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.freiburg.iif.math.MathUtils;
import model.Patterns;
import model.PdfArea;
import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextAlignment;
import model.PdfTextLine;
import model.PdfTextParagraph;
import statistics.TextLineStatistician;

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
    
    if (prevLineIsTooShort(paragraph, lastTextLine, firstTextLine)) {
      return false;
    }
    
    if (!firstTextLine.isIndented()) {
      return true;
    }
    
    if (lastTextLine.isIndented()
        && lastTextLine.getIndentLevel() == firstTextLine.getIndentLevel()) {
      return true;
    }
    return false;
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
}
