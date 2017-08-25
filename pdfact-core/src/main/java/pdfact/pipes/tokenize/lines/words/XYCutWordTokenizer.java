package pdfact.pipes.tokenize.lines.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import pdfact.model.CharacterStatistic;
import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.Position;
import pdfact.model.Rectangle;
import pdfact.model.Position.PositionFactory;
import pdfact.model.Rectangle.RectangleFactory;
import pdfact.model.TextLine;
import pdfact.model.Word;
import pdfact.model.Word.WordFactory;
import pdfact.pipes.tokenize.xycut.XYCut;
import pdfact.util.CharacterUtils;
import pdfact.util.CollectionUtils;
import pdfact.util.comparator.MinXComparator;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.CharacterList;
import pdfact.util.list.WordList;
import pdfact.util.list.WordList.WordListFactory;
import pdfact.util.statistic.CharacterStatistician;

/**
 * An implementation of {@link WordTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutWordTokenizer extends XYCut implements WordTokenizer {
  /**
   * The factory to create instances of {@link WordList}.
   */
  protected WordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link Word}.
   */
  protected WordFactory wordFactory;

  /**
   * The factory to create instances of {@link PositionFactory}.
   */
  protected PositionFactory positionFactory;

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician charStatistician;

  /**
   * The factory to create instances of Rectangle.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * Creates a new word tokenizer.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link WordList}.
   * @param wordFactory
   *        The factory to create instances of {@link Word}.
   * @param characterStatistician
   *        The statistician to compute statistics about characters.
   * @param positionFactory
   *        The factory to create instance of {@link Position}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   */
  @Inject
  public XYCutWordTokenizer(
      WordListFactory wordListFactory,
      WordFactory wordFactory,
      PositionFactory positionFactory,
      CharacterStatistician characterStatistician,
      RectangleFactory rectangleFactory) {
    this.wordListFactory = wordListFactory;
    this.wordFactory = wordFactory;
    this.charStatistician = characterStatistician;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
  }

  // ==========================================================================

  @Override
  public WordList tokenize(PdfDocument pdf, Page page, TextLine line,
      CharacterList lineCharacters) throws PdfActException {

    WordList result = this.wordListFactory.create();

    List<CharacterList> charLists = cut(pdf, page, lineCharacters);
    Word word = null;
    for (CharacterList charList : charLists) {
      word = this.wordFactory.create();
      word.setCharacters(charList);
      word.setText(computeText(word));
      word.setPositions(computePositions(page, word));
      word.setCharacterStatistic(computeCharStatistics(word));
      result.add(word);
    }

    // Check if the last word in the line is hyphenated.
    if (word != null) {
      word.setIsHyphenated(computeIsHyphenated(word));
    }

    return result;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    CharacterList left = halves.get(0);
    CharacterStatistic leftStats = this.charStatistician.compute(left);
    float leftMaxX = leftStats.getLargestMaxX();

    CharacterList right = halves.get(1);
    CharacterStatistic rightStats = this.charStatistician.compute(right);
    float rightMinX = rightStats.getSmallestMinX();

    float width = rightMinX - leftMaxX;
    if (width < 1f) {
      return -1;
    }
    return width;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    return -1;
  }

  // ==========================================================================

  /**
   * Computes the character statistics for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The character statistics for the given word.
   */
  protected CharacterStatistic computeCharStatistics(Word word) {
    return this.charStatistician.compute(word.getCharacters());
  }

  /**
   * Computes the position for the given word.
   * 
   * @param page
   *        The PDF page in which the word is located.
   * @param word
   *        The word to process.
   * @return The position for the given word.
   */
  protected List<Position> computePositions(Page page, Word word) {
    List<Position> positions = new ArrayList<>();
    CharacterList characters = word.getCharacters();
    Rectangle rect = this.rectangleFactory.fromHasPositionElements(characters);
    Position position = this.positionFactory.create(page, rect);
    positions.add(position);
    return positions;
  }

  /**
   * Computes the text for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The text for the given word.
   */
  protected String computeText(Word word) {
    Collections.sort(word.getCharacters(), new MinXComparator());
    return CollectionUtils.join(word.getCharacters(), "");
  }

  /**
   * Checks if the given word is hyphenated.
   * 
   * @param word
   *        The word to check.
   * @return True if the given word is hyphenated; false otherwise.
   */
  protected boolean computeIsHyphenated(Word word) {
    if (word == null) {
      return false;
    }

    CharacterList characters = word.getCharacters();
    if (characters == null || characters.size() < 2) {
      return false;
    }

    return CharacterUtils.isHyphen(word.getLastCharacter());
  }

}