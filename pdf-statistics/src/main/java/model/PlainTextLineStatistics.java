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
   * The largest line pitch.
   */
  protected float largestLinePitch;
  
  /**
   * The smallest line pitch.
   */
  protected float smallestLinePitch;
  
  /**
   * The smallest significant line pitch.
   */
  protected float smallestSignificantLinePitch;
  
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
  
  /**
   * The largest base line pitch.
   */
  protected float largestBaselinePitch;
  
  /**
   * The smallest base line pitch.
   */
  protected float smallestBaselinePitch;
  
  /**
   * The average base line pitch.
   */
  protected float averageBaselinePitch;
  
  /**
   * The most common base line pitch.
   */
  protected float mostCommonBaselinePitch;
  
  /**
   * The smallest significant baseline pitch.
   */
  protected float smallestSignificantBaselinePitch;
  
  /**
   * The most common line pitch frequencies.
   */
  protected Map<Float, Integer> baselinePitchFrequencies;
  
  @Override
  public float getMostCommonLinePitch() {
    return mostCommonLinePitch;
  }
  
  @Override
  public float getMostCommonBaselinePitch() {
    return mostCommonBaselinePitch;
  }

  @Override
  public float getAverageLinePitch() {
    return averageLinePitch;
  }

  @Override
  public float getAverageBaselinePitch() {
    return averageBaselinePitch;
  }
  
  @Override
  public float getLargestLinePitch() {
    return largestLinePitch;
  }
  
  @Override
  public float getLargestBaselinePitch() {
    return largestBaselinePitch;
  }
  
  @Override
  public float getSmallestLinePitch() {
    return smallestLinePitch;
  }
  
  @Override
  public float getSmallestBaselinePitch() {
    return smallestBaselinePitch;
  }
  
  @Override
  public float getSmallestSignificantLinepitch() {
    return smallestSignificantLinePitch;
  }
  
  @Override
  public float getSmallestSignificantBaselinepitch() {
    return smallestSignificantBaselinePitch;
  }
  
  @Override
  public Map<Float, Integer> getLinepitchFrequencies() {
    return linepitchFrequencies;
  }
  
  @Override
  public Map<Float, Integer> getBaselinePitchFrequencies() {
    return baselinePitchFrequencies;
  }
  
  /**
   * Sets the most common line pitch.
   */
  public void setMostCommonLinePitch(float mostCommonLinePitch) {
    this.mostCommonLinePitch = mostCommonLinePitch;
  }

  /**
   * Sets the most common base line pitch.
   */
  public void setMostCommonBaselinePitch(float mostCommonBaselinePitch) {
    this.mostCommonBaselinePitch = mostCommonBaselinePitch;
  }
  
  /**
   * Sets the average line pitch.
   */
  public void setAverageLinePitch(float averageLinePitch) {
    this.averageLinePitch = averageLinePitch;
  }
  
  /**
   * Sets the average base line pitch.
   */
  public void setAverageBaselinePitch(float averageLinePitch) {
    this.averageBaselinePitch = averageLinePitch;
  }
  
  /**
   * Sets the line pitch frequencies.
   */
  public void setLinepitchFrequencies(Map<Float, Integer> freqs) {
    this.linepitchFrequencies = freqs;
  }
  
  /**
   * Sets the base line pitch frequencies.
   */
  public void setBaselinePitchFrequencies(Map<Float, Integer> freqs) {
    this.baselinePitchFrequencies = freqs;
  }
  
  /**
   * Sets the smallest line pitch.
   */
  public void setSmallestLinePitch(float pitch) {
    this.smallestLinePitch = pitch;
  }
  
  /**
   * Sets the smallest base line pitch.
   */
  public void setSmallestBaselinePitch(float pitch) {
    this.smallestBaselinePitch = pitch;
  }
  
  /**
   * Sets the largest line pitch.
   */
  public void setLargestLinePitch(float pitch) {
    this.largestLinePitch = pitch;
  }
  
  /**
   * Sets the largest baseline pitch.
   */
  public void setLargestBaselinePitch(float pitch) {
    this.largestBaselinePitch = pitch;
  }

  /**
   * Sets the smallest significant line pitch.
   */
  public void setSmallestSignificantLinePitch(float pitch) {
    this.smallestSignificantLinePitch = pitch;
  }
  
  /**
   * Sets the smallest significant baseline pitch.
   */
  public void setSmallestSignificantBaselinePitch(float pitch) {
    this.smallestSignificantBaselinePitch = pitch;
  }
}
