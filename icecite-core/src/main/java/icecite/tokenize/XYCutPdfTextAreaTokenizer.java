package icecite.tokenize;

import java.util.List;
import java.util.Set;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.tokenize.xycut.XYCut;
import icecite.utils.geometric.Rectangle;

/**
 * An implementation of {@link PdfTextAreaTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextAreaTokenizer extends XYCut<PdfCharacterList>
    implements PdfTextAreaTokenizer {
  @Override
  public List<PdfCharacterList> tokenize(PdfDocument pdf, PdfPage page) {
    return cut(pdf, page, page.getCharacters());
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList left = halves.get(0);
    PdfCharacterList right = halves.get(1);

    // Compute the (fictive) lane between the left and right half.
    float laneMinX = left.getRectangle().getMaxX();
    float laneMaxX = right.getRectangle().getMinX();
    float laneWidth = laneMaxX - laneMinX;

    float docCharWidth = pdf.getCharacters().getMostCommonWidth();
    float pageCharWidth = page.getCharacters().getMostCommonWidth();

    // Don't allow the lane, if it is too narrow.
    if (laneWidth < Math.max(docCharWidth, pageCharWidth)) {
      return -1;
    }

    // Don't allow the lane, if it separates consecutive chars.
    if (separatesConsecutiveCharacters(left, right)) {
      return -1;
    }

    return laneWidth;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList upper = halves.get(0);
    PdfCharacterList lower = halves.get(1);

    // Compute the (fictive) lane between the lower and upper half.
    float laneMinY = lower.getRectangle().getMaxY();
    float laneMaxY = upper.getRectangle().getMinY();
    float laneHeight = laneMaxY - laneMinY;

    // Don't allow lanes with negative heights.
    if (laneHeight < 0) {
      return -1;
    }

    float docCharHeight = pdf.getCharacters().getMostCommonHeight();
    float pageCharHeight = page.getCharacters().getMostCommonHeight();

    // Don't allow the lane, if it is too shallow.
    if (laneHeight < Math.min(docCharHeight, pageCharHeight)) {
      return -1;
    }

    return laneHeight;
  }

  // ==========================================================================

  @Override
  public PdfCharacterList pack(PdfPage page, PdfCharacterList characters) {
    return characters;
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
   *        The first character set.
   * @param right
   *        The second character set.
   * @return True if there is such a character pair, false otherwise.
   */
  protected boolean separatesConsecutiveCharacters(PdfCharacterList left,
      PdfCharacterList right) {
    Set<PdfCharacter> leftChars = left.getElementsWithLargestMaxX();
    Set<PdfCharacter> rightChars = right.getElementsWithSmallestMinX();

    for (PdfCharacter leftChar : leftChars) {
      int leftCharNum = leftChar.getPositionInExtractionOrder();
      Rectangle leftCharBox = leftChar.getRectangle();
      for (PdfCharacter rightChar : rightChars) {
        int rightCharNum = rightChar.getPositionInExtractionOrder();
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
