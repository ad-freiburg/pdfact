package model;

import java.util.Map;

/**
 * The statistics for text lines.
 *
 * @author Claudius Korzen
 *
 */
public class PlainTextLineStatistics implements TextLineStatistics {
  /**
   * The most common line pitch.
   */
  protected float mostCommonLinePitch;
  
  /**
   * The average common line pitch.
   */
  protected float averageLinePitch;
  
  /**
   * The line pitch frequencies.
   */
  protected Map<Float, Integer> linepitchFrequencies;
  
  @Override
  public float getMostCommonLinePitch() {
    return mostCommonLinePitch;
  }

  @Override
  public float getAverageLinePitch() {
    return averageLinePitch;
  }

  @Override
  public Map<Float, Integer> getLinepitchFrequencies() {
    return linepitchFrequencies;
  }

  /**
   * Sets the most common line pitch.
   */
  public void setMostCommonLinePitch(float mostCommonLinePitch) {
    this.mostCommonLinePitch = mostCommonLinePitch;
  }

  /**
   * Sets the average line pitch.
   */
  public void setAverageLinePitch(float averageLinePitch) {
    this.averageLinePitch = averageLinePitch;
  }
  
  /**
   * Sets the line pitch frequencies.
   */
  public void setLinepitchFrequencies(Map<Float, Integer> freqs) {
    this.linepitchFrequencies = freqs;
  }
}
