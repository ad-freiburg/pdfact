package icecite.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
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
  public List<PdfParagraph> tokenize(PdfDocument pdf, PdfPage page,
      List<PdfTextLine> lines) {
    List<PdfParagraph> paragraphs = new ArrayList<>();

    // The current paragraph.
    PdfParagraph paragraph = null;
    PdfTextLine prevLine = null;
    PdfTextLine currentLine = null;

    for (PdfTextLine nextLine : lines) {
      if (currentLine != null) {
        if (introducesParagraph(paragraph, prevLine, currentLine, nextLine)) {
          // Add the current paragraph to the result, if it not empty.
          if (paragraph != null && paragraph.getTextLines().size() > 0) {
            paragraphs.add(paragraph);
          }
          // Create a new paragraph.
          paragraph = this.paragraphFactory.create();
          paragraph.setPage(page);
        }

        // Add the text line to the current paragraph.
        paragraph.addTextLine(currentLine);
      }

      prevLine = currentLine;
      currentLine = nextLine;
    }
    // Add the remaining paragraph to the result, if it not empty.
    if (paragraph != null && paragraph.getTextLines().size() > 0) {
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
   * @param currentLine
   *        The current text line.
   * @param nextLine
   *        The next text line.
   * @return True, if the given current line introduces a new paragraph; false
   *         otherwise.
   */
  protected boolean introducesParagraph(PdfParagraph paragraph,
      PdfTextLine prevLine, PdfTextLine currentLine, PdfTextLine nextLine) {
    // The line does *not* introduce a new paragraph, if it is null.
    if (currentLine == null) {
      return false;
    }

    // The line introduces a new paragraph, if there is no current paragraph.
    if (paragraph == null) {
      return true;
    }

    // The line does *not* introduce a new paragraph, if the current paragraph
    // is empty.
    if (paragraph.getTextLines().isEmpty()) {
      return false;
    }

    // The line introduces a new paragraph, if it doesn't overlap the paragraph
    // horizontally.
    if (!overlapsHorizontally(paragraph, currentLine)) {
      return true;
    }

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

    Rectangle paragraphBoundingBox = paragraph.getBoundingBox();
    if (paragraphBoundingBox == null) {
      return false;
    }

    if (line == null) {
      return false;
    }

    Rectangle lineBoundingBox = line.getBoundingBox();
    if (lineBoundingBox == null) {
      return false;
    }

    return paragraphBoundingBox.overlapsHorizontally(lineBoundingBox);
  }
}
