package pdfact.tokenize.words;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import pdfact.models.PdfCharacterList;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfCharacterStatistician;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfPosition.PdfPositionFactory;
import pdfact.tokenize.xycut.XYCut;

/**
 * An implementation of {@link PdfWordSegmenter} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfWordSegmenter extends XYCut implements PdfWordSegmenter {
  /**
   * The factory to create instances of {@link PdfPositionFactory}.
   */
  protected PdfPositionFactory positionFactory;
  
  /**
   * The statistician to compute statistics about characters.
   */
  protected PdfCharacterStatistician charStatistician;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new word tokenizer.
   * 
   * @param characterStatistician 
   *        The statistician to compute statistics about characters.
   * @param positionFactory
   *        The factory to create instance of PdfPosition.
   */
  @Inject
  public XYCutPdfWordSegmenter(PdfCharacterStatistician characterStatistician,
      PdfPositionFactory positionFactory) {
    super();
    this.charStatistician = characterStatistician;
    this.positionFactory = positionFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList chars) {
    List<PdfCharacterList> words = new ArrayList<>();
    cut(pdf, page, chars, words);
    return words;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList left = halves.get(0);
    PdfCharacterStatistic leftStats = this.charStatistician.compute(left);
    float leftMaxX = leftStats.getLargestMaxX();
    
    PdfCharacterList right = halves.get(1);
    PdfCharacterStatistic rightStats = this.charStatistician.compute(right);
    float rightMinX = rightStats.getSmallestMinX();
    
    float width = rightMinX - leftMaxX;
    if (width < 1f) {
      return -1;
    }
    return width;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    return -1;
  }

  // ==========================================================================

//   @Override
//   public PdfWordList buildWords(List<PdfCharacterList> wordCharacters) {
//     PdfWordList words = this.wordListFactory.create();
//     
//     for (PdfCharacterList characters : wordCharacters) {
//       PdfWord word = this.wordFactory.create();
//       word.setCharacters(characters);
//       word.setCharacterStatistics(statistics);
//       word.setIsHyphenated(isHyphenated);
//       word.setPosition(position);
//       word.setRectangle(rectangle);
//       word.setText(text);
//       words.add(word);
//     }
//     
//     return words;
//   }

  // ==========================================================================

  // /**
  // * Estimates the width of whitespace in the given area. If this distance is
  // * smaller than the given minValue, the value of minValue is returned.
  // * If there is no proper whitespace was found, the given default value is
  // * returned.
  // */
  // public float estimateWhitespaceWidth(PdfArea area, float minValue,
  // float defaultValue) {
  // List<PdfCharacter> chars = area.getTextCharacters();
  //
  // // Sort the characters of area by minX values to be able to obtain the
  // // distance of a character to its previous and next character.
  // Collections.sort(chars, new MinXComparator());
  //
  // FloatCounter distanceCounter = new FloatCounter();
  //
  // // Iterate through each character and compute its distance to prev and
  // next.
  // for (int i = 0; i < chars.size(); i++) {
  // PdfCharacter prev = i > 0 ? chars.get(i - 1) : null;
  // PdfCharacter curr = chars.get(i);
  // PdfCharacter next = i < chars.size() - 1 ? chars.get(i + 1) : null;
  //
  // if (prev != null && curr != null && next != null) {
  // Rectangle prevRect = prev.getBoundingBox();
  // Rectangle rect = curr.getBoundingBox();
  // Rectangle nextRect = next.getBoundingBox();
  //
  // // Compute distance between prev and curr.
  // float left = MathUtils.floor(rect.getMinX() - prevRect.getMaxX(), 1);
  // // Consider negative distances as "0".
  // left = Math.max(left, 0);
  //
  // // Compute distance between curr and next.
  // float right = MathUtils.floor(nextRect.getMinX() - rect.getMaxX(), 1);
  // // Consider negative distances as "0".
  // right = Math.max(right, 0);
  //
  // // If one of the distances is larger than the other, register it in
  // the
  // // counter.
  // if (MathUtils.isLarger(left, right, 1f)) {
  // distanceCounter.add(left);
  // }
  //
  // if (MathUtils.isLarger(right, left, 1f)) {
  // distanceCounter.add(right);
  // }
  // }
  // }
  //
  // // If there was at least one whitespace found, return the most frequent
  // // width, otherwise return the default value.
  // if (distanceCounter.size() > 0) {
  // return Math.max(distanceCounter.getMostFrequentFloat(), minValue);
  // } else {
  // return defaultValue;
  // }
  // }
}
