package icecite.tokenize;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfFont;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.utils.geometric.Rectangle;
import icecite.utils.textlines.PdfTextLineUtils;

/**
 * A plain implementation of {@link PdfTextBlockTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlockTokenizer implements PdfTextBlockTokenizer {
  /**
   * The factory to create instances of {@link PdfTextBlock}.
   */
  protected PdfTextBlockFactory textBlockFactory;

  /**
   * Creates a new tokenizer that tokenizes text lines into text blocks.
   * 
   * @param textBlockFactory
   *        The factory to create instances of {@link PdfTextBlock}.
   */
  @Inject
  public PlainPdfTextBlockTokenizer(PdfTextBlockFactory textBlockFactory) {
    this.textBlockFactory = textBlockFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page) {
    List<PdfTextBlock> textBlocks = new ArrayList<>();
    PdfTextBlock textBlock = null;

    List<PdfTextLine> lines = page.getTextLines();
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

      if (introducesTextBlock(pdf, textBlock, prevLine, line, nextLine)) {
        if (textBlock != null && !textBlock.getTextLines().isEmpty()) {
          textBlocks.add(textBlock);
        }

        // Create a new text block.
        textBlock = this.textBlockFactory.create(page);
      }
      // Add the word of the current line to the current text block.
      textBlock.addTextLine(line);
      textBlock.addCharacters(line.getCharacters());
    }

    // Don't forget the remaining text block.
    if (textBlock != null && !textBlock.getTextLines().isEmpty()) {
      textBlocks.add(textBlock);
    }

    return textBlocks;
  }

  /**
   * Checks if the given current text line introduces a new text block.
   * 
   * @param pdf
   *        The PDF document.
   * @param block
   *        The current text block.
   * @param prevLine
   *        The previous text line.
   * @param line
   *        The current text line.
   * @param nextLine
   *        The next text line.
   * @return True, if the given current line introduces a new text block; false
   *         otherwise.
   */
  protected boolean introducesTextBlock(PdfDocument pdf, PdfTextBlock block,
      PdfTextLine prevLine, PdfTextLine line, PdfTextLine nextLine) {
    // The line does *not* introduce a text block, if it is null.
    if (line == null) {
      return false;
    }

    // The line introduces a text block, if there is no previous line.
    if (prevLine == null) {
      return true;
    }

    // The line introduces a text block, if there is no current text block.
    if (block == null) {
      return true;
    }

    // The line does *not* introduce a text block, if the current text block is
    // empty.
    if (block.getTextLines().isEmpty()) {
      return false;
    }

    // The line introduces a text block, if it doesn't overlap the text
    // block horizontally.
    if (!overlapsHorizontally(block, line)) {
      return true;
    }

    // The line introduces a new text block, if the line pitch between the
    // line and the previous line is larger than expected.
    if (isLinepitchLargerThanExpected(pdf, prevLine, line)) {
      return true;
    }

    // The line introduces a new text block, if the line pitch between the
    // line and the previous line is larger than the line pitch between the
    // line and the next line.
    if (isLinePitchLargerThanNextLinePitch(prevLine, line, nextLine)) {
      return true;
    }

    // The line introduces a text block, if it is indented compared to the
    // previous and the next line.
    if (isIndented(prevLine, line, nextLine)) {
      return true;
    }

    // The line introduces a text block, if it has a special font face.
    if (hasSignificantDifferentFontFace(prevLine, line)) {
      return true;
    }

    // The line introduces a text block, if it is the start of a reference.
    if (isProbablyReferenceStart(prevLine, line, nextLine)) {
      return true;
    }

    return false;
  }

  /**
   * Checks, if the given line overlaps the given text block horizontally.
   * 
   * @param block
   *        The text block to process.
   * @param line
   *        The line to process.
   * @return True, if the given line overlaps the given text block
   *         horizontally, false otherwise.
   */
  protected boolean overlapsHorizontally(PdfTextBlock block, PdfTextLine line) {
    if (block == null || line == null) {
      return false;
    }

    Rectangle blockBoundingBox = block.getRectangle();
    Rectangle lineBoundingBox = line.getRectangle();
    if (blockBoundingBox == null || lineBoundingBox == null) {
      return false;
    }

    return blockBoundingBox.overlapsHorizontally(lineBoundingBox);
  }

  /**
   * Checks if the line pitch between the given line and the given previous
   * line is larger than expected (larger than the most common line pitch for
   * the font / font size pair of the given line).
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
  protected static boolean isLinepitchLargerThanExpected(PdfDocument pdf,
      PdfTextLine prevLine, PdfTextLine line) {
    if (pdf == null) {
      return false;
    }

    PdfTextLineList textLines = pdf.getTextLines();
    if (textLines == null) {
      return false;
    }

    // Obtain the expected and actual line pitch for the given line.
    float expectedLinePitch = textLines.getMostCommonLinePitch(line);
    float actualLinePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);

    // TODO
    return actualLinePitch - expectedLinePitch > 1;
  }

  /**
   * Checks if the line pitch between the given line and its previous line is
   * larger than the line pitch between the given line and its next line.
   * 
   * @param prevLine
   *        The previous text line.
   * @param line
   *        The text line to process.
   * @param nextLine
   *        The next text line.
   * @return True, if the line pitch between the given line and its previous
   *         line is larger than the line pitch between the given line and its
   *         next line, flase otherwise.
   */
  protected static boolean isLinePitchLargerThanNextLinePitch(
      PdfTextLine prevLine, PdfTextLine line, PdfTextLine nextLine) {
    float linePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);
    float nextLinePitch = PdfTextLineUtils.computeLinePitch(line, nextLine);

    // TODO
    return linePitch - nextLinePitch > 1;
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
   * @return True, if (1) the line pitches between the lines are equal, (2)
   *         prevLine and nextLine do not start with an reference anchor and
   *         (3) the line is indented compared to the previous and the next
   *         line.
   */
  protected boolean isIndented(PdfTextLine prevLine, PdfTextLine line,
      PdfTextLine nextLine) {
    // The line pitches between the lines must be equal.
    if (!PdfTextLineUtils.isLinepitchesEqual(prevLine, line, nextLine)) {
      return false;
    }

    // The previous and next line must not start with an reference anchor.
    boolean hasPrevLineReferenceAnchor = startsWithReferenceAnchor(prevLine);
    boolean hasNextLineReferenceAnchor = startsWithReferenceAnchor(nextLine);
    if (hasPrevLineReferenceAnchor && hasNextLineReferenceAnchor) {
      return false;
    }

    // Check if the line is indented compared to the previous and next line.
    boolean isIndentedToPrevLine = PdfTextLineUtils.isIndented(line, prevLine);
    boolean isIndentedToNextLine = PdfTextLineUtils.isIndented(line, nextLine);

    // Check if the minX values of the previous and the next lines are equal.
    boolean isMinXEqual = PdfTextLineUtils.isMinXEqual(prevLine, nextLine);

    return isIndentedToPrevLine && isIndentedToNextLine && isMinXEqual;
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
   * The pattern to identify reference anchors.
   */
  protected static final Pattern REFERENCE_ANCHOR = Pattern
      .compile("^\\[(.*)\\]\\s+");

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
