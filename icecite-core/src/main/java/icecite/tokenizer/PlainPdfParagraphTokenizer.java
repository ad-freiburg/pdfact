package icecite.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfTextLine;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfParagraphTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphTokenizer implements PdfParagraphTokenizer {
  /**
   * The factory to create instances of PdfParagraph.
   */
  protected PdfParagraphFactory paragraphFactory;

  /**
   * Creates a new tokenizer to that tokenizes text lines into paragraphs.
   * 
   * @param paragraphFactory
   *        The factory to create instances of PdfParagraph.
   */
  @Inject
  public PlainPdfParagraphTokenizer(PdfParagraphFactory paragraphFactory) {
    this.paragraphFactory = paragraphFactory;
  }

  // ==========================================================================

  @Override
  // TODO: Dehyphenation.
  public List<PdfParagraph> tokenize(PdfDocument pdf) {
    List<PdfParagraph> paragraphs = new ArrayList<>();
    PdfParagraph paragraph = null;

    List<PdfTextLine> lines = pdf.getTextLines();
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;
      
      if (introducesParagraph(paragraph, prevLine, line, nextLine)) {
        if (paragraph != null && !paragraph.getWords().isEmpty()) {
          paragraphs.add(paragraph);
        }

        // Create a new paragraph.
        paragraph = this.paragraphFactory.create();
      }

      // Add the word of the current line to the current paragraph.
      paragraph.addWords(line.getWords());
    }
    // Don't forget the remaining paragraph.
    if (paragraph != null && !paragraph.getWords().isEmpty()) {
      paragraphs.add(paragraph);
    }

    return paragraphs;
  }

  /**
   * Checks if the given current text line introduces a new paragraph.
   *
   * @param paragraph
   *        The current paragraph.
   * @param prevLine
   *        The previous text line.
   * @param line
   *        The current text line.
   * @param nextLine
   *        The next text line.
   * @return True, if the given current line introduces a new paragraph; false
   *         otherwise.
   */
  protected boolean introducesParagraph(PdfParagraph paragraph,
      PdfTextLine prevLine, PdfTextLine line, PdfTextLine nextLine) {
    // The line does *not* introduce a new paragraph, if it is null.
    if (line == null) {
      return false;
    }

    // The line introduces a new paragraph, if there is no previous line.
    if (prevLine == null) {
      return true;
    }

    // The line introduces a new paragraph, if there is no current
    if (paragraph == null) {
      return true;
    }

    // The line does *not* introduce a new paragraph, if the current paragraph
    // is empty.
    if (paragraph.getWords().isEmpty()) {
      return false;
    }

    // The line introduces a new paragraph, if it doesn't overlap the paragraph
    // horizontally.
    if (!overlapsHorizontally(paragraph, line)) {
      return true;
    }

    // The line introduces a new paragraph, if it is indented compared to the
    // previous and the next line.
    if (isIndented(prevLine, line, nextLine)) {
      return true;
    }
    
    // The line introduces a new paragraph, if it has a larger fontsize than the
    // previous line.
//    if (hasLargerFontsize(prevLine, line)) {
//      return true;
//    }

    return false;
  }

  /**
   * Checks, if the given line overlaps the given paragraph horizontally.
   * 
   * @param paragraph
   *        The paragraph to process.
   * @param line
   *        The line to process.
   * @return True, if the given line overlaps the given paragraph horizontally,
   *         False otherwise.
   */
  protected boolean overlapsHorizontally(PdfParagraph paragraph,
      PdfTextLine line) {
    if (paragraph == null) {
      return false;
    }

    Rectangle paragraphBoundingBox = paragraph.getRectangle();
    if (paragraphBoundingBox == null) {
      return false;
    }

    if (line == null) {
      return false;
    }

    Rectangle lineBoundingBox = line.getRectangle();
    if (lineBoundingBox == null) {
      return false;
    }

    return paragraphBoundingBox.overlapsHorizontally(lineBoundingBox);
  }

  /**
   * Checks, if the given line is indented compared to the given previous line
   * and the given next line.
   * 
   * @param prevLine
   *        The previous line.
   * @param line
   *        The line to process.
   * @param nextLine
   *        The next line.
   * @return True, if the given line is indented compared to the given previous
   *         line and the given next line.
   */
  protected boolean isIndented(PdfTextLine prevLine, PdfTextLine line,
      PdfTextLine nextLine) {
    if (prevLine == null || line == null || nextLine == null) {
      return false;
    }
    
    Rectangle prevRectangle = prevLine.getRectangle();
    Rectangle rectangle = line.getRectangle();
    Rectangle nextRectangle = nextLine.getRectangle();
    
    if (prevRectangle == null || rectangle == null || nextRectangle == null) {
      return false;
    }
    
    if (prevRectangle.getMinX() != nextRectangle.getMinX()) {
      return false;
    }
    
    return rectangle.getMinX() > prevRectangle.getMinX();
  }
}
