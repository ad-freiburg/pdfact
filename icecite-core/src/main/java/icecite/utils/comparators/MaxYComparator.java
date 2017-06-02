package icecite.utils.comparators;

import java.io.Serializable;
import java.util.Comparator;

import icecite.utils.geometric.HasRectangle;
import icecite.utils.geometric.Rectangle;

/**
 * A comparator that compares rectangles by their maxY values.
 * 
 * @author Claudius Korzen
 */
public class MaxYComparator implements Comparator<HasRectangle>, Serializable {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -8263437216397294221L;

  @Override
  public int compare(HasRectangle pos1, HasRectangle pos2) {
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

    return Float.compare(rect1.getMaxY(), rect2.getMaxY());
  }
}
