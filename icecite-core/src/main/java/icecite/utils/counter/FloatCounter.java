package icecite.utils.counter;

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

  // ==========================================================================
  // Constructors.

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
  // Public methods.

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
      computeStatistics();
    }
    return this.mostCommonFloat;
  }

  /**
   * Returns the most common float.
   * 
   * @return The most common float in this counter or Float.NaN if the counter
   *         is empty.
   */
  public float getMostCommonFloatFreq() {
    return get(getMostCommonFloat());
  }

  /**
   * Returns the average float.
   * 
   * @return The average over the float values.
   */
  public float getAverageFloat() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.averageFloat;
  }

  /**
   * Computes some statistics about the float values.
   */
  protected void computeStatistics() {
    float sumFloats = 0;
    float sumFreqs = 0;
    int largestFreq = -1;

    // Do nothing if the counter is empty.
    if (isEmpty()) {
      return;
    }

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

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
