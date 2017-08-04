package icecite.tokenize.lines;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfPosition;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfWordList;
import icecite.tokenize.areas.PdfTextAreaSegmenter;
import icecite.tokenize.words.PdfWordTokenizer;
import icecite.utils.character.PdfCharacterUtils;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.comparators.MinXComparator;
import icecite.utils.counter.FloatCounter;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Line.LineFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfTextLineTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineTokenizer implements PdfTextLineTokenizer {
  /**
   * The text area tokenizer.
   */
  protected PdfTextAreaSegmenter textAreaSegmenter;

  /**
   * The text line classifier;
   */
  protected PdfTextLineSegmenter lineSegmenter;

  /**
   * The word tokenizer.
   */
  protected PdfWordTokenizer wordTokenizer;

  /**
   * The factory to create instances of {@link PdfTextLineList}.
   */
  protected PdfTextLineListFactory textLineListFactory;

  /**
   * The factory to create instances of {@link PdfTextLine}.
   */
  protected PdfTextLineFactory textLineFactory;

  /**
   * The characters statistician.
   */
  protected PdfCharacterStatistician charStatistician;

  /**
   * The factory to create instances of PdfPosition.
   */
  protected PdfPositionFactory positionFactory;

  /**
   * The factory to create instances of Rectangle.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The factory to create instances of Line.
   */
  protected LineFactory lineFactory;

  /**
   * The default constructor.
   * 
   * @param textAreaTokenizer
   *        The text area tokenizer.
   * @param textLineClassifier
   *        The text line classifier.
   * @param wordTokenizer
   *        The tokenizer to tokenize words.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   * @param textLineFactory
   *        The factory to create instances of {@link PdfTextLine}.
   * @param characterStatistician
   *        The character statistician.
   * @param positionFactory
   *        The factory to create instances of {@link PdfPosition}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param lineFactory
   *        The factory to create instances of {@link Line}.
   */
  @Inject
  public PlainPdfTextLineTokenizer(PdfTextAreaSegmenter textAreaTokenizer,
      PdfTextLineSegmenter textLineClassifier,
      PdfWordTokenizer wordTokenizer,
      PdfTextLineListFactory textLineListFactory,
      PdfTextLineFactory textLineFactory,
      PdfCharacterStatistician characterStatistician,
      PdfPositionFactory positionFactory,
      RectangleFactory rectangleFactory,
      LineFactory lineFactory) {
    this.textAreaSegmenter = textAreaTokenizer;
    this.lineSegmenter = textLineClassifier;
    this.wordTokenizer = wordTokenizer;
    this.textLineListFactory = textLineListFactory;
    this.textLineFactory = textLineFactory;
    this.charStatistician = characterStatistician;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
    this.lineFactory = lineFactory;
  }

  @Override
  public PdfTextLineList tokenize(PdfDocument pdf, PdfPage page) {
    PdfTextLineList textLines = this.textLineListFactory.create();

    // Segment the characters of the page into text area segments.
    List<PdfCharacterList> textAreaSegments = this.textAreaSegmenter
        .segment(pdf, page, page.getCharacters());

    // Segment each text area segment into text line segments.
    for (PdfCharacterList textAreaSegment : textAreaSegments) {
      List<PdfCharacterList> lineSegments = this.lineSegmenter.segment(pdf,
          page, textAreaSegment);

      // Segment each text line segment into words.
      for (PdfCharacterList lineSegment : lineSegments) {
        // Tokenize the segment into words.
        PdfWordList words = this.wordTokenizer.tokenize(pdf, page, lineSegment);

        // Create a PdfTextLine object.
        PdfTextLine textLine = this.textLineFactory.create();
        textLine.setWords(words);
        textLine.setText(computeText(textLine));
        textLine.setBaseline(computeBaseline(lineSegment));
        textLine.setPosition(computePosition(page, textLine));
        textLine.setCharacterStatistic(computeCharStatistics(textLine));
        textLines.add(textLine);
      }
    }
    return textLines;
  }

  // ==========================================================================

  /**
   * Computes the character statistics for the given text line.
   * 
   * @param line
   *        The text line to process.
   * @return The character statistics for the given text line.
   */
  protected PdfCharacterStatistic computeCharStatistics(PdfTextLine line) {
    return this.charStatistician.combine(line.getWords());
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
  protected PdfPosition computePosition(PdfPage page, PdfTextLine line) {
    Rectangle rect = this.rectangleFactory.computeBoundingBox(line.getWords());
    return this.positionFactory.create(page, rect);
  }

  /**
   * Computes the baseline from the given characters of a line.
   * 
   * @param characters
   *        The list of characters to process.
   * @return The computed baseline.
   */
  protected Line computeBaseline(PdfCharacterList characters) {
    Line baseLine = null;
    // TODO: Inject!
    FloatCounter minYCounter = new FloatCounter();

    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new MinXComparator());

      float minX = Float.MAX_VALUE;
      float maxX = -Float.MAX_VALUE;
      for (PdfCharacter character : characters) {
        if (PdfCharacterUtils.isBaselineCharacter(character)) {
          minYCounter.add(character.getRectangle().getMinY());
        }

        minX = Math.min(minX, character.getRectangle().getMinX());
        maxX = Math.max(maxX, character.getRectangle().getMaxX());
      }

      if (!minYCounter.isEmpty()) {
        float minY = minYCounter.getMostCommonFloat();
        baseLine = this.lineFactory.create(minX, minY, maxX, minY);
      }
    }

    return baseLine;
  }

  /**
   * Computes the text for the given text line.
   * 
   * @param line
   *        The text line to process.
   * @return The text for the given text line.
   */
  protected String computeText(PdfTextLine line) {
    return CollectionUtils.join(line.getWords(), " ");
  }
}
