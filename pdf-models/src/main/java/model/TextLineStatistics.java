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
   * Returns the largest line pitch.
   */
  float getLargestLinePitch();
  
  /**
   * Returns the smallest pitch.
   */
  float getSmallestLinePitch();
  
  /**
   * Returns the most common line pitch.
   */
  float getMostCommonLinePitch();
  
  /**
   * Returns the average line pitch.
   */
  float getAverageLinePitch();

  /**
   * Returns the smallest significant line pitch.
   */
  float getSmallestSignificantLinepitch();
  
  /**
   * Returns the baseline pitch frequencies.
   */
  Map<Float, Integer> getLinepitchFrequencies();

  /**
   * Returns the largest baseline pitch.
   */
  float getLargestBaselinePitch();
  
  /**
   * Returns the smallest baseline pitch.
   */
  float getSmallestBaselinePitch();
  
  /**
   * Returns the most common baseline pitch.
   */
  float getMostCommonBaselinePitch();
  
  /**
   * Returns the average baseline pitch.
   */
  float getAverageBaselinePitch();
  
  /**
   * Returns the smallest significant baseline pitch.
   */
  float getSmallestSignificantBaselinepitch();
  
  /**
   * Returns the baseline pitch frequencies.
   */
  Map<Float, Integer> getBaselinePitchFrequencies();
}

