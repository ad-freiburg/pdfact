package icecite.utils.comparators;

import java.io.Serializable;
import java.util.Comparator;

import icecite.models.HasPosition;
import icecite.utils.geometric.Rectangle;

/**
 * A comparator that compares rectangles by their minY values.
 * 
 * @author Claudius Korzen
 */
public class MinYComparator implements Comparator<HasPosition>, Serializable {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -6048171342968387350L;

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

    return Float.compare(rect1.getMinY(), rect2.getMinY());
  }
}