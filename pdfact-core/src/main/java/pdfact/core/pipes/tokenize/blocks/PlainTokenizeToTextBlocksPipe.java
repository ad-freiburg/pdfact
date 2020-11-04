package pdfact.core.pipes.tokenize.blocks;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Font;
import pdfact.core.model.FontFace;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.TextLineStatistic;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.statistician.TextLineStatistician;

/**
 * A plain implementation of {@link TokenizeToTextBlocksPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainTokenizeToTextBlocksPipe implements TokenizeToTextBlocksPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainTokenizeToTextBlocksPipe.class);

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician characterStatistician;

  /**
   * The statistician to compute statistics about text lines.
   */
  protected TextLineStatistician textLineStatistician;

  /**
   * The number of processed text lines.
   */
  protected int numProcessedTextLines;

  /**
   * The number of tokenized text blocks.
   */
  protected int numTokenizedTextBlocks;

  /**
   * The default constructor.
   */
  public PlainTokenizeToTextBlocksPipe() {
    this.characterStatistician = new CharacterStatistician();
    this.textLineStatistician = new TextLineStatistician();
  }

  // ==============================================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Tokenizing the text lines into text blocks.");
    tokenizeToTextBlocks(pdf);

    log.debug("Tokenizing the text lines into text blocks done.");
    log.debug("# processed text lines : " + this.numProcessedTextLines);
    log.debug("# tokenized text blocks: " + this.numTokenizedTextBlocks);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==============================================================================================

  /**
   * Tokenizes the text lines in the pages of the given PDF document into text
   * blocks.
   * 
   * @param pdf The PDF document to process.
   * 
   * @throws PdfActException If something went wrong while tokenization.
   */
  protected void tokenizeToTextBlocks(PdfDocument pdf) throws PdfActException {
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      page.setTextBlocks(tokenizeToTextBlocks(pdf, page));
    }
  }

  // ==============================================================================================

  /**
   * Tokenizes the text lines in the given page into text lines.
   * 
   * @param pdf  The PDF document to which the given page belongs to.
   * @param page The PDF page to process.
   * 
   * @return The list of text blocks.
   * 
   * @throws PdfActException If something went wrong while tokenization.
   */
  protected ElementList<TextBlock> tokenizeToTextBlocks(PdfDocument pdf, Page page) throws PdfActException {
    ElementList<TextBlock> textBlocks = new ElementList<>();
    TextBlock textBlock = new TextBlock();

    ElementList<TextLine> lines = page.getTextLines();
    for (int i = 0; i < lines.size(); i++) {
      TextLine prev = i > 0 ? lines.get(i - 1) : null;
      TextLine line = lines.get(i);
      TextLine next = i < lines.size() - 1 ? lines.get(i + 1) : null;

      this.numProcessedTextLines++;

      if (introducesNewTextBlock(pdf, page, textBlock, prev, line, next)) {
        if (!textBlock.getTextLines().isEmpty()) {
          textBlocks.add(textBlock);
        }
        // Create a new text block.
        textBlock = new TextBlock();
      }
      // Add the current line to the current text block.
      textBlock.addTextLine(line);
    }

    // Don't forget the remaining text block.
    if (!textBlock.getTextLines().isEmpty()) {
      textBlocks.add(textBlock);
    }

    // Iterate through the text blocks in order to compute their properties.
    for (TextBlock block : textBlocks) {
      block.setCharacterStatistic(computeCharacterStatistic(block));
      block.setTextLineStatistic(computeTextLineStatistic(block));
      block.setPosition(computePosition(page, block));
      block.setText(computeText(block));
    }

    this.numTokenizedTextBlocks += textBlocks.size();

    return textBlocks;
  }

  // ==============================================================================================

  /**
   * Computes the character statistic for the given text block.
   *
   * @param block The text block to process.
   * 
   * @return The character statistic for the given text block.
   */
  protected CharacterStatistic computeCharacterStatistic(TextBlock block) {
    return this.characterStatistician.aggregate(block.getTextLines());
  }

  /**
   * Computes the text line statistic for the given text block.
   *
   * @param block The text block to process.
   * 
   * @return The text line statistic for the given text block.
   */
  protected TextLineStatistic computeTextLineStatistic(TextBlock block) {
    return this.textLineStatistician.compute(block.getTextLines());
  }

  /**
   * Computes the position for the given text block.
   *
   * @param page  The PDF page in which the block is located.
   * @param block The text block to process.
   * @return The position for the given text block.
   */
  protected Position computePosition(Page page, TextBlock block) {
    ElementList<TextLine> textLines = block.getTextLines();
    Rectangle rect = Rectangle.fromHasPositionElements(textLines);
    return new Position(page, rect);
  }

  /**
   * Computes the text for the given text block.
   *
   * @param block The text block to process.
   * @return The text for the given text block.
   */
  protected String computeText(TextBlock block) {
    return PdfActUtils.join(block.getTextLines(), " ");
  }

  // ==============================================================================================

  /**
   * Checks if the given text line introduces a new text block.
   *
   * @param pdf              The PDF document to which the given text line belongs
   *                         to.
   * @param page             The PDF page to which the given text line belongs to.
   * @param currentTextBlock The current text block.
   * @param prevLine         The previous text line.
   * @param line             The current text line to process.
   * @param nextLine         The next text line.
   * 
   * @return True, if the given current text line introduces a new text block;
   *         false otherwise.
   */
  protected boolean introducesNewTextBlock(PdfDocument pdf, Page page, TextBlock currentTextBlock, TextLine prevLine,
      TextLine line, TextLine nextLine) {
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
    if (isLinepitchLargerThanExpected(pdf, page, prevLine, line)) {
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
   * @param block The text block to process.
   * @param line  The text line to process.
   * @return True, if the given line overlaps the given text block horizontally,
   *         false otherwise.
   */
  protected boolean overlapsHorizontally(TextBlock block, TextLine line) {
    if (block == null || line == null) {
      return false;
    }

    ElementList<TextLine> lines = block.getTextLines();
    Rectangle blockRect = Rectangle.fromHasPositionElements(lines);
    Rectangle lineRect = line.getPosition().getRectangle();
    if (blockRect == null || lineRect == null) {
      return false;
    }

    return blockRect.overlapsHorizontally(lineRect);
  }

  /**
   * Checks if the line pitch between the given line and the given previous line
   * is larger than expected (larger than the most common line pitch for the font
   * / font size pair of the given line).
   *
   * @param pdf      The PDF document to which the given text line belongs to.
   * @param page     The PDF page to which the given text line belongs to.
   * @param prevLine The previous text line.
   * @param line     The text line to process.
   * @return True, if the line pitch between the given text line and the given
   *         previous text line is larger than usual; False otherwise.
   */
  protected static boolean isLinepitchLargerThanExpected(PdfDocument pdf, Page page, TextLine prevLine, TextLine line) {
    if (pdf == null) {
      return false;
    }

    // Obtain the expected and actual line pitch for the given line.
    CharacterStatistic characterStats = line.getCharacterStatistic();
    TextLineStatistic textLineStats = pdf.getTextLineStatistic();

    FontFace fontFace = characterStats.getMostCommonFontFace();
    float expectedLinePitch = textLineStats.getMostCommonLinePitch(fontFace);
    float actualLinePitch = computeLinePitch(prevLine, line);

    // TODO
    return actualLinePitch - expectedLinePitch > 1;
  }

  /**
   * Checks if the line pitch between the given line and its previous line is
   * larger than the line pitch between the given line and its next line.
   *
   * @param prevLine The previous text line.
   * @param line     The text line to process.
   * @param nextLine The next text line.
   * @return True, if the line pitch between the given line and its previous line
   *         is larger than the line pitch between the given line and its next
   *         line, flase otherwise.
   */
  protected static boolean isLinePitchLargerThanNextLinePitch(TextLine prevLine, TextLine line, TextLine nextLine) {
    float linePitch = computeLinePitch(prevLine, line);
    float nextLinePitch = computeLinePitch(line, nextLine);

    // TODO
    return linePitch - nextLinePitch > 1;
  }

  /**
   * Checks, if the given line is indented compared to the given previous line and
   * the given next line.
   *
   * @param prevLine The previous line.
   * @param line     The line to process.
   * @param nextLine The next line.
   * @return True, if (1) the line pitches between the lines are equal, (2)
   *         prevLine and nextLine do not start with an reference anchor and (3)
   *         the line is indented compared to the previous and the next line.
   */
  protected boolean isIndented(TextLine prevLine, TextLine line, TextLine nextLine) {
    // The line pitches between the lines must be equal.
    if (!isLinepitchesEqual(prevLine, line, nextLine)) {
      return false;
    }

    // The previous and next line must not start with an reference anchor.
    boolean hasPrevLineReferenceAnchor = startsWithReferenceAnchor(prevLine);
    boolean hasNextLineReferenceAnchor = startsWithReferenceAnchor(nextLine);
    if (hasPrevLineReferenceAnchor && hasNextLineReferenceAnchor) {
      return false;
    }

    // Check if the line is indented compared to the previous and next line.
    boolean isIndentedToPrevLine = isIndented(line, prevLine);
    boolean isIndentedToNextLine = isIndented(line, nextLine);

    // Check if the minX values of the previous and the next lines are equal.
    boolean isMinXEqual = isMinXEqual(prevLine, nextLine);

    return isIndentedToPrevLine && isIndentedToNextLine && isMinXEqual;
  }

  /**
   * Checks if the line pitches between the line / previous line and the line /
   * next line are equal.
   * 
   * @param prevLine The previous text line.
   * @param line     The text line to process.
   * @param nextLine The next text line.
   * 
   * @return True, if the line pitches between the line / previous line and the
   *         line / next line are equal, false otherwise.
   */
  public static boolean isLinepitchesEqual(TextLine prevLine, TextLine line, TextLine nextLine) {
    float prevLinePitch = computeLinePitch(prevLine, line);
    float nextLinePitch = computeLinePitch(line, nextLine);
    // TODO
    return Math.abs(prevLinePitch - nextLinePitch) < 1;
  }

  /**
   * Computes the line pitch between the given lines. Both lines must share the
   * same page.
   * 
   * @param firstLine  The first text line.
   * @param secondLine The second text line.
   * @return The line pitch between the given lines or Float.NaN if the lines do
   *         not share the same page.
   */
  public static float computeLinePitch(TextLine firstLine, TextLine secondLine) {
    if (firstLine == null || secondLine == null) {
      return Float.NaN;
    }

    if (firstLine.getPosition().getPage() != secondLine.getPosition().getPage()) {
      return Float.NaN;
    }

    Line firstBaseLine = firstLine.getBaseline();
    Line secondBaseLine = secondLine.getBaseline();
    if (firstBaseLine == null || secondBaseLine == null) {
      return Float.NaN;
    }

    return Math.abs(firstBaseLine.getStartY() - secondBaseLine.getStartY());
  }

  /**
   * Checks if the given line has a special font face, compared to the given
   * previous line.
   *
   * @param prevLine The previous line of the line to process.
   * @param line     The line to process.
   * @return True, if the given line has a special font face, False otherwise.
   */
  protected static boolean hasSignificantDifferentFontFace(TextLine prevLine, TextLine line) {
    if (prevLine == null || line == null) {
      return false;
    }

    // If the font of the previous line and the font of the current line are
    // not from the same base, the line has a special font face.
    CharacterStatistic prevLineCharStats = prevLine.getCharacterStatistic();
    CharacterStatistic lineCharStats = line.getCharacterStatistic();
    if (prevLineCharStats == null || lineCharStats == null) {
      return false;
    }

    FontFace prevLineFontFace = prevLineCharStats.getMostCommonFontFace();
    FontFace lineFontFace = lineCharStats.getMostCommonFontFace();
    if (prevLineFontFace == null || lineFontFace == null) {
      return false;
    }

    Font prevLineFont = prevLineFontFace.getFont();
    Font lineFont = lineFontFace.getFont();
    if (prevLineFont == null || lineFont == null) {
      return false;
    }

    String prevLineFontFamilyName = prevLineFont.getFontFamilyName();
    String lineFontFamilyName = lineFont.getFontFamilyName();

    if (prevLineFontFamilyName == null && lineFontFamilyName != null) {
      return true;
    }
    if (prevLineFontFamilyName != null && lineFontFamilyName == null) {
      return true;
    }
    if (prevLineFontFamilyName != null && !prevLineFontFamilyName.equals(lineFontFamilyName)) {
      return true;
    }

    float prevLineFontsize = prevLineFontFace.getFontSize();
    float lineFontsize = lineFontFace.getFontSize();

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
   * @param prevLine The previous line of the line to process.
   * @param line     The line to process.
   * @param nextLine The next line of the line to process.
   * @return True, if the given line is (probably) a reference start.
   */
  protected boolean isProbablyReferenceStart(TextLine prevLine, TextLine line, TextLine nextLine) {
    if (prevLine == null || line == null || nextLine == null) {
      return false;
    }

    // The line must start with an reference anchor.
    if (!startsWithReferenceAnchor(line)) {
      return false;
    }

    Rectangle prevLineRect = prevLine.getPosition().getRectangle();
    Rectangle lineRect = line.getPosition().getRectangle();
    Rectangle nextLineRect = nextLine.getPosition().getRectangle();
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
  protected static final Pattern REFERENCE_ANCHOR = Pattern.compile("^\\[(.*)\\]\\s+");

  /**
   * Checks is the given text line starts with a reference anchor like "[1]",
   * "[2]", etc.
   *
   * @param line The text line to check.
   * @return True, if the given text line starts with a reference anchor, false
   *         otherwise.
   */
  // TODO: Move to any util.
  protected boolean startsWithReferenceAnchor(TextLine line) {
    if (line == null) {
      return false;
    }

    String text = line.getText();
    if (text == null) {
      return false;
    }

    return REFERENCE_ANCHOR.matcher(text).find();
  }

  /**
   * Checks if the given line is indented, compared to the given reference line.
   * 
   * @param line    The text line to process.
   * @param refLine The reference text line.
   * 
   * @return True, if the given line is indented compared to the given reference
   *         line.
   */
  public static boolean isIndented(TextLine line, TextLine refLine) {
    if (line == null || refLine == null) {
      return false;
    }

    Rectangle rectangle = line.getPosition().getRectangle();
    Rectangle referenceRectangle = refLine.getPosition().getRectangle();
    if (rectangle == null || referenceRectangle == null) {
      return false;
    }

    // TODO
    return rectangle.getMinX() - referenceRectangle.getMinX() > 1;
  }

  /**
   * Checks, if the minX values for the given lines are equal.
   * 
   * @param line1 The first text line.
   * @param line2 The second text line-
   * @return True, if the minX values for the given lines are equal, false
   *         otherwise.
   */
  public static boolean isMinXEqual(TextLine line1, TextLine line2) {
    if (line1 == null || line2 == null) {
      return false;
    }

    Rectangle rectangle1 = line1.getPosition().getRectangle();
    Rectangle rectangle2 = line2.getPosition().getRectangle();
    if (rectangle1 == null || rectangle2 == null) {
      return false;
    }

    // TODO
    return Math.abs(rectangle1.getMinX() - rectangle2.getMinX()) < 1;
  }
}