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
  public int compare(HasRectangle box1, HasRectangle box2) {
    if (box1 == null && box2 == null) {
      return 0;
    }
    if (box1 == null) {
      return 1;
    }
    if (box2 == null) {
      return -1;
    }

    Rectangle rect1 = box1.getRectangle();
    Rectangle rect2 = box2.getRectangle();
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
