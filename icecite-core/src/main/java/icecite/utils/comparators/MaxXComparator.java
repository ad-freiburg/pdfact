package icecite.utils.comparators;

import java.util.Comparator;

import icecite.utils.geometric.HasRectangle;
import icecite.utils.geometric.Rectangle;

/**
 * A comparator that compares rectangles by their maxX values.
 * 
 * @author Claudius Korzen
 */
public class MaxXComparator implements Comparator<HasRectangle> {
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

    return Float.compare(rect1.getMaxX(), rect2.getMaxX());
  }
}