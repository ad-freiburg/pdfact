package pdfact.pipes.tokenize.lines.lines;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import pdfact.model.Character;
import pdfact.model.CharacterStatistic;
import pdfact.model.Line;
import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.Position;
import pdfact.model.Rectangle;
import pdfact.model.Position.PositionFactory;
import pdfact.model.Rectangle.RectangleFactory;
import pdfact.model.TextArea;
import pdfact.model.TextLine;
import pdfact.model.TextLine.TextLineFactory;
import pdfact.pipes.tokenize.lines.words.WordTokenizer;
import pdfact.pipes.tokenize.lines.words.WordTokenizer.WordTokenizerFactory;
import pdfact.pipes.tokenize.xycut.XYCut;
import pdfact.util.CharacterUtils;
import pdfact.util.CollectionUtils;
import pdfact.util.comparator.MinXComparator;
import pdfact.util.counter.FloatCounter;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.CharacterList;
import pdfact.util.list.TextLineList;
import pdfact.util.list.WordList;
import pdfact.util.list.TextLineList.TextLineListFactory;
import pdfact.util.statistic.CharacterStatistician;
import pdfact.model.Line.LineFactory;

/**
 * An implementation of {@link TextLineTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutTextLineTokenizer extends XYCut
    implements TextLineTokenizer {
  /**
   * The factory to create instances of {@link TextLineList}.
   */
  protected TextLineListFactory textLineListFactory;

  /**
   * The factory to create instances of {@link TextLine}.
   */
  protected TextLineFactory textLineFactory;

  /**
   * The factory to create instances of {@link WordTokenizer}.
   */
  protected WordTokenizerFactory wordTokenizerFactory;

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician charStatistician;

  /**
   * The factory to create instances of {@link Position}.
   */
  protected PositionFactory positionFactory;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The factory to create instances of {@link Line}.
   */
  protected LineFactory lineFactory;

  /**
   * Creates a new text line tokenizer.
   * 
   * @param textLineListFactory
   *        The factory to create instances of {@link TextLineList}.
   * @param textLineFactory
   *        The factory to create instances of {@link TextLine}.
   * @param wordTokenizerFactory
   *        The factory to create instances of {@link WordTokenizer}.
   * @param characterStatistician
   *        The statistician to compute statistics about characters.
   * @param positionFactory
   *        The factory to create instances of {@link Position}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param lineFactory
   *        The factory to create instances of {@link Line}.
   */
  @Inject
  public XYCutTextLineTokenizer(
      TextLineListFactory textLineListFactory,
      TextLineFactory textLineFactory,
      WordTokenizerFactory wordTokenizerFactory,
      CharacterStatistician characterStatistician,
      PositionFactory positionFactory,
      RectangleFactory rectangleFactory,
      LineFactory lineFactory) {
    this.textLineListFactory = textLineListFactory;
    this.textLineFactory = textLineFactory;
    this.wordTokenizerFactory = wordTokenizerFactory;
    this.charStatistician = characterStatistician;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
    this.lineFactory = lineFactory;
  }

  // ==========================================================================

  @Override
  public TextLineList tokenize(PdfDocument pdf, Page page,
      List<TextArea> areas) throws PdfActException {
    TextLineList result = this.textLineListFactory.create();

    for (TextArea area : areas) {
      List<CharacterList> charLists = cut(pdf, page, area.getCharacters());

      for (CharacterList charList : charLists) {
        // Create a PdfTextLine object.
        TextLine textLine = this.textLineFactory.create();
        textLine.setBaseline(computeBaseline(charList));
        textLine.setCharacterStatistic(computeCharStats(charList));
        textLine.setWords(tokenizeIntoWords(pdf, page, textLine, charList));
        textLine.setText(computeText(textLine));
        textLine.setPosition(computePosition(page, textLine));
        result.add(textLine);
      }
    }

    return result;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    return -1;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    CharacterList upper = halves.get(0);
    CharacterStatistic upperStats = this.charStatistician.compute(upper);
    float upperMinY = upperStats.getSmallestMinY();

    CharacterList lower = halves.get(1);
    CharacterStatistic lowerStats = this.charStatistician.compute(lower);
    float lowerMaxY = lowerStats.getLargestMaxY();

    return upperMinY - lowerMaxY;
  }

  // ==========================================================================

  /**
   * Tokenizes the given text line into words.
   * 
   * @param pdf
   *        The PDF document to which the given text line belongs to.
   * @param page
   *        The PDF page to which the given text line belongs to.
   * @param line
   *        The line to tokenize.
   * @param chars
   *        The characters of the text line to tokenize.
   * @return The list of identified words.
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected WordList tokenizeIntoWords(PdfDocument pdf, Page page,
      TextLine line, CharacterList chars) throws PdfActException {
    return this.wordTokenizerFactory.create().tokenize(pdf, page, line, chars);
  }

  /**
   * Computes the text of the given text line.
   * 
   * @param line
   *        The line to process.
   * 
   * @return The text of the given line.
   */
  protected String computeText(TextLine line) {
    return CollectionUtils.join(line.getWords(), " ");
  }

  /**
   * Computes the character statistics for the given text line.
   * 
   * @param chars
   *        The characters to process.
   * @return The character statistics for the given text line.
   */
  protected CharacterStatistic computeCharStats(CharacterList chars) {
    return this.charStatistician.compute(chars);
  }

  /**
   * Computes the position for the given text line.
   * 
   * @param page
   *        The PDF page in which the line is located.
   * @param line
   *        The text line to process.
   * @return The position for the given text line.
   */
  protected Position computePosition(Page page, TextLine line) {
    WordList words = line.getWords();
    Rectangle rect = this.rectangleFactory.fromHasPositionsElements(words);
    return this.positionFactory.create(page, rect);
  }

  /**
   * Computes the baseline from the given characters of a line.
   * 
   * @param characters
   *        The list of characters to process.
   * @return The computed baseline.
   */
  protected Line computeBaseline(CharacterList characters) {
    Line baseLine = null;
    // TODO: Inject!
    FloatCounter minYCounter = new FloatCounter();

    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new MinXComparator());

      float minX = Float.MAX_VALUE;
      float maxX = -Float.MAX_VALUE;
      for (Character character : characters) {
        if (CharacterUtils.isBaselineCharacter(character)) {
          minYCounter.add(character.getPosition().getRectangle().getMinY());
        }

        minX = Math.min(minX, character.getPosition().getRectangle().getMinX());
        maxX = Math.max(maxX, character.getPosition().getRectangle().getMaxX());
      }

      if (!minYCounter.isEmpty()) {
        float minY = minYCounter.getMostCommonFloat();
        baseLine = this.lineFactory.create(minX, minY, maxX, minY);
      }
    }

    return baseLine;
  }
}