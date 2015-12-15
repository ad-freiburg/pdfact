package model;

import java.util.Map;

/**
 * Interface for text line statistics.
 *
 * @author Claudius Korzen
 *
 */
public interface TextLineStatistics {
  /**
   * Returns the most common line pitch.
   */
  float getMostCommonLinePitch();
  
  /**
   * Returns the average line pitch.
   */
  float getAverageLinePitch();
  
  /**
   * Returns the linepitch frequencies.
   */
  Map<Float, Integer> getLinepitchFrequencies();
}

