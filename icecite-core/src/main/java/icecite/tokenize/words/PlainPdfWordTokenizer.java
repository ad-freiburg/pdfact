package icecite.tokenize.words;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfPosition;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.utils.character.PdfCharacterUtils;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.comparators.MinXComparator;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfWordTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWordTokenizer implements PdfWordTokenizer {
  /**
   * The factory to create instances of {@link PdfWordListFactory}.
   */
  protected PdfWordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link PdfWordFactory}.
   */
  protected PdfWordFactory wordFactory;

  /**
   * The word classifier.
   */
  protected PdfWordSegmenter segmenter;

  /**
   * The character statistician.
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
   * The default constructor.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link PdfWordListFactory}.
   * @param wordFactory
   *        The factory to create instances of {@link PdfWordFactory}.
   * @param segmenter
   *        The word segmenter.
   * @param characterStatistician
   *        The character statistician.
   * @param positionFactory
   *        The factory to create instances of {@link PdfPosition}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   */
  @Inject
  public PlainPdfWordTokenizer(
      PdfWordListFactory wordListFactory,
      PdfWordFactory wordFactory,
      PdfWordSegmenter segmenter,
      PdfCharacterStatistician characterStatistician,
      PdfPositionFactory positionFactory,
      RectangleFactory rectangleFactory) {
    this.wordListFactory = wordListFactory;
    this.wordFactory = wordFactory;
    this.segmenter = segmenter;
    this.charStatistician = characterStatistician;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
  }

  @Override
  public PdfWordList tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList line) {
    PdfWordList words = this.wordListFactory.create();

    // Segment the given characters of a line into word segments.
    List<PdfCharacterList> segments = this.segmenter.segment(pdf, page, line);

    PdfWord word = null;
    // Create the PdfWord objects.
    for (PdfCharacterList segment : segments) {
      word = this.wordFactory.create();
      word.setCharacters(segment);
      word.setText(computeText(word));
      word.setPosition(computePosition(page, word));
      word.setCharacterStatistic(computeCharStatistics(word));
      words.add(word);
    }

    // Check if the last word in the line is hyphenated.
    if (word != null) {
      word.setIsHyphenated(computeIsHyphenated(word));
    }

    return words;
  }

  // ==========================================================================

  /**
   * Computes the character statistics for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The character statistics for the given word.
   */
  protected PdfCharacterStatistic computeCharStatistics(PdfWord word) {
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
  protected PdfPosition computePosition(PdfPage page, PdfWord word) {
    PdfCharacterList characters = word.getCharacters();
    Rectangle rect = this.rectangleFactory.computeBoundingBox(characters);
    return this.positionFactory.create(page, rect);
  }

  /**
   * Computes the text for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The text for the given word.
   */
  protected String computeText(PdfWord word) {
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
  protected boolean computeIsHyphenated(PdfWord word) {
    if (word == null) {
      return false;
    }

    PdfCharacterList characters = word.getCharacters();
    if (characters == null || characters.size() < 2) {
      return false;
    }

    return PdfCharacterUtils.isHyphen(word.getLastCharacter());
  }
}
