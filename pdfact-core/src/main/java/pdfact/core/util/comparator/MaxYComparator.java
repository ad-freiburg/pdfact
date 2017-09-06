package pdfact.core.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import pdfact.core.model.HasPosition;
import pdfact.core.model.Page;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;

/**
 * A comparator that compares rectangles by their maxY values.
 * 
 * @author Claudius Korzen
 */
public class MaxYComparator implements Comparator<HasPosition>, Serializable {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -8263437216397294221L;

  @Override
  public int compare(HasPosition element1, HasPosition element2) {
    if (element1 == null && element2 == null) {
      return 0;
    }
    if (element1 == null) {
      return 1;
    }
    if (element2 == null) {
      return -1;
    }

    Position pos1 = element1.getPosition();
    Position pos2 = element2.getPosition();
    if (pos1 == null && pos2 == null) {
      return 0;
    }
    if (pos1 == null) {
      return 1;
    }
    if (pos2 == null) {
      return -1;
    }
    
    Page page1 = pos1.getPage();
    Page page2 = pos2.getPage();
    if (page1 == null && page2 == null) {
      return 0;
    }
    if (page1 == null) {
      return 1;
    }
    if (page2 == null) {
      return -1;
    }
    
    int pageNum1 = page1.getPageNumber();
    int pageNum2 = page2.getPageNumber();
    if (pageNum1 != pageNum2) {
      return pageNum1 - pageNum2;
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
