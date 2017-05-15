package icecite.tokenizer.xycut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterSet;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfPage;
import icecite.utils.geometric.Rectangle;

/**
 * A class that cuts given characters horizontally and vertically into any type
 * of blocks, which has to be defined in the implementing class.
 * 
 * @param <T>
 *        The type in which the resulting character blocks should be packed.
 * 
 * @author Claudius Korzen
 */
public abstract class XYCut<T extends PdfElement> {
  /**
   * The factory to create instance of {@link PdfCharacterSet}.
   */
  protected PdfCharacterSetFactory characterSetFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new instance of XYCut.
   * 
   * @param characterSetFactory
   *        The factory to create instances of {@link PdfCharacterSet}.
   */
  @Inject
  public XYCut(PdfCharacterSetFactory characterSetFactory) {
    this.characterSetFactory = characterSetFactory;
  }

  // ==========================================================================

  /**
   * Cuts the given characters into objects of type T.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The characters to cut.
   * 
   * @return The list of resulting objects.
   */
  public List<T> cut(PdfDocument pdf, PdfPage page, PdfCharacterSet chars) {
    List<T> objects = new ArrayList<>();
    cut(pdf, page, chars, objects);
    return objects;
  }

  // ==========================================================================

  /**
   * Cuts the given characters into objects of type T and adds them into the
   * given list.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param characters
   *        The characters to be cut.
   * @param objects
   *        The list of objects to fill.
   */
  protected void cut(PdfDocument pdf, PdfPage page, PdfCharacterSet characters,
      List<T> objects) {
    // Do a vertical cut (x-cut).
    List<PdfCharacterSet> xBlocks = xCut(pdf, page, characters);

    for (PdfCharacterSet xBlock : xBlocks) {
      // Do a horizontal cut (y-cut).
      List<PdfCharacterSet> yBlocks = yCut(pdf, page, xBlock);
      if (xBlocks.size() == 1 && yBlocks.size() == 1) {
        // The characters could *not* be cut. Pack them and them to the result.
        PdfCharacterSet block = yBlocks.get(0);
        if (block != null && !block.isEmpty()) {
          objects.add(pack(page, block));
        }
      } else {
        // The characters could be cut. Cut the subblocks recursively.
        for (PdfCharacterSet yBlock : yBlocks) {
          cut(pdf, page, yBlock, objects);
        }
      }
    }
  }

  // ==========================================================================

  /**
   * Takes a set of characters and sweeps them in x direction in order to find
   * a position to cut the characters vertically into a left half and a right
   * half.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The characters to cut.
   * @return A list of set of characters. In case of the characters could be
   *         cut, this list has two inner sets, containing the characters of
   *         the two halves. In case of the characters could *not* be cut, the
   *         list consists of a single set, which is a copy of the original set
   *         of characters.
   */
  protected List<PdfCharacterSet> xCut(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    /*-
     * To illustrate the approach of this sweep algorithm, we introduce an 
     * example of bounding boxes in order to refer to it in further comments.
     * The bounding boxes are given as intervals (minX, maxX), because we
     * only consider the x direction in this method:
     * 
     * Character A (minX:  9, maxX: 13):         |---|
     * Character B (minX:  1, maxX:  6): |----|   
     * Character C (minX: 10, maxX: 12):          |-|
     * Character D (minX:  2, maxX:  5):  |--|
     * 
     * Width of lane to sweep: 4  : |+++|->           
     * 
     * On processing, we actually sweep a (virtual) lane of width defined by
     * getVerticalLaneWidth() through the characters.
     */

    // Define the sweep direction: from left to right (minX -> maxX).
    XYCutSweepDirection dir = XYCutSweepDirection.LEFT_TO_RIGHT;

    // Define a min-based queue of events, where an event is defined by the
    // start value (the minX value) and the end value (the maxX value) of an
    // bounding box.
    Comparator<XYCutSweepEvent> cmp = new XYCutSweepEventComparator();
    Queue<XYCutSweepEvent> events = new PriorityQueue<>(cmp);

    // Fill the queue with the related events. For the example above, the queue
    // looks like: [(5, A), (9, A), (1, B), (6, B), ...]. They are removed from
    // the queue in ascending order: (1, B), (2, D), (5, D), ...
    for (PdfCharacter character : chars) {
      Rectangle boundingBox = character.getBoundingBox();
      events.add(new XYCutSweepEvent(character, boundingBox.getMinX(), dir));
      events.add(new XYCutSweepEvent(character, boundingBox.getMaxX(), dir));
    }

    // The set of characters that overlap the current lane horizontally.
    PdfCharacterSet overlapping = this.characterSetFactory.create();
    // The set of characters of the left half.
    PdfCharacterSet leftHalf = this.characterSetFactory.create();
    // The set of characters of the right half.
    PdfCharacterSet rightHalf = this.characterSetFactory.create(chars);

    while (!events.isEmpty()) {
      // Process the next event, e.g. "(1, B)" in the example above.
      XYCutSweepEvent evt = events.poll();

      if (evt.isStartEvent()) {
        // Add the char to the overlapping characters.
        overlapping.add(evt.getCharacter());
        rightHalf.remove(evt.getCharacter());
      } else {
        // Remove the char from overlapping chars, e.g. if event is "(6, B)".
        overlapping.remove(evt.getCharacter());
        leftHalf.add(evt.getCharacter());
      }

      // Compute the extent of the lane, starting from the current value.
      float border = evt.getValue() + getVerticalLaneWidth(pdf, page, chars);

      // Identify all further chars that overlap the current lane.
      while (!events.isEmpty()) {
        XYCutSweepEvent next = events.peek();
        // Only consider start events.
        if (next.isEndEvent()) {
          break;
        }
        // Only consider those chars that indeed falls into the lane area.
        if (next.getValue() >= border) {
          break;
        }
        overlapping.add(next.getCharacter());
        rightHalf.remove(next.getCharacter());
        events.poll();
      }

      // Break if a valid lane was found.
      if (isValidVerticalLane(pdf, page, leftHalf, overlapping, rightHalf)) {
        break;
      }
    }

    if (rightHalf.isEmpty()) {
      // If the chars could not be cut into two halves, rightHalf is empty.
      // Return only the left half.
      return Arrays.asList(leftHalf);
    }

    // The chars could be cut.
    return Arrays.asList(leftHalf, rightHalf);
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
  protected List<PdfCharacterSet> yCut(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    // Define the sweep direction: from top to bottom (maxY -> minY).
    XYCutSweepDirection dir = XYCutSweepDirection.TOP_TO_BOTTOM;

    // Define a *max*-based queue of events, where an event is defined by the
    // start value (the maxY value) and the end value (the minY value) of an
    // bounding box.
    Comparator<XYCutSweepEvent> cmp = new XYCutSweepEventComparator();
    Queue<XYCutSweepEvent> events = new PriorityQueue<>(
        Collections.reverseOrder(cmp));

    // Fill the queue with the related events.
    for (PdfCharacter character : chars) {
      Rectangle boundingBox = character.getBoundingBox();
      events.add(new XYCutSweepEvent(character, boundingBox.getMinY(), dir));
      events.add(new XYCutSweepEvent(character, boundingBox.getMaxY(), dir));
    }

    // The set of characters that overlap the current lane horizontally.
    PdfCharacterSet overlapping = this.characterSetFactory.create();
    // The set of characters of the upper half.
    PdfCharacterSet upperHalf = this.characterSetFactory.create();
    // The set of characters of the lower half.
    PdfCharacterSet lowerHalf = this.characterSetFactory.create(chars);

    while (!events.isEmpty()) {
      // Process the next event.
      XYCutSweepEvent evt = events.poll();

      if (evt.isStartEvent()) {
        // Add the char to the overlapping characters.
        overlapping.add(evt.getCharacter());
        lowerHalf.remove(evt.getCharacter());
      } else {
        // Remove the char from the overlapping characters.
        overlapping.remove(evt.getCharacter());
        upperHalf.add(evt.getCharacter());
      }

      // Compute the extent of the lane, starting from the current value.
      float border = evt.getValue() - getHorizontalLaneHeight(pdf, page, chars);

      // Identify all further chars that overlap the current lane.
      while (!events.isEmpty()) {
        XYCutSweepEvent next = events.peek();
        // Only consider start events.
        if (next.isEndEvent()) {
          break;
        }
        // Only consider those chars that indeed fall into the lane area.
        if (next.getValue() <= border) {
          break;
        }
        overlapping.add(next.getCharacter());
        lowerHalf.remove(next.getCharacter());
        events.poll();
      }

      // Break if a valid lane was found.
      if (isValidHorizontalLane(pdf, page, upperHalf, overlapping, lowerHalf)) {
        break;
      }
    }

    if (lowerHalf.isEmpty()) {
      // If the chars could not be cut into two halves, lowerHalf is empty.
      // Return only the upper half.
      return Arrays.asList(upperHalf);
    }

    // The chars could be cut.
    return Arrays.asList(upperHalf, lowerHalf);
  }

  // ==========================================================================
  // Abstract methods.

  /**
   * Returns the width of the vertical lane to sweep through the given
   * characters on cutting the characters vertically.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The characters to cut.
   * 
   * @return The width of the vertical lane to sweep.
   */
  public abstract float getVerticalLaneWidth(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars);

  /**
   * Returns the height of the horizontal lane to sweep through the given
   * characters on cutting the characters horizontally.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param chars
   *        The chars to cut.
   * 
   * @return The height of the horizontal lane to sweep.
   */
  public abstract float getHorizontalLaneHeight(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars);

  /**
   * Returns true, if the vertical lane that overlaps the given characters is
   * valid; false otherwise.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param left
   *        The characters to the left of the lane.
   * @param overlap
   *        The characters that overlap the lane.
   * @param right
   *        The characters to the right of the lane.
   * @return True, if the lane that overlaps the given characters is valid;
   *         false otherwise.
   */
  public abstract boolean isValidVerticalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet left, PdfCharacterSet overlap, PdfCharacterSet right);

  /**
   * Returns true, if the horizontal lane that overlaps the given characters is
   * valid; false otherwise.
   * 
   * @param pdf
   *        The PDF document to which the characters belong to.
   * @param page
   *        The page in which the characters are located.
   * @param upper
   *        The characters above the lane.
   * @param overlap
   *        The characters that overlap the lane.
   * @param lower
   *        The characters below the lane.
   * @return True, if the lane that overlaps the given characters is valid;
   *         false otherwise.
   */
  public abstract boolean isValidHorizontalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet upper, PdfCharacterSet overlap, PdfCharacterSet lower);

  /**
   * Packs the given characters into the target type.
   * 
   * @param page
   *        The page in which the given characters are included.
   * @param characters
   *        The characters to pack.
   * 
   * @return An object of given target type.
   */
  public abstract T pack(PdfPage page, PdfCharacterSet characters);
}
