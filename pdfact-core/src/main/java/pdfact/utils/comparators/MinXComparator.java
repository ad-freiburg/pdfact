package pdfact.utils.comparators;

import java.io.Serializable;
import java.util.Comparator;

import pdfact.models.HasPosition;
import pdfact.utils.geometric.Rectangle;

/**
 * A comparator that compares rectangles by their minX values.
 * 
 * @author Claudius Korzen
 */
public class MinXComparator implements Comparator<HasPosition>, Serializable {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 8095843011284188012L;

  @Override
  public int compare(HasPosition pos1, HasPosition pos2) {
    if (pos1 == null && pos2 == null) {
      return 0;
    }
    if (pos1 == null) {
      return 1;
    }
    if (pos2 == null) {
      return -1;
    }

    Rectangle rect1 = pos1.getRectangle();
    Rectangle rect2 = pos2.getRectangle();
    if (rect1 == null && rect2 == null) {
      return 0;
    }
    if (rect1 == null) {
      return 1;
    }
    if (rect2 == null) {
      return -1;
    }

    return Float.compare(rect1.getMinX(), rect2.getMinX());
  }
}
