package model;

import java.util.Map;

/**
 * A plain implementation of DimensionStatistics.
 *
 * @author Claudius Korzen
 *
 */
public class PlainDimensionStatistics implements DimensionStatistics {
  /**
   * The average height.
   */
  protected float avgHeight;

  /**
   * The average width.
   */
  protected float avgWidth;
  
  /**
   * The most common height.
   */
  protected float mostCommonHeight;

  /**
   * The most common width.
   */
  protected float mostCommonWidth;
    
  /**
   * The height frequencies.
   */
  protected Map<Float, Integer> heightFreqs;

  /**
   * The height frequencies.
   */
  protected Map<Float, Integer> widthFreqs;
    
  // ___________________________________________________________________________

  @Override
  public float getAverageHeight() {
    return this.avgHeight;
  }

  @Override
  public float getAverageWidth() {
    return this.avgWidth;
  }
  
  @Override
  public float getMostCommonHeight() {
    return this.mostCommonHeight;
  }

  @Override
  public float getMostCommonWidth() {
    return this.mostCommonWidth;
  }
  
  /**
   * Sets the average height.
   */
  public void setAverageHeight(float avgHeight) {
    this.avgHeight = avgHeight;
  }

  /**
   * Sets the average width.
   */
  public void setAverageWidth(float avgWidth) {
    this.avgWidth = avgWidth;
  }

  /**
   * Sets the most common height.
   */
  public void setMostCommonHeight(float mostCommonHeight) {
    this.mostCommonHeight = mostCommonHeight;
  }

  /**
   * Sets the most common width.
   */
  public void setMostCommonWidth(float mostCommonWidth) {
    this.mostCommonWidth = mostCommonWidth;
  }

  /**
   * Returns the height frequencies.
   */
  public Map<Float, Integer> getHeightFrequencies() {
    return heightFreqs;
  }

  /**
   * Sets the height frequencies.
   */
  public void setHeightFrequencies(Map<Float, Integer> heightFreqs) {
    this.heightFreqs = heightFreqs;
  }

  /**
   * Returns the width frequencies.
   */
  public Map<Float, Integer> getWidthFrequencies() {
    return widthFreqs;
  }

  /**
   * Returns the width frequencies.
   */
  public void setWidthFrequencies(Map<Float, Integer> widthFreqs) {
    this.widthFreqs = widthFreqs;
  }
}
