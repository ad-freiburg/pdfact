package pdfact.tokenize.xycut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterList;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.utils.comparators.MaxYComparator;
import pdfact.utils.comparators.MinXComparator;

/**
 * A class that cuts a list of characters horizontally and vertically into
 * (smaller) blocks.
 * 
 * @author Claudius Korzen
 */
public abstract class XYCut {
  /**
   * Cuts the given characters into blocks of type T.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param characters
   *        The characters to cut.
   * 
   * @return The list of resulting blocks.
   */
  public List<PdfCharacterList> cut(PdfDocument pdf, PdfPage page,
      PdfCharacterList characters) {
    List<PdfCharacterList> target = new ArrayList<>();
    cut(pdf, page, characters, target);
    return target;
  }

  /**
   * Cuts the given characters into blocks of type T and adds them to the given
   * result list.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param origin
   *        The characters to be cut.
   * @param target
   *        The list of blocks to fill.
   */
  protected void cut(PdfDocument pdf, PdfPage page, PdfCharacterList origin,
      List<PdfCharacterList> target) {
    // Cut the characters vertically (x-cut).
    List<PdfCharacterList> xBlocks = xCut(pdf, page, origin);

    for (PdfCharacterList xBlock : xBlocks) {
      // Cut the characters horizontally (y-cut).
      List<PdfCharacterList> yBlocks = yCut(pdf, page, xBlock);
      if (xBlocks.size() == 1 && yBlocks.size() == 1) {
        // Both cuts results in a single blocks. So, the characters could *not*
        // be cut. Pack them and add them to the result list.
        PdfCharacterList block = yBlocks.get(0);
        if (block != null && !block.isEmpty()) {
          target.add(block);
        }
      } else {
        // The characters could be cut. Cut the resulted blocks recursively.
        for (PdfCharacterList yBlock : yBlocks) {
          cut(pdf, page, yBlock, target);
        }
      }
    }
  }

  /**
   * Takes a list of characters and iterates them by sweeping a lane in x
   * direction in order to find a position to cut the characters vertically
   * into a left half and a right half.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The characters to cut.
   * @return A list of list of characters. In case of the characters could be
   *         cut, this list has two inner lists representing the characters of
   *         the two halves. In case of the characters could *not* be cut, the
   *         list consists only of a single list, which is the original list of
   *         characters.
   */
  protected List<PdfCharacterList> xCut(PdfDocument pdf, PdfPage page,
      PdfCharacterList chars) {
    // Sort the characters by minX in order to sweep them in x direction.
    Collections.sort(chars, new MinXComparator());

    // The score of the best cut found so far.
    float bestCutScore = 0;
    // The index of the best cut found so far.
    int bestCutIndex = -1;
    // The current position in the list of characters.
    float currentPos = chars.get(0).getRectangle().getMaxX();

    for (int index = 1; index < chars.size(); index++) {
      PdfCharacter character = chars.get(index);

      if (character.getRectangle().getMinX() > currentPos) {
        List<PdfCharacterList> halves = chars.cut(index);
        // Find the position of the "best" cut.
        while (index < chars.size()) {
          // The score of the current cut.
          float cutScore = assessVerticalCut(pdf, page, halves);

          if (cutScore < 0) {
            break;
          } else
            if (cutScore > bestCutScore) {
              bestCutScore = cutScore;
              bestCutIndex = index;
            }
          halves = chars.cut(++index);
        }
      }
      currentPos = character.getRectangle().getMaxX();
    }
    
    if (bestCutIndex > -1) {
      // A cut was found. Return the resulting halves.
      return chars.cut(bestCutIndex);
    } else {
      return Arrays.asList(chars);
    }
  }

  /**
   * Takes a set of characters and sweeps the characters in y direction in
   * order to find a position to cut the characters vertically into a upper
   * half and a lower half. For more details about the approach of the sweep
   * algorithm, see the examples given for xCut().
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The characters to cut.
   * @return A list of set of characters. In case of the characters could be
   *         cut, this list consists of two inner sets, containing the
   *         characters of the two halves. In case of the characters could not
   *         be cut, the list consists only of a single set, representing a
   *         copy of the original set of characters.
   */
  protected List<PdfCharacterList> yCut(PdfDocument pdf, PdfPage page,
      PdfCharacterList chars) {
    // Sort the characters by minX in order to sweep them in x direction.
    Collections.sort(chars, Collections.reverseOrder(new MaxYComparator()));

    // The score of the best cut found so far.
    float bestCutScore = 0;
    // The index of the best cut found so far.
    int bestCutIndex = -1;
    // The current position in the list of characters.
    float currentPos = chars.get(0).getRectangle().getMinY();

    for (int index = 1; index < chars.size(); index++) {
      PdfCharacter character = chars.get(index);

      if (character.getRectangle().getMaxY() < currentPos) {
        List<PdfCharacterList> halves = chars.cut(index);
        // Find the position of the "best" cut.
        while (index < chars.size()) {
          float cutScore = assessHorizontalCut(pdf, page, halves);

          if (cutScore < 0) {
            break;
          } else
            if (cutScore > bestCutScore) {
              bestCutScore = cutScore;
              bestCutIndex = index;
            }
          halves = chars.cut(++index);
        }
      }
      currentPos = character.getRectangle().getMinY();
    }
    
    if (bestCutIndex > -1) {
      // A cut was found. Return the resulting halves.
      return chars.cut(bestCutIndex);
    } else {
      return Arrays.asList(chars);
    }
  }

  // ==========================================================================
  // Abstract methods.

  /**
   * Assesses the given vertical cut. Returns a positive score, if the cut is
   * valid and a negative score if the cut is invalid. The better the cut, the
   * higher the returned score.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param halves
   *        The characters of the two halves.
   * @return A score that assesses the given cut.
   */
  public abstract float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves);

  /**
   * Assesses the given horizontal cut. Returns a positive score, if the cut is
   * valid and a negative score if the cut is invalid. The better the cut, the
   * higher the returned score.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param halves
   *        The characters of the two halves.
   * @return A score that assesses the given cut.
   */
  public abstract float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves);

  // /**
  // * Packs the given characters into the target type.
  // *
  // * @param page
  // * The page in which the characters are located.
  // * @param characters
  // * The characters to pack.
  // *
  // * @return An object of given target type.
  // */
  // public abstract T pack(PdfPage page, PdfCharacterList characters);
}
