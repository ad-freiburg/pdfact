package pdfact.tokenize.areas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterList;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfCharacterStatistician;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.tokenize.xycut.XYCut;
import pdfact.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfTextAreaSegmenter}, based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextAreaSegmenter extends XYCut
    implements PdfTextAreaSegmenter {
  /**
   * The statistician to compute statistics about characters.
   */
  protected PdfCharacterStatistician charStatistician;

  /**
   * The default constructor.
   * 
   * @param charStatistician
   *        The statistician to compute statistics about characters.
   */
  @Inject
  public XYCutPdfTextAreaSegmenter(PdfCharacterStatistician charStatistician) {
    this.charStatistician = charStatistician;
  }

  @Override
  public List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList characters) {
    return cut(pdf, page, characters);
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    // Compute the statistics for the characters in the left half.
    PdfCharacterList left = halves.get(0);
    PdfCharacterStatistic leftStats = this.charStatistician.compute(left);

    // Compute the statistics for the characters in the right half.
    PdfCharacterList right = halves.get(1);
    PdfCharacterStatistic rightStats = this.charStatistician.compute(right);

    // Compute the (fictive) lane between the left and right half.
    float laneMinX = leftStats.getLargestMaxX();
    float laneMaxX = rightStats.getSmallestMinX();
    float laneWidth = laneMaxX - laneMinX;

    PdfCharacterStatistic pdfCharStats = pdf.getCharacterStatistic();
    PdfCharacterStatistic pageCharStats = page.getCharacterStatistic();
    float pdfCharWidth = pdfCharStats.getMostCommonWidth();
    float pageCharWidth = pageCharStats.getMostCommonWidth();

    // Don't allow the lane, if it is too narrow.
    if (laneWidth < Math.max(pdfCharWidth, pageCharWidth)) {
      return -1;
    }

    // Don't allow the lane, if it separates consecutive chars.
    if (separatesConsecutiveCharacters(left, leftStats, right, rightStats)) {
      return -1;
    }

    return laneWidth;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    // Compute the statistics for the characters in the upper half.
    PdfCharacterList upper = halves.get(0);
    PdfCharacterStatistic upperStats = this.charStatistician.compute(upper);

    // Compute the statistics for the characters in the lower half.
    PdfCharacterList lower = halves.get(1);
    PdfCharacterStatistic lowerStats = this.charStatistician.compute(lower);

    // Compute the (fictive) lane between the lower and upper half.
    float laneMinY = lowerStats.getLargestMaxY();
    float laneMaxY = upperStats.getSmallestMinY();
    float laneHeight = laneMaxY - laneMinY;

    // Don't allow lanes with negative heights.
    if (laneHeight < 0) {
      return -1;
    }

    float pdfCharHeight = pdf.getCharacterStatistic().getMostCommonHeight();
    float pageCharHeight = page.getCharacterStatistic().getMostCommonHeight();

    // Don't allow the lane, if it is too shallow.
    if (laneHeight < Math.min(pdfCharHeight, pageCharHeight)) {
      return -1;
    }

    return laneHeight;
  }

  // ==========================================================================
  // Utility methods.

  /**
   * Checks if there is a character in the first given list of characters with
   * an extraction order number i and a character in the second given list of
   * characters with extraction order number i + 1, where both characters
   * overlap vertically.
   * 
   * @param left
   *        The characters in the left half.
   * @param leftStats
   *        The statistics about the characters in the left half.
   * @param right
   *        The characters in the right half.
   * @param rightStats
   *        The statistics about the characters in the right half.
   * @return True if there is such a character pair, false otherwise.
   */
  protected boolean separatesConsecutiveCharacters(PdfCharacterList left,
      PdfCharacterStatistic leftStats, PdfCharacterList right,
      PdfCharacterStatistic rightStats) {
    float largestMaxX = leftStats.getLargestMaxX();
    Set<PdfCharacter> leftChars = new HashSet<>();
    for (PdfCharacter character : left) {
      if (character.getRectangle().getMaxX() == largestMaxX) {
        leftChars.add(character);
      }
    }

    float smallestMinX = rightStats.getSmallestMinX();
    Set<PdfCharacter> rightChars = new HashSet<>();
    for (PdfCharacter character : right) {
      if (character.getRectangle().getMinX() == smallestMinX) {
        rightChars.add(character);
      }
    }

    for (PdfCharacter leftChar : leftChars) {
      int leftCharNum = leftChar.getSequenceNumber();
      Rectangle leftCharBox = leftChar.getRectangle();
      for (PdfCharacter rightChar : rightChars) {
        int rightCharNum = rightChar.getSequenceNumber();
        Rectangle rightCharNox = rightChar.getRectangle();

        // Check if the characters are consecutive.
        if (rightCharNum != leftCharNum + 1) {
          continue;
        }
        // Check if the characters overlap.
        if (!leftCharBox.overlapsVertically(rightCharNox)) {
          continue;
        }
        return true;
      }
    }
    return false;
  }
}
