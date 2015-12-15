package model;

import java.util.Map;

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
  Map<Float, Integer> getHeightFrequencies();
  
  /**
   * Returns the width frequencies.
   */
  Map<Float, Integer> getWidthFrequencies();
}
