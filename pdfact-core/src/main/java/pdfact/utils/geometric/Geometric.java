package pdfact.utils.geometric;

/**
 * The base class of a (two-dimensional) geometric object (e.g., a point, a
 * line, a rectangle, etc).
 * 
 * @author Claudius Korzen
 */
public abstract class Geometric implements HasRectangle {
  /**
   * Returns true, when the bounding box of this geometric object completely
   * contains the given geometric object.
   * 
   * @param g
   *        The other geometric object.
   * 
   * @return True, if the bounding box of this geometric object completely
   *         contains the other geometric object.
   */
  public boolean contains(Geometric g) {
    if (g == null) {
      return false;
    }

    Rectangle rect = getRectangle();
    Rectangle other = g.getRectangle();

    if (rect == null || other == null) {
      return false;
    }
    if (other.getMinX() < rect.getMinX()) {
      return false;
    }
    if (other.getMaxX() > rect.getMaxX()) {
      return false;
    }
    if (other.getMinY() < rect.getMinY()) {
      return false;
    }
    if (other.getMaxY() > rect.getMaxY()) {
      return false;
    }
    return true;
  }

  /**
   * Returns true, if this geometric object overlaps the other geometric
   * horizontally and vertically (if there is an area that share both objects).
   * 
   * @param g
   *        The other geometric object.
   * 
   * @return True, if this geometric object overlaps the other geometric.
   */
  public boolean overlaps(Geometric g) {
    if (g == null) {
      return false;
    }
    Rectangle rect = g.getRectangle();
    if (rect == null) {
      return false;
    }
    return overlaps(rect.getMinX(), rect.getMinY(), rect.getMaxX(),
        rect.getMaxY());
  }

  /**
   * Returns true, if this geometric object overlaps the rectangle defined by
   * the given coordinates.
   * 
   * @param minX
   *        The x coordinate of lower left corner of rectangle.
   * @param minY
   *        The y coordinate of lower left corner of rectangle.
   * @param maxX
   *        The x coordinate of upper right corner of rectangle.
   * @param maxY
   *        The x coordinate of upper right corner of rectangle.
   * 
   * @return True, if this geometric object overlaps the given rectangle.
   */
  public boolean overlaps(float minX, float minY, float maxX, float maxY) {
    return overlapsHorizontally(minX, maxX) && overlapsVertically(minY, maxY);
  }

  /**
   * Returns true, if there is an horizontal overlap between this geometric
   * object and the other given geometric object.
   * 
   * @param g
   *        The other geometric object.
   * 
   * @return True if this geometric object overlaps the other geometric object
   *         horizontally.
   */
  public boolean overlapsHorizontally(Geometric g) {
    if (g == null) {
      return false;
    }
    Rectangle rect = g.getRectangle();
    if (rect == null) {
      return false;
    }
    return overlapsHorizontally(rect.getMinX(), rect.getMaxX());
  }

  /**
   * Returns true, if this geometric object overlaps the horizontal range
   * defined by the given coordinates.
   * 
   * @param minX
   *        The left border of the range.
   * @param maxX
   *        The right border of the range.
   * 
   * @return True if this geometric object overlaps the given horizontal range.
   */
  public boolean overlapsHorizontally(float minX, float maxX) {
    Rectangle rect = getRectangle();
    if (rect == null) {
      return false;
    }
    return rect.getMaxX() >= minX && rect.getMinX() <= maxX;
  }

  /**
   * Returns true, if there is an vertical overlap between this geometric
   * object and the other given geometric object.
   * 
   * @param g
   *        The other geometric object.
   * 
   * @return True if this geometric object overlaps the other geometric object
   *         vertically.
   */
  public boolean overlapsVertically(Geometric g) {
    if (g == null) {
      return false;
    }
    Rectangle rect = g.getRectangle();
    if (rect == null) {
      return false;
    }
    return overlapsVertically(rect.getMinY(), rect.getMaxY());
  }

  /**
   * Returns true, if this geometric object overlaps the vertical range defined
   * by the given coordinates.
   * 
   * @param minY
   *        The lower border of the range.
   * @param maxY
   *        The upper border of the range.
   * 
   * @return True if this geometric object overlaps the given vertical range.
   */
  public boolean overlapsVertically(float minY, float maxY) {
    return getRectangle().getMinY() <= maxY && getRectangle().getMaxY() >= minY;
  }

  /**
   * Computes the size of the overlap area between this geometric object and
   * the other geometric object.
   * 
   * @param g
   *        The other geometric object.
   * @return the size of the overlap area
   */
  public float computeOverlapArea(Geometric g) {
    return computeHorizontalOverlapLength(g) * computeVerticalOverlapLength(g);
  }

  /**
   * Computes the length of the vertical overlap between this geometric object
   * and the other geometric object.
   * 
   * @param g
   *        The other geometric object.
   * @return The length of the vertical overlap
   */
  public float computeVerticalOverlapLength(Geometric g) {
    if (g == null) {
      return 0;
    }
    Rectangle rect = getRectangle();
    Rectangle other = g.getRectangle();
    if (rect != null && other != null) {
      float minMaxY = Math.min(rect.getMaxY(), other.getMaxY());
      float maxMinY = Math.max(rect.getMinY(), other.getMinY());
      return Math.max(0, minMaxY - maxMinY);
    }
    return 0;
  }

  /**
   * Computes the length of the horizontal overlap between this geometric
   * object and the other geometric object.
   * 
   * @param g
   *        The other geometric object.
   * @return The length of the vertical overlap
   */
  public float computeHorizontalOverlapLength(Geometric g) {
    if (g == null) {
      return 0;
    }
    Rectangle rect = getRectangle();
    Rectangle other = g.getRectangle();
    if (rect != null && other != null) {
      float minMaxX = Math.min(rect.getMaxX(), other.getMaxX());
      float maxMinX = Math.max(rect.getMinX(), other.getMinX());
      return Math.max(0, minMaxX - maxMinX);
    }
    return 0;
  }

  @Override
  public String toString() {
    return "[" + getRectangle().getMinX() + "," + getRectangle().getMinY() + ","
        + getRectangle().getMaxX() + "," + getRectangle().getMaxY() + "]";
  }
}
