package icecite.utils.counter;

import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.map.hash.TFloatIntHashMap;

/**
 * A class that takes float values and computes some statistics about them.
 * 
 * @author Claudius Korzen
 */
public class FloatCounter extends TFloatIntHashMap {
  /**
   * Flag that indicates whether the statistics need to be recomputed.
   */
  protected boolean isStatisticsOutdated = true;

  /**
   * The largest value in this set of floats.
   */
  protected float largestFloat = -Float.MAX_VALUE;

  /**
   * The smallest value in the set of floats.
   */
  protected float smallestFloat = Float.MAX_VALUE;

  /**
   * (One of) the most frequent float(s).
   */
  protected float mostFrequentFloat = -Float.MAX_VALUE;

  /**
   * All most frequent floats.
   */
  protected float[] allMostFrequentFloats;

  /**
   * The frequency of the most frequent float.
   */
  protected int mostFrequentFloatCount = -Integer.MAX_VALUE;

  /**
   * (One of) the least frequent float(s).
   */
  protected float leastFrequentFloat = Float.MAX_VALUE;

  /**
   * All least frequent floats.
   */
  protected float[] allLeastFrequentFloats;

  /**
   * The frequency of the least frequent float.
   */
  protected int leastFrequentFloatCount = Integer.MAX_VALUE;

  /**
   * The average value over all floats.
   */
  protected float averageValue = Float.MAX_VALUE;

  // ==========================================================================
  // Add methods.

  /**
   * Adds all given floats to this counter.
   * 
   * @param floats
   *        The floats to add.
   */
  public void addAll(float[] floats) {
    if (floats == null) {
      return;
    }

    for (float f : floats) {
      add(f);
    }
  }

  /**
   * Adds the given float to this counter.
   * 
   * @param f
   *        The float to add.
   */
  public void add(float f) {
    add(f, 1);
  }

  /**
   * Adds the given FLoatCounter with its associated frequencies to this
   * counter.
   * 
   * @param counter
   *        The counter to add.
   */
  public void add(FloatCounter counter) {
    if (counter == null) {
      return;
    }

    for (float f : counter.getFloats()) {
      add(f, counter.getFrequency(f));
    }
  }

  /**
   * Adds the given float with the given frequency to this counter.
   * 
   * @param f
   *        The float to add.
   * @param freq
   *        The frequency of the float.
   */
  public void add(float f, int freq) {
    if (Float.isNaN(f)) {
      return;
    }

    int count = 0;
    if (containsKey(f)) {
      count = get(f);
    }
    put(f, count + freq);
    this.isStatisticsOutdated = true;
  }

  // ==========================================================================
  // Discount methods. 
  
  /**
   * Discounts 1 from the counts of all given floats.
   * 
   * @param floats
   *        The floats to process.
   */
  public void discount(float[] floats) {
    if (floats == null) {
      return;
    }

    for (float f : floats) {
      remove(f);
    }
  }

  /**
   * Discounts 1 from the count of the given float.
   * 
   * @param f
   *        The float to process.
   */
  public void discount(float f) {
    discount(f, 1);
  }

  /**
   * Discounts the associated frequency from the count of each float in the
   * given counter.
   * 
   * @param counter
   *        The counter to process.
   */
  public void discount(FloatCounter counter) {
    if (counter == null) {
      return;
    }

    for (float f : counter.getFloats()) {
      discount(f, counter.getFrequency(f));
    }
  }

  /**
   * Discounts the given frequency from the count of the given float.
   * 
   * @param f
   *        The float to process.
   * @param freq
   *        The frequency of the float.
   */
  public void discount(float f, int freq) {
    int count = 0;
    if (containsKey(f)) {
      count = get(f);
    }
    put(f, Math.max(0, count - freq));
    this.isStatisticsOutdated = true;
  }
  
  // ==========================================================================

  /**
   * Resets (clears) this counter.
   */
  public void reset() {
    clear();
    resetComputedValues();
  }

  // ==========================================================================
  // Getter methods.

  /**
   * Returns all different floats (that are the keys) in this counter.
   * 
   * @return The array of floats.
   */
  public float[] getFloats() {
    return keys();
  }

  /**
   * Returns the frequency of the given float.
   * 
   * @param f
   *        The float to process.
   * @return The frequency of the given float.
   */
  public int getFrequency(float f) {
    return containsKey(f) ? get(f) : 0;
  }

  /**
   * Returns the frequency of the most frequent float.
   * 
   * @return The frequency of the most frequent float.
   */
  public int getMostFrequentFloatFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentFloatCount;
  }

  /**
   * Returns (one of) the most frequent float.
   * 
   * @return (One of) the most frequent float.
   */
  public float getMostFrequentFloat() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentFloat;
  }

  /**
   * Returns all most frequent floats in an array.
   * 
   * @return All most frequent floats in an array.
   */
  public float[] getAllMostFrequentFloats() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allMostFrequentFloats;
  }

  /**
   * Returns the frequency of the least frequent float.
   * 
   * @return The frequency of the least frequent float.
   */
  public int getLeastFrequentFloatFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentFloatCount;
  }

  /**
   * Returns the least frequent float.
   * 
   * @return The least frequent float.
   */
  public float getLeastFrequentFloat() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentFloat;
  }

  /**
   * Returns all least frequent floats in an array.
   * 
   * @return All least frequent floats in an array.
   */
  public float[] getAllLeastFrequentFloats() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allLeastFrequentFloats;
  }

  /**
   * Returns the average float.
   * 
   * @return The average float.
   */
  public float getAverageValue() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.averageValue;
  }

  /**
   * Returns the smallest float value.
   * 
   * @return The smallest float value.
   */
  public float getSmallestFloat() {
    return this.smallestFloat;
  }

  /**
   * Returns the smallest float value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest float value that occurs at least 'freq'-times.
   */
  public float getSmallestFloatOccuringAtLeast(int freq) {
    float smallestFloat = Float.MAX_VALUE;
    for (float f : keys()) {
      if (get(f) >= freq && f < smallestFloat) {
        smallestFloat = f;
      }
    }
    return smallestFloat;
  }

  /**
   * Returns the smallest float value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest float value that occurs at most 'freq'-times.
   */
  public float getSmallestFloatOccuringAtMost(int freq) {
    float smallestFloat = Float.MAX_VALUE;
    for (float f : keys()) {
      if (get(f) <= freq && f < smallestFloat) {
        smallestFloat = f;
      }
    }
    return smallestFloat;
  }

  /**
   * Returns the largest float value.
   * 
   * @return The largest float value.
   */
  public float getLargestFloat() {
    return this.largestFloat;
  }

  /**
   * Returns the largest float value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest float value that occurs at most 'freq'-times.
   */
  public float getLargestFloatOccuringAtMost(int freq) {
    float largestFloat = -Float.MAX_VALUE;
    for (float f : keys()) {
      if (get(f) <= freq && f > largestFloat) {
        largestFloat = f;
      }
    }
    return largestFloat;
  }

  /**
   * Returns the largest float value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest float value that occurs at least 'freq'-times.
   */
  public float getLargestFloatOccuringAtLeast(int freq) {
    float largestFloat = -Float.MAX_VALUE;
    for (float f : keys()) {
      if (get(f) >= freq && f > largestFloat) {
        largestFloat = f;
      }
    }
    return largestFloat;
  }

  /**
   * Counts the frequencies of the integers.
   */
  protected void count() {
    resetComputedValues();

    float sumFloats = 0;
    float numFloats = 0;
    TFloatArrayList allMostFrequentFloats = new TFloatArrayList();
    TFloatArrayList allLeastFrequentFloats = new TFloatArrayList();

    for (float f : keys()) {
      int count = get(f);

      if (f > this.largestFloat) {
        this.largestFloat = f;
      }

      if (f < this.smallestFloat) {
        this.smallestFloat = f;
      }

      if (count > this.mostFrequentFloatCount) {
        this.mostFrequentFloat = f;
        this.mostFrequentFloatCount = count;
        allMostFrequentFloats.clear();
        allMostFrequentFloats.add(f);
      }

      if (count == this.mostFrequentFloatCount) {
        allMostFrequentFloats.add(f);
      }

      if (count < this.mostFrequentFloatCount) {
        this.leastFrequentFloat = f;
        this.leastFrequentFloatCount = count;
        allLeastFrequentFloats.clear();
        allMostFrequentFloats.add(f);
      }

      if (count == this.leastFrequentFloatCount) {
        allLeastFrequentFloats.add(f);
      }

      sumFloats += count * f;
      numFloats += count;
    }

    this.averageValue = numFloats > 0 ? (sumFloats / numFloats) : 0;
    this.allMostFrequentFloats = allMostFrequentFloats.toArray();
    this.allLeastFrequentFloats = allLeastFrequentFloats.toArray();

    this.isStatisticsOutdated = false;
  }

  /**
   * Resets the internal counters.
   */
  protected void resetComputedValues() {
    this.largestFloat = -Float.MAX_VALUE;
    this.smallestFloat = Float.MAX_VALUE;
    this.mostFrequentFloat = -Float.MAX_VALUE;
    this.allMostFrequentFloats = null;
    this.mostFrequentFloatCount = -Integer.MAX_VALUE;
    this.leastFrequentFloat = Float.MAX_VALUE;
    this.allLeastFrequentFloats = null;
    this.leastFrequentFloatCount = Integer.MAX_VALUE;
    this.averageValue = Float.MAX_VALUE;
  }
}
