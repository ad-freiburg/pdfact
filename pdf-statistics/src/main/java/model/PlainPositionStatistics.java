package model;

/**
 * A plain implementation of PositionStatistics.
 *
 * @author Claudius Korzen
 *
 */
public class PlainPositionStatistics implements PositionStatistics {
  /**
   * The most common minX value.
   */
  protected float mostCommonMinX;

  /**
   * The most common maxX value.
   */
  protected float mostCommonMaxX;
  
  /**
   * The most common minY value.
   */
  protected float mostCommonMinY;
  
  /**
   * The most common maxYX value.
   */
  protected float mostCommonMaxY;
      
  // ___________________________________________________________________________

  @Override
  public float getMostCommonMinX() {
    return this.mostCommonMinX;
  }

  @Override
  public float getMostCommonMinY() {
    return this.mostCommonMinY;
  }
  
  @Override
  public float getMostCommonMaxX() {
    return this.mostCommonMaxX;
  }

  @Override
  public float getMostCommonMaxY() {
    return this.mostCommonMaxY;
  }
  
  /**
   * Sets the most common minX.
   */
  public void setMostCommonMinX(float minX) {
    this.mostCommonMinX = minX;
  }

  /**
   * Sets the most common minY.
   */
  public void setMostCommonMinY(float minY) {
    this.mostCommonMinY = minY;
  }

  /**
   * Sets the most common maxX.
   */
  public void setMostCommonMaxX(float maxX) {
    this.mostCommonMaxX = maxX;
  }

  /**
   * Sets the most common maxY.
   */
  public void setMostCommonMaxY(float maxY) {
    this.mostCommonMaxY = maxY;
  }
}
