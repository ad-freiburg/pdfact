package icecite.tokenize.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfDocument;
import icecite.models.PdfFont;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfPosition;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfTextLineStatistician;
import icecite.models.PdfTextLineStatistic;
import icecite.tokenize.lines.PdfTextLineTokenizer;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;
import icecite.utils.textlines.PdfTextLineUtils;

/**
 * A plain implementation of {@link PdfTextBlockTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlockTokenizer implements PdfTextBlockTokenizer {
  /**
   * The text line tokenizer.
   */
  protected PdfTextLineTokenizer textLineTokenizer;

  /**
   * The factory to create instances of {@link PdfTextBlock}.
   */
  protected PdfTextBlockFactory textBlockFactory;

  /**
   * The factory to create instances of PdfTextLineListFactory.
   */
  protected PdfTextLineListFactory textLineListFactory;

  /**
   * The characters statistician.
   */
  protected PdfCharacterStatistician charStatistician;

  /**
   * The text line statistician.
   */
  protected PdfTextLineStatistician textLineStatistician;

  /**
   * The factory to create instances of PdfPosition.
   */
  protected PdfPositionFactory positionFactory;

  /**
   * The factory to create instances of Rectangle.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The default constructor.
   * 
   * @param textLineTokenizer
   *        The text line tokenizer.
   * @param textBlockFactory
   *        The factory to create instances of {@link PdfTextBlock}.
   * @param textLineListFactory
   *        The text line list factory.
   * @param characterStatistician
   *        The character statistician.
   * @param textLineStatistician
   *        The text line statistician.
   * @param positionFactory
   *        The factory to create instances of {@link PdfPosition}.
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @Inject
  public PlainPdfTextBlockTokenizer(PdfTextLineTokenizer textLineTokenizer,
      PdfTextBlockFactory textBlockFactory,
      PdfTextLineListFactory textLineListFactory,
      PdfCharacterStatistician characterStatistician,
      PdfTextLineStatistician textLineStatistician,
      PdfPositionFactory positionFactory,
      RectangleFactory rectangleFactory) {
    this.textLineTokenizer = textLineTokenizer;
    this.textBlockFactory = textBlockFactory;
    this.textLineListFactory = textLineListFactory;
    this.charStatistician = characterStatistician;
    this.textLineStatistician = textLineStatistician;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
  }

  @Override
  public List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page, PdfTextLineList lines) {
    // Then, identify the text blocks from the text lines.
    return identifyTextBlocks(pdf, page, lines);
  }

  // ==========================================================================

  /**
   * Identifies the text blocks from the given text lines.
   * 
   * @param pdf
   *        The PDF document to which the text lines belong to.
   * @param page
   *        The PDF page to which the text lines belong to.
   * @param lines
   *        The text lines to process.
   * 
   * @return The list of identified text blocks.
   */
  protected List<PdfTextBlock> identifyTextBlocks(PdfDocument pdf, PdfPage page,
      PdfTextLineList lines) {
    List<PdfTextBlock> textBlocks = new ArrayList<>();
    PdfTextBlock textBlock = this.textBlockFactory.create();

    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

      if (introducesNewTextBlock(pdf, textBlock, prevLine, line, nextLine)) {
        if (!textBlock.getTextLines().isEmpty()) {
          textBlocks.add(textBlock);
        }
        // Create a new text block.
        textBlock = this.textBlockFactory.create();
      }
      // Add the word of the current line to the current text block.
      textBlock.addTextLine(line);
    }

    // Don't forget the remaining text block.
    if (!textBlock.getTextLines().isEmpty()) {
      textBlocks.add(textBlock);
    }

    // Iterate through the text blocks in order to compute their properties.
    for (PdfTextBlock block : textBlocks) {
      block.setCharacterStatistic(computeCharStatistics(block));
      block.setTextLineStatistic(computeTextLineStatistics(block));
      block.setPosition(computePosition(page, block));
      block.setText(computeText(block));
    }

    return textBlocks;
  }

  /**
   * Checks if the given text line introduces a new text block.
   * 
   * @param pdf
   *        The PDF document.
   * @param currentTextBlock
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
  protected boolean introducesNewTextBlock(PdfDocument pdf,
      PdfTextBlock currentTextBlock, PdfTextLine prevLine, PdfTextLine line,
      PdfTextLine nextLine) {
    // The line does *not* introduce a text block, if it is null.
    if (line == null) {
      return false;
    }

    // The line introduces a text block, if there is no previous line.
    if (prevLine == null) {
      return true;
    }

    // The line introduces a text block, if there is no current text block.
    if (currentTextBlock == null) {
      return true;
    }

    // The line does *not* introduce a text block, if the current text block is
    // empty.
    if (currentTextBlock.getTextLines().isEmpty()) {
      return false;
    }

    // The line introduces a text block, if it doesn't overlap the text
    // block horizontally.
    if (!overlapsHorizontally(currentTextBlock, line)) {
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

    PdfTextLineList blockLines = block.getTextLines();
    Rectangle blockRect = this.rectangleFactory.computeBoundingBox(blockLines);
    Rectangle lineRect = line.getRectangle();
    if (blockRect == null || lineRect == null) {
      return false;
    }

    return blockRect.overlapsHorizontally(lineRect);
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

    // Obtain the expected and actual line pitch for the given line.
    PdfCharacterStatistic characterStats = pdf.getCharacterStatistic();
    PdfTextLineStatistic textLineStats = pdf.getTextLineStatistic();

    PdfFontFace fontFace = characterStats.getMostCommonFontFace();
    float expectedLinePitch = textLineStats.getMostCommonLinePitch(fontFace);
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
   * Checks if the given line has a special font face, compared to the given
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
    PdfCharacterStatistic prevLineCharStats = prevLine
        .getCharacterStatistic();
    PdfFontFace prevLineFontFace = prevLineCharStats.getMostCommonFontFace();
    PdfFont prevLineFont = prevLineFontFace.getFont();
    float prevLineFontsize = prevLineFontFace.getFontSize();

    PdfCharacterStatistic lineCharStats = line.getCharacterStatistic();
    PdfFontFace lineFontFace = lineCharStats.getMostCommonFontFace();
    PdfFont lineFont = lineFontFace.getFont();
    float lineFontsize = lineFontFace.getFontSize();

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

  // ==========================================================================

  /**
   * Computes the character statistics for the given text block.
   * 
   * @param block
   *        The text block to process.
   * @return The character statistics for the given text block.
   */
  protected PdfCharacterStatistic computeCharStatistics(PdfTextBlock block) {
    return this.charStatistician.combine(block.getTextLines());
  }

  /**
   * Computes the text line statistics for the given text block.
   * 
   * @param block
   *        The text block to process.
   * @return The text line statistics for the given text block.
   */
  protected PdfTextLineStatistic computeTextLineStatistics(
      PdfTextBlock block) {
    return this.textLineStatistician.compute(block.getTextLines());
  }

  /**
   * Computes the position for the given text block.
   * 
   * @param page
   *        The PDF page in which the block is located.
   * @param block
   *        The text block to process.
   * @return The position for the given text block.
   */
  protected PdfPosition computePosition(PdfPage page, PdfTextBlock block) {
    PdfTextLineList textLines = block.getTextLines();
    Rectangle rect = this.rectangleFactory.computeBoundingBox(textLines);
    return this.positionFactory.create(page, rect);
  }

  /**
   * Computes the text for the given text block.
   * 
   * @param block
   *        The text block to process.
   * @return The text for the given text block.
   */
  protected String computeText(PdfTextBlock block) {
    return CollectionUtils.join(block.getTextLines(), " ");
  }
}
