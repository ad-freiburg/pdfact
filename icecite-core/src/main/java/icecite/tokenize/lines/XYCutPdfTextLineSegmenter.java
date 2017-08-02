package icecite.tokenize.lines;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.tokenize.xycut.XYCut;

// TODO: Rework.

/**
 * A plain implementation of {@link PdfTextLineSegmenter}, based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextLineSegmenter extends XYCut
    implements PdfTextLineSegmenter {
  /**
   * The statistician to compute statistics about characters.
   */
  protected PdfCharacterStatistician charStatistician;

  /**
   * The overlapping characters of the previous sweep iteration.
   */
  protected PdfCharacterList prevOverlappingChars;

  /**
   * The flag to indicate whether the lane in the previous sweep iteration was
   * valid.
   */
  protected boolean prevIsValidHorizontalLane;

  /**
   * The flag to indicate whether the previous overlapping characters consist
   * only of ascenders, descenders, sub- or superscripts.
   */
  protected boolean prevContainsOnlyCriticalChars;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text line tokenizer.
   * 
   * @param charStatistician
   *        The statistician to compute statistics about characters.
   */
  @Inject
  public XYCutPdfTextLineSegmenter(PdfCharacterStatistician charStatistician) {
    this.charStatistician = charStatistician;
  }

  // ==========================================================================

  @Override
  public List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList area) {
    List<PdfCharacterList> textLines = new ArrayList<>();
    cut(pdf, page, area, textLines);
    return textLines;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    return -1;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList upper = halves.get(0);
    PdfCharacterStatistics upperStats = this.charStatistician.compute(upper);
    float upperMinY = upperStats.getSmallestMinY();

    PdfCharacterList lower = halves.get(1);
    PdfCharacterStatistics lowerStats = this.charStatistician.compute(lower);
    float lowerMaxY = lowerStats.getLargestMaxY();

    return upperMinY - lowerMaxY;
  }

  // ==========================================================================

  // @Override
  // public PdfTextLine pack(PdfPage page, PdfCharacterList chars) {
  // PdfTextLine line = this.textLineFactory.create(chars);
  // line.setPosition(this.positionFactory.create(page, chars.getRectangle()));
  // line.setBaseline(computeBaseline(line));
  // return line;
  // }

  // ==========================================================================

  // /**
  // * Computes the baseline for the given text line.
  // *
  // * @param textLine
  // * The text line to process.
  // * @return The base line of the given text line.
  // */
  // protected Line computeBaseline(PdfTextLine textLine) {
  // // Compute the most common minY value among all baseline characters.
  // FloatCounter minYValuesOfBaselineCharacters = new FloatCounter();
  // for (PdfCharacter character : textLine.getCharacters()) {
  // if (PdfCharacterUtils.isBaselineCharacter(character)) {
  // minYValuesOfBaselineCharacters.add(character.getRectangle().getMinY());
  // }
  // }
  // float startX = textLine.getRectangle().getMinX();
  // float endX = textLine.getRectangle().getMaxX();
  // float y = minYValuesOfBaselineCharacters.getMostCommonFloat();
  //
  // return this.lineFactory.create(startX, y, endX, y);
  // }
}
