package icecite.tokenize.words;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.tokenize.xycut.XYCut;

/**
 * An implementation of {@link PdfWordTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfWordTokenizer extends XYCut<PdfWord>
    implements PdfWordTokenizer {
  /**
   * The factory to create instances of {@link PdfPositionFactory}.
   */
  protected PdfPositionFactory positionFactory;
  
  /**
   * The factory to create instances of {@link PdfWordList}.
   */
  protected PdfWordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link PdfWord}.
   */
  protected PdfWordFactory wordFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new word tokenizer.
   * @param positionFactory 
   *        The factory to create instance of PdfPosition.
   * @param wordListFactory
   *        The factory to create instance of {@link PdfWordList}.
   * @param wordFactory
   *        The factory to create instance of {@link PdfWord}.
   */
  @Inject
  public XYCutPdfWordTokenizer(PdfPositionFactory positionFactory, 
      PdfWordListFactory wordListFactory, PdfWordFactory wordFactory) {
    super();
    this.positionFactory = positionFactory;
    this.wordListFactory = wordListFactory;
    this.wordFactory = wordFactory;
  }

  // ==========================================================================

  @Override
  public PdfWordList tokenize(PdfDocument pdf, PdfPage page, PdfTextLine line) {
    PdfWordList words = this.wordListFactory.create();
    if (line != null) {
      cut(pdf, page, line.getCharacters(), words);
    }
    return words;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList l = halves.get(0);
    PdfCharacterList r = halves.get(1);

    float width = r.getRectangle().getMinX() - l.getRectangle().getMaxX();
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

  @Override
  public PdfWord pack(PdfPage page, PdfCharacterList chars) {
    PdfWord word = this.wordFactory.create(chars);
    word.setPosition(this.positionFactory.create(page, chars.getRectangle()));
    
    return word;
  }

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
