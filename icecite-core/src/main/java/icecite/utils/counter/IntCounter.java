package icecite.utils.counter;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

/**
 * A class that takes integer values and computes some statistics about them.
 * 
 * @author Claudius Korzen
 */
public class IntCounter extends TIntIntHashMap {
  /**
   * Flag that indicates whether the statistics need to be recomputed.
   */
  protected boolean isStatisticsOutdated = true;

  /**
   * The largest value in this set of integers.
   */
  protected int largestInt = -Integer.MAX_VALUE;

  /**
   * The smallest value in the set of integers.
   */
  protected int smallestInt = Integer.MAX_VALUE;

  /**
   * (One of) the most frequent integer(s).
   */
  protected int mostFrequentInt = -Integer.MAX_VALUE;

  /**
   * All most frequent integers.
   */
  protected int[] allMostFrequentInts;

  /**
   * The frequency of the most frequent integer.
   */
  protected int mostFrequentIntCount = -Integer.MAX_VALUE;

  /**
   * (One of) the least frequent integer(s).
   */
  protected int leastFrequentInt = Integer.MAX_VALUE;

  /**
   * All least frequent integers.
   */
  protected int[] allLeastFrequentInts;

  /**
   * The frequency of the least frequent integer.
   */
  protected int leastFrequentIntCount = Integer.MAX_VALUE;

  /**
   * The average value over all integers.
   */
  protected int averageValue = Integer.MAX_VALUE;

  // ==========================================================================
  // Add methods.

  /**
   * Adds all given integers to this counter.
   * 
   * @param ints
   *        The integers to add.
   */
  public void addAll(int[] ints) {
    if (ints == null) {
      return;
    }

    for (int i : ints) {
      add(i);
    }
  }

  /**
   * Adds the given integer to this counter.
   * 
   * @param i
   *        The integer to add.
   */
  public void add(int i) {
    add(i, 1);
  }

  /**
   * Adds the given IntCounter with its associated frequencies to this counter.
   * 
   * @param counter
   *        The counter to add.
   */
  public void add(IntCounter counter) {
    if (counter == null) {
      return;
    }

    for (int i : counter.getInts()) {
      add(i, counter.getFrequency(i));
    }
  }

  /**
   * Adds the given int with the given frequency to this counter.
   * 
   * @param i
   *        The int to add.
   * @param freq
   *        The frequency of the integer.
   */
  public void add(int i, int freq) {
    int count = 0;
    if (containsKey(i)) {
      count = get(i);
    }
    put(i, count + freq);
    this.isStatisticsOutdated = true;
  }

  // ==========================================================================
  // Discount methods. 
  
  /**
   * Discounts 1 from the counts of all given ints.
   * 
   * @param ints
   *        The ints to process.
   */
  public void discount(int[] ints) {
    if (ints == null) {
      return;
    }

    for (int i : ints) {
      remove(i);
    }
  }

  /**
   * Discounts 1 from the count of the given int.
   * 
   * @param i
   *        The int to process.
   */
  public void discount(int i) {
    discount(i, 1);
  }

  /**
   * Discounts the associated frequency from the count of each int in the
   * given counter.
   * 
   * @param counter
   *        The counter to process.
   */
  public void discount(IntCounter counter) {
    if (counter == null) {
      return;
    }

    for (int i : counter.getInts()) {
      discount(i, counter.getFrequency(i));
    }
  }

  /**
   * Discounts the given frequency from the count of the given int.
   * 
   * @param i
   *        The int to process.
   * @param freq
   *        The frequency of the int.
   */
  public void discount(int i, int freq) {
    int count = 0;
    if (containsKey(i)) {
      count = get(i);
    }
    put(i, Math.max(0, count - freq));
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
   * Returns all different integers (that are the keys) in this counter.
   * 
   * @return The array of integers.
   */
  public int[] getInts() {
    return keys();
  }

  /**
   * Returns the frequency of the given integer.
   * 
   * @param i
   *        The int to process.
   * @return The frequency of the given integer.
   */
  public int getFrequency(int i) {
    return containsKey(i) ? get(i) : 0;
  }

  /**
   * Returns the frequency of the most frequent integer.
   * 
   * @return The frequency of the most frequent integer.
   */
  public int getMostFrequentIntFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentIntCount;
  }

  /**
   * Returns (one of) the most frequent integer.
   * 
   * @return (One of) the most frequent integer.
   */
  public int getMostFrequentInt() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentInt;
  }

  /**
   * Returns all most frequent integers in an array.
   * 
   * @return All most frequent integers in an array.
   */
  public int[] getAllMostFrequentIntegers() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allMostFrequentInts;
  }

  /**
   * Returns the frequency of the least frequent integer.
   * 
   * @return The frequency of the least frequent integer.
   */
  public int getLeastFrequentIntegerFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentIntCount;
  }

  /**
   * Returns the least frequent integer.
   * 
   * @return The least frequent integer.
   */
  public int getLeastFrequentInteger() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentInt;
  }

  /**
   * Returns all least frequent integers in an array.
   * 
   * @return All least frequent integers in an array.
   */
  public int[] getAllLeastFrequentIntegers() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allLeastFrequentInts;
  }

  /**
   * Returns the average integer.
   * 
   * @return The average integer.
   */
  public int getAverageValue() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.averageValue;
  }

  /**
   * Returns the smallest int value.
   * 
   * @return The smallest int value.
   */
  public int getSmallestInteger() {
    return this.smallestInt;
  }

  /**
   * Returns the smallest int value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest int value that occurs at least 'freq'-times.
   */
  public int getSmallestIntegerOccuringAtLeast(int freq) {
    int smallestInteger = Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i) >= freq && i < smallestInteger) {
        smallestInteger = i;
      }
    }
    return smallestInteger;
  }

  /**
   * Returns the smallest int value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest int value that occurs at most 'freq'-times.
   */
  public int getSmallestIntegerOccuringAtMost(int freq) {
    int smallestInteger = Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i) <= freq && i < smallestInteger) {
        smallestInteger = i;
      }
    }
    return smallestInteger;
  }

  /**
   * Returns the largest int value.
   * 
   * @return The largest int value.
   */
  public int getLargestInteger() {
    return this.largestInt;
  }

  /**
   * Returns the largest int value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest int value that occurs at most 'freq'-times.
   */
  public int getLargestIntegerOccuringAtMost(int freq) {
    int largestInteger = -Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i) <= freq && i > largestInteger) {
        largestInteger = i;
      }
    }
    return largestInteger;
  }

  /**
   * Returns the largest int value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest int value that occurs at least 'freq'-times.
   */
  public int getLargestIntegerOccuringAtLeast(int freq) {
    int largestInteger = -Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i) >= freq && i > largestInteger) {
        largestInteger = i;
      }
    }
    return largestInteger;
  }

  /**
   * Counts the frequencies of the integers.
   */
  protected void count() {
    resetComputedValues();

    int sumIntegers = 0;
    int numIntegers = 0;
    TIntArrayList allMostFrequentIntegers = new TIntArrayList();
    TIntArrayList allLeastFrequentIntegers = new TIntArrayList();

    for (int f : keys()) {
      int count = get(f);

      if (f > this.largestInt) {
        this.largestInt = f;
      }

      if (f < this.smallestInt) {
        this.smallestInt = f;
      }

      if (count > this.mostFrequentIntCount) {
        this.mostFrequentInt = f;
        this.mostFrequentIntCount = count;
        allMostFrequentIntegers.clear();
        allMostFrequentIntegers.add(f);
      }

      if (count == this.mostFrequentIntCount) {
        allMostFrequentIntegers.add(f);
      }

      if (count < this.mostFrequentIntCount) {
        this.leastFrequentInt = f;
        this.leastFrequentIntCount = count;
        allLeastFrequentIntegers.clear();
        allMostFrequentIntegers.add(f);
      }

      if (count == this.leastFrequentIntCount) {
        allLeastFrequentIntegers.add(f);
      }

      sumIntegers += count * f;
      numIntegers += count;
    }

    this.averageValue = numIntegers > 0 ? (sumIntegers / numIntegers) : 0;
    this.allMostFrequentInts = allMostFrequentIntegers.toArray();
    this.allLeastFrequentInts = allLeastFrequentIntegers.toArray();

    this.isStatisticsOutdated = false;
  }

  /**
   * Resets the internal counters.
   */
  protected void resetComputedValues() {
    this.largestInt = -Integer.MAX_VALUE;
    this.smallestInt = Integer.MAX_VALUE;
    this.mostFrequentInt = -Integer.MAX_VALUE;
    this.allMostFrequentInts = null;
    this.mostFrequentIntCount = -Integer.MAX_VALUE;
    this.leastFrequentInt = Integer.MAX_VALUE;
    this.allLeastFrequentInts = null;
    this.leastFrequentIntCount = Integer.MAX_VALUE;
    this.averageValue = Integer.MAX_VALUE;
  }
}
