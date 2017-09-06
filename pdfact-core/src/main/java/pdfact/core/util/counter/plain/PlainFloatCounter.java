package pdfact.core.util.counter.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.hash.TFloatIntHashMap;
import pdfact.core.util.counter.FloatCounter;

/**
 * A plain implementation of {@link FloatCounter}.
 * 
 * @author Claudius Korzen
 */
public class PlainFloatCounter extends TFloatIntHashMap
    implements FloatCounter {
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
  @AssistedInject
  public PlainFloatCounter() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new FloatCounter with the given initial capacity.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainFloatCounter(@Assisted int initialCapacity) {
    super(initialCapacity, DEFAULT_LOAD_FACTOR, 0, 0);
  }

  // ==========================================================================

  @Override
  public void add(float f) {
    adjustOrPutValue(f, 1, 1);
  }

  @Override
  public void add(FloatCounter f) {
    for (float key : f.getFloats()) {
      adjustOrPutValue(key, f.getFrequency(key), f.getFrequency(key));
    }
  }

  // ==========================================================================

  @Override
  public float getMostCommonFloat() {
    if (!this.isStatisticsComputed) {
      computeStatistic();
    }
    return this.mostCommonFloat;
  }

  @Override
  public float getMostCommonFloatFrequency() {
    return getFrequency(getMostCommonFloat());
  }

  // ==========================================================================

  @Override
  public float getAverageFloat() {
    if (!this.isStatisticsComputed) {
      computeStatistic();
    }
    return this.averageFloat;
  }

  // ==========================================================================

  @Override
  public float[] getFloats() {
    return keys();
  }

  @Override
  public int getFrequency(float value) {
    return get(value);
  }

  // ==========================================================================

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
