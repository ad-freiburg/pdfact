package model;

import de.freiburg.iif.counter.FloatCounter;

/**
 * Interface for dimension statistics.
 *
 * @author Claudius Korzen
 */
public interface DimensionStatistics {
  /**
   * Returns the average height.
   */
  float getAverageHeight();
  
  /**
   * Returns the average width.
   */
  float getAverageWidth();
    
  /**
   * Returns the most common height.
   */
  float getMostCommonHeight();
  
  /**
   * Returns the most common width.
   */
  float getMostCommonWidth();
  
  /**
   * Returns the height frequencies.
   */
  FloatCounter getHeightsCounter();
  
  /**
   * Returns the width frequencies.
   */
  FloatCounter getWidthsCounter();
}
