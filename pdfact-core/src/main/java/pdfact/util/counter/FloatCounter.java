package pdfact.util.counter;

import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.hash.TFloatIntHashMap;

/**
 * A counter to compute some statistics about float values.
 * 
 * @author Claudius Korzen
 */
public class FloatCounter extends TFloatIntHashMap {
  /**
   * The default initial capacity of this counter.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * The most common float.
   */
  protected float mostCommonFloat = Float.NaN;

  /**
   * The average float.
   */
  protected float averageFloat = Float.NaN;

  /**
   * A flag that indicates whether the statistics were already computed.
   */
  protected boolean isStatisticsComputed;

  /**
   * Creates a new FloatCounter with the default initial capacity.
   */
  public FloatCounter() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new FloatCounter with the given initial capacity.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  public FloatCounter(int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  /**
   * Adds the given float to this counter.
   * 
   * @param f
   *        The float to add.
   */
  public void add(float f) {
    adjustOrPutValue(f, 1, 1);
  }

  /**
   * Adds the given float counter to this counter.
   * 
   * @param f
   *        The float to add.
   */
  public void add(FloatCounter f) {
    for (float key : f.keys()) {
      adjustOrPutValue(key, f.get(key), f.get(key));
    }
  }

  /**
   * Returns the most common float.
   * 
   * @return The most common float in this counter or Float.NaN if the counter
   *         is empty.
   */
  public float getMostCommonFloat() {
    if (!this.isStatisticsComputed) {
      computeStatistic();
    }
    return this.mostCommonFloat;
  }

  /**
   * Returns the frequency of the most common float.
   * 
   * @return The frequency of the most common object in this counter.
   */
  public float getMostCommonFloatFreq() {
    return get(getMostCommonFloat());
  }

  /**
   * Returns the average float.
   * 
   * @return The average value of the float values.
   */
  public float getAverageFloat() {
    if (!this.isStatisticsComputed) {
      computeStatistic();
    }
    return this.averageFloat;
  }

  /**
   * Computes the statistic about the float values.
   */
  protected void computeStatistic() {
    float sumFloats = 0;
    float sumFreqs = 0;
    int largestFreq = -1;

    TFloatIntIterator itr = iterator();
    while (itr.hasNext()) {
      itr.advance();
      float f = itr.key();
      int freq = itr.value();
      
      if (freq > largestFreq) {
        this.mostCommonFloat = f;
        largestFreq = freq;
      }
      
      sumFloats += freq * f;
      sumFreqs += freq;
    }

    this.averageFloat = sumFreqs > 0 ? sumFloats / sumFreqs : 0;
    this.isStatisticsComputed = true;
  }
}
