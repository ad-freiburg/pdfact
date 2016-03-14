package model;

import java.util.Map;

/**
 * Interface for position statistics.
 *
 * @author Claudius Korzen
 */
public interface PositionStatistics {
  /**
   * Returns the most common min x value.
   */
  public float getMostCommonMinX();
  
  /**
   * Returns the most common min y value.
   */
  public float getMostCommonMinY();
  
  /**
   * Returns the most common max x value.
   */
  public float getMostCommonMaxX();
  
  /**
   * Returns the most common max y value.
   */
  public float getMostCommonMaxY();
}
