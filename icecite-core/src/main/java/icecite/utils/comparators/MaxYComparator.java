package icecite.utils.comparators;

import java.util.Comparator;

import icecite.models.HasBoundingBox;
import icecite.utils.geometric.Rectangle;

/**
 * A comparator that compares rectangle by their maxY values.
 * 
 * @author Claudius Korzen
 */
public class MaxYComparator implements Comparator<HasBoundingBox> {
  @Override
  public int compare(HasBoundingBox box1, HasBoundingBox box2) {
    if (box1 == null && box2 == null) {
      return 0;
    }
    if (box1 == null) {
      return 1;
    }
    if (box2 == null) {
      return -1;
    }

    Rectangle rect1 = box1.getBoundingBox();
    Rectangle rect2 = box2.getBoundingBox();
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
