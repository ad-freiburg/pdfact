package icecite.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfFont;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.utils.geometric.Rectangle;
import icecite.utils.textlines.PdfTextLineUtils;

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
   * The pattern to identify reference anchors.
   */
  protected static final Pattern REFERENCE_ANCHOR = Pattern
      .compile("^\\[(.*)\\]\\s+");

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

      if (introducesParagraph(pdf, paragraph, prevLine, line, nextLine)) {
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
   * @param pdf
   *        The PDF document.
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
  protected boolean introducesParagraph(PdfDocument pdf, PdfParagraph paragraph,
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

    // The line introduces a new paragraph, if it has a special font face.
    if (hasSignificantDifferentFontFace(prevLine, line)) {
      return true;
    }

    // The line introduces a new paragraph, if the pitch to the previous line
    // is larger than the most common line pitch in the paragraph.
    if (isLinepitchTooLarge(pdf, prevLine, line)) {
      return true;
    }

    // The line introduces a new paragraph, if it denotes the start of a
    // reference.
    if (isProbablyReferenceStart(prevLine, line, nextLine)) {
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
   *         line and the given next line and the line pitch between the
   *         previous line / current line and between the current line / next
   *         line are almost equal.
   */
  protected boolean isIndented(PdfTextLine prevLine, PdfTextLine line,
      PdfTextLine nextLine) {
    if (prevLine == null || line == null || nextLine == null) {
      return false;
    }

    // The line pitch between previous line / current line and between the
    // current line / next line must be almost equal.
    float prevLinePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);
    float linePitch = PdfTextLineUtils.computeLinePitch(line, nextLine);
    // TODO
    if (Math.abs(prevLinePitch - linePitch) > 1) {
      return false;
    }

    // The previous and next line must not start with an reference anchor.
    boolean hasPrevLineReferenceAnchor = startsWithReferenceAnchor(prevLine);
    boolean hasNextLineReferenceAnchor = startsWithReferenceAnchor(nextLine);
    if (hasPrevLineReferenceAnchor && hasNextLineReferenceAnchor) {
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

  /**
   * Checks if the given line has a special font face, comapred to the given
   * previous line.
   * 
   * @param prevLine
   *        The previous line of the line to process.
   * @param line
   *        The line to process.
   * @return True, if the given line has a special font face, False otherwise.
   */
  protected static boolean hasSignificantDifferentFontFace(PdfTextLine prevLine,
      PdfTextLine line) {
    if (prevLine == null || line == null) {
      return false;
    }

    // If the font of the previous line and the font of the current line are
    // not from the same base, the line has a special font face.
    PdfFont prevLineFont = prevLine.getCharacters().getMostCommonFont();
    PdfFont lineFont = line.getCharacters().getMostCommonFont();
    if (prevLineFont == null || lineFont == null) {
      return false;
    }
    String prevLineFontFamilyName = prevLineFont.getFontFamilyName();
    String lineFontFamilyName = lineFont.getFontFamilyName();
    if (prevLineFontFamilyName == null || lineFontFamilyName == null) {
      return false;
    }
    if (!prevLineFontFamilyName.equals(lineFontFamilyName)) {
      return true;
    }

    // If the font size of the previous line and the font size of the current
    // line are not equal, the line has a special font face.
    float prevLineFontsize = prevLine.getCharacters().getMostCommonFontsize();
    float lineFontsize = line.getCharacters().getMostCommonFontsize();
    if (prevLineFontsize != lineFontsize) {
      return true;
    }

    return false;
  }

  /**
   * Checks if the line pitch between the given line and the given previous
   * line is larger than usual (larger than the most common line pitch for the
   * font / font size pair of the given line).
   * 
   * @param pdf
   *        The PDF document.
   * @param prevLine
   *        The previous line of the line to process.
   * @param line
   *        The line to process.
   * @return True, if the line pitch between the given line and the given
   *         previous line is larger than usual; False otherwise.
   */
  protected static boolean isLinepitchTooLarge(PdfDocument pdf,
      PdfTextLine prevLine, PdfTextLine line) {
    if (pdf == null || prevLine == null || line == null) {
      return false;
    }

    PdfCharacterList lineCharacters = line.getCharacters();
    if (lineCharacters == null) {
      return false;
    }

    PdfTextLineList textLines = pdf.getTextLines();
    if (textLines == null) {
      return false;
    }

    // Obtain the expected and actual line pitch for the given line.
    float expectedLinePitch = textLines.getMostCommonLinePitch(line);
    float actualLinePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);

    if (Float.isNaN(expectedLinePitch) || Float.isNaN(actualLinePitch)) {
      return false;
    }

    // TODO
    return actualLinePitch - expectedLinePitch > 1;
  }

  /**
   * Returns true, if the given line is (probably) a reference start.
   * 
   * @param prevLine
   *        The previous line of the line to process.
   * @param line
   *        The line to process.
   * @param nextLine
   *        The next line of the line to process.
   * @return True, if the given line is (probably) a reference start.
   */
  protected boolean isProbablyReferenceStart(PdfTextLine prevLine,
      PdfTextLine line, PdfTextLine nextLine) {
    if (prevLine == null || line == null || nextLine == null) {
      return false;
    }

    // The line must start with an reference anchor.
    if (!startsWithReferenceAnchor(line)) {
      return false;
    }

    Rectangle prevLineRect = prevLine.getRectangle();
    Rectangle lineRect = line.getRectangle();
    Rectangle nextLineRect = nextLine.getRectangle();
    if (prevLineRect == null || lineRect == null || nextLineRect == null) {
      return false;
    }

    float prevLineMinX = prevLineRect.getMinX();
    float lineMinX = lineRect.getMinX();
    float nextLineMinX = nextLineRect.getMinX();

    // TODO
    boolean hasPrevLineDifferentMinX = Math.abs(prevLineMinX - lineMinX) > 0.5;
    boolean hasNextLineDifferentMinX = Math.abs(nextLineMinX - lineMinX) > 0.5;

    boolean hasPrevLineReferenceAnchor = startsWithReferenceAnchor(prevLine);
    boolean hasNextLineReferenceAnchor = startsWithReferenceAnchor(nextLine);

    return (hasPrevLineDifferentMinX || hasPrevLineReferenceAnchor)
        && (hasNextLineDifferentMinX || hasNextLineReferenceAnchor);
  }

  /**
   * Checks is the given text line starts with a reference anchor like "[1]",
   * "[2]", etc.
   * 
   * @param line
   *        The text line to check.
   * @return True, if the given text line starts with a reference anchor, false
   *         otherwise.
   */
  // TODO: Move to any util.
  protected boolean startsWithReferenceAnchor(PdfTextLine line) {
    if (line == null) {
      return false;
    }

    String text = line.getText();
    if (text == null) {
      return false;
    }

    return REFERENCE_ANCHOR.matcher(text).find();
  }
}
