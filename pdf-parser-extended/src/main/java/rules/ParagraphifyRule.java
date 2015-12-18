package rules;

import java.util.List;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import model.PdfFont;
import model.PdfPage;
import model.PdfTextParagraph;
import model.PdfTextLine;
import model.PdfWord;
import model.TextLineStatistics;
import statistics.TextLineStatistician;

/**
 * Rules to paragraphs identification.
 *
 * @author Claudius Korzen
 *
 */
public class ParagraphifyRule {
  /**
   * Returns true, if the given parameters introduces a new paragraph.
   */
  public static boolean introducesNewParagraph(PdfPage page,
      PdfTextParagraph paragraph, PdfTextLine prevLine, PdfTextLine line,
      PdfTextLine nextLine) {
    // The line doesn't introduce a new paragraph, if the paragraph is empty.
    if (paragraph.getTextLines().isEmpty()) {
      return false;
    }

    // The line doesn't introduce a new paragraph, if the previous and the 
    // current line have a overlapped element in common.
    if (haveOverlappedElementInCommon(page, prevLine, line)) {
      return false;
    }

    // The line introduces a new paragraph, if it doesn't overlap the paragraph
    // horizontally.
    if (!overlapsHorizontally(paragraph, line)) {
      return true;
    }
    
    // The line introduces a new paragraph, if the font size of the line and the
    // fontsize of the previous line aren't almost equal.
    if (fontsizesAreTooDifferent(prevLine, line)) {
      return true;
    }
    
    // The line introduces a new paragraph, if the pitch to the previous line
    // is larger than the most common line pitch in the paragraph.
    if (linepitchIsTooLarge(page, paragraph, prevLine, line)) {
      return true;
    }
    
    // TODO: Experimental. Identify headings, which have same fontsize as
    // body.
    if (isProbablyHeading(page, paragraph, prevLine, line)) {
      return true;
    }

    return false;
  }

  /**
   * Returns true, if the given two lines are overlapped by a same third
   * element.
   */
  protected static boolean haveOverlappedElementInCommon(PdfPage page, 
      PdfTextLine prevLine, PdfTextLine line) {
    // The line doesn't introduce a new paragraph, if it is located in the same
    // non text elements as the previous text element.
    List<? extends HasRectangle> prevAreas = null;
    if (prevLine != null) {
      Rectangle prevRectangle = prevLine.getRectangle();
      prevAreas = page.getElementsOverlapping(prevRectangle);

      List<? extends HasRectangle> areas = null;
      if (line != null) {
        Rectangle rectangle = line.getRectangle();
        areas = page.getElementsOverlapping(rectangle);

        areas.retainAll(prevAreas);

        if (!areas.isEmpty()) {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * Returns true, if the given elements overlaps horizontally.
   */
  protected static boolean overlapsHorizontally(HasRectangle hr1, 
      HasRectangle hr2) {
    return hr1.getRectangle().overlapsHorizontally(hr2.getRectangle());
  }
  
  /**
   * Analyzes the fontsizes of the given lines and returns true, if the
   * fontsizes "are too different".
   */
  protected static boolean fontsizesAreTooDifferent(PdfTextLine prevLine, 
      PdfTextLine line) {
    float lineFontsize = line != null ? line.getFontsize() : 0;
    float prevLineFontsize = prevLine != null ? prevLine.getFontsize() : 0;
  
    return MathUtils.isLarger(lineFontsize, prevLineFontsize, 0.49f)
        || MathUtils.isSmaller(lineFontsize, prevLineFontsize, 0.49f);
  }
  
  /**
   * Analyzes the linepitch and returns true, if the linepitch is too large.
   */
  protected static boolean linepitchIsTooLarge(PdfPage page, 
      PdfTextParagraph paragraph, PdfTextLine prevLine, PdfTextLine line) {
    
    float pitch = TextLineStatistician.computeLinePitch(prevLine, line);
    TextLineStatistics pageLineStatistics = page.getTextLineStatistics();
    float pageLinePitch = pageLineStatistics.getMostCommonLinePitch();
        
    if (MathUtils.isLarger(pitch, pageLinePitch, pageLinePitch)) {
      return true;
    }
    
    if (paragraph.getTextLines().size() > 1) {
      TextLineStatistics paraStatistics = paragraph.getTextLineStatistics();
      float paragraphPitch = paraStatistics.getMostCommonLinePitch();

      // The line introduces a new paragraph, if the pitch to the previous line
      // is larger than the most common line pitch in the paragraph.
      if (MathUtils.isLarger(pitch, paragraphPitch, 0.5f * paragraphPitch)) {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Returns true, if the given line is probably a heading. EXPERIMENTAL.
   */
  protected static boolean isProbablyHeading(PdfPage page, 
      PdfTextParagraph paragraph, PdfTextLine prevLine, PdfTextLine line) {
    PdfFont pageFont = page.getFont();
    PdfFont prevLineFont = prevLine != null ? prevLine.getFont() : null;
    PdfWord firstWord = line != null ? line.getFirstWord() : null;
    PdfWord lastWord = prevLine != null ? prevLine.getLastWord() : null;
    PdfFont lastWordFont = lastWord != null ? lastWord.getFont() : null;
    PdfFont firstWordFont = firstWord != null ? firstWord.getFont() : null;
    
    if (prevLine != null
        && !prevLineFont.equals(pageFont)
        && !lastWordFont.equals(firstWordFont)
        && MathUtils.isSmaller(prevLine.getRectangle().getMaxX(),
            line.getRectangle().getMaxX(), 5f)
        && paragraph.getTextLines().size() == 1) {
      return true;
    }
    return false;
  }
}
