package model;

import java.util.Comparator;

import de.freiburg.iif.model.HasRectangle;

/**
 * Class, that contains all Comparators, needed by StructuredPdfParser.
 * 
 * @author Claudius Korzen
 * 
 */
public class Comparators {
  /**
   * A comparator to sort objects, containing a rectangle, by getMinX().
   * 
   * @author Claudius Korzen *
   */
  public static class MinXComparator implements Comparator<HasRectangle> {
    @Override
    public int compare(HasRectangle o1, HasRectangle o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      if (o1.getRectangle().getMinX() < o2.getRectangle().getMinX()) {
        return -1;
      }
      if (o1.getRectangle().getMinX() > o2.getRectangle().getMinX()) {
        return 1;
      }
      return 0;
    }
  }

  /**
   * A comparator to sort objects, containing a rectangle, by getMaxX().
   * 
   * @author Claudius Korzen *
   */
  public static class MaxXComparator implements Comparator<HasRectangle> {
    @Override
    public int compare(HasRectangle o1, HasRectangle o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      if (o1.getRectangle().getMaxX() < o2.getRectangle().getMaxX()) {
        return -1;
      }
      if (o1.getRectangle().getMaxX() > o2.getRectangle().getMaxX()) {
        return 1;
      }
      return 0;
    }
  }
  
  /**
   * A comparator to sort objects, containing a rectangle, by getMinY().
   * 
   * @author Claudius Korzen *
   */
  public static class MinYComparator implements Comparator<HasRectangle> {
    @Override
    public int compare(HasRectangle o1, HasRectangle o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      if (o1.getRectangle().getMinY() < o2.getRectangle().getMinY()) {
        return -1;
      }
      if (o1.getRectangle().getMinY() > o2.getRectangle().getMinY()) {
        return 1;
      }
      return 0;
    }
  }
  
  /**
   * A comparator to sort objects, containing a rectangle, by getMinY().
   * 
   * @author Claudius Korzen *
   */
  public static class MaxYComparator implements Comparator<HasRectangle> {
    @Override
    public int compare(HasRectangle o1, HasRectangle o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      if (o1.getRectangle().getMaxY() < o2.getRectangle().getMaxY()) {
        return -1;
      }
      if (o1.getRectangle().getMaxY() > o2.getRectangle().getMaxY()) {
        return 1;
      }
      return 0;
    }
  }
  
  /**
   * A comparator to sort objects by their id.
   * 
   * @author Claudius Korzen
   */
  public static class IdComparator implements Comparator<HasId> {
    @Override
    public int compare(HasId o1, HasId o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      return o1.getId().compareTo(o2.getId());
    }
  }
}
