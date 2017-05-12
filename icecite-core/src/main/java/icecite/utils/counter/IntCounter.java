package icecite.utils.counter;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * A class that groups objects by associated integer values and computes some
 * statistics.
 * 
 * @param <T>
 *        The type of the objects to group.
 * 
 * @author Claudius Korzen
 */
public class IntCounter<T> extends TIntObjectHashMap<Set<T>> {
  /**
   * Flag that indicates whether the statistics need to be recomputed.
   */
  protected boolean isStatisticOutdated = true;

  /**
   * The largest int value.
   */
  protected int largestInt = -Integer.MAX_VALUE;

  /**
   * The smallest int value.
   */
  protected int smallestInt = Integer.MAX_VALUE;

  /**
   * (One of) the most frequent int values.
   */
  protected int mostFrequentInt = -Integer.MAX_VALUE;

  /**
   * All most frequent int values.
   */
  protected int[] allMostFrequentInts;

  /**
   * The frequency of the most frequent int value.
   */
  protected int mostFrequentIntFrequency = -Integer.MAX_VALUE;

  /**
   * (One of) the least frequent int values.
   */
  protected int leastFrequentInt = Integer.MAX_VALUE;

  /**
   * All least frequent int values.
   */
  protected int[] allLeastFrequentInts;

  /**
   * The frequency of the least frequent int value.
   */
  protected int leastFrequentIntFrequency = Integer.MAX_VALUE;

  /**
   * The average value over all int values.
   */
  protected int averageValue = Integer.MAX_VALUE;

  // ==========================================================================
  // Add methods.

  /**
   * Adds the given object with the given int value.
   * 
   * @param i
   *        The int value to associate with the given object.
   * @param object
   *        The object to group.
   */
  public void add(int i, T object) {
    if (!containsKey(i)) {
      put(i, new HashSet<>());
    }

    get(i).add(object);
    this.isStatisticOutdated = true;
  }

  /**
   * Merges this IntCounter with another IntCounter.
   * 
   * @param counter
   *        The counter to merge with this counter.
   */
  public void add(IntCounter<T> counter) {
    if (counter == null) {
      return;
    }
    if (counter.keys() == null) {
      return;
    }
    for (int i : counter.keys()) {
      for (T object : counter.get(i)) {
        add(i, object);
      }
    }
  }

  // ==========================================================================
  // Remove methods.

  /**
   * Removes the given object with the given int value from this counter.
   * 
   * @param i
   *        The int value associated with the given object.
   * @param object
   *        The object to remove.
   */
  public void remove(int i, T object) {
    this.isStatisticOutdated = containsKey(i) && get(i).remove(object);
  }

  /**
   * Removes the given IntCounter from this counter.
   * 
   * @param counter
   *        The counter to remove.
   */
  public void remove(IntCounter<T> counter) {
    if (counter == null) {
      return;
    }
    if (counter.keys() == null) {
      return;
    }
    for (int i : counter.keys()) {
      for (T object : counter.get(i)) {
        remove(i, object);
      }
    }
  }

  // ==========================================================================
  // Clear methods.

  /**
   * Resets (clears) this counter.
   */
  public void reset() {
    clear();
    resetComputedValues();
  }
  
  /**
   * Resets the internal counters.
   */
  protected void resetComputedValues() {
    this.largestInt = -Integer.MAX_VALUE;
    this.smallestInt = Integer.MAX_VALUE;
    this.mostFrequentInt = -Integer.MAX_VALUE;
    this.allMostFrequentInts = null;
    this.mostFrequentIntFrequency = -Integer.MAX_VALUE;
    this.leastFrequentInt = Integer.MAX_VALUE;
    this.allLeastFrequentInts = null;
    this.leastFrequentIntFrequency = Integer.MAX_VALUE;
    this.averageValue = Integer.MAX_VALUE;
  }

  // ==========================================================================
  // Getter methods.

  /**
   * Returns (one of) the most frequent int values.
   * 
   * @return (One of) the most frequent int values.
   */
  public int getMostFrequentInt() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.mostFrequentInt;
  }

  /**
   * Returns the frequency of the most frequent int value.
   * 
   * @return The frequency of the most frequent int value.
   */
  public int getMostFrequentIntFrequency() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.mostFrequentIntFrequency;
  }

  /**
   * Returns the objects that are associated with the most frequent int in this
   * counter.
   * 
   * @return The objects that are associated with the most frequent int in this
   *         counter.
   */
  public Set<T> getObjectsWithMostFrequentInt() {
    return get(getMostFrequentInt());
  }

  // ==========================================================================

  /**
   * Returns all most frequent int values.
   * 
   * @return All most frequent int values.
   */
  public int[] getAllMostFrequentInts() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.allMostFrequentInts;
  }

  /**
   * Returns all objects that are associated with all most frequent int values
   * in this counter.
   * 
   * @return all objects that are associated with all most frequent int values
   *         in this counter.
   */
  public Set<T> getAllObjectsWithMostFrequentInts() {
    Set<T> allObjects = new HashSet<>();
    for (int i : getAllMostFrequentInts()) {
      allObjects.addAll(get(i));
    }
    return allObjects;
  }

  // ==========================================================================

  /**
   * Returns the least frequent int value.
   * 
   * @return The least frequent int value.
   */
  public int getLeastFrequentInt() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.leastFrequentInt;
  }

  /**
   * Returns the frequency of the least frequent int value.
   * 
   * @return The frequency of the least frequent int value.
   */
  public int getLeastFrequentIntFrequency() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.leastFrequentIntFrequency;
  }

  /**
   * Returns the objects that are associated with the least frequent int in
   * this counter.
   * 
   * @return The objects that are associated with the least frequent int in
   *         this counter.
   */
  public Set<T> getObjectsWithLeastFrequentInt() {
    return get(getLeastFrequentInt());
  }

  // ==========================================================================

  /**
   * Returns all least frequent int values in this counter.
   * 
   * @return All least frequent int values in this counter.
   */
  public int[] getAllLeastFrequentInts() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.allLeastFrequentInts;
  }

  /**
   * Returns all objects that are associated with all least frequent int values
   * in this counter.
   * 
   * @return all objects that are associated with all least frequent int values
   *         in this counter.
   */
  public Set<T> getAllObjectsWithLeastFrequentInts() {
    Set<T> allObjects = new HashSet<>();
    for (int i : getAllMostFrequentInts()) {
      allObjects.addAll(get(i));
    }
    return allObjects;
  }

  // ==========================================================================

  /**
   * Returns the average value over all int values.
   * 
   * @return The average value over all int values.
   */
  public int getAverageValue() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.averageValue;
  }

  // ==========================================================================

  /**
   * Returns the smallest int value.
   * 
   * @return The smallest int value.
   */
  public int getSmallestInt() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.smallestInt;
  }

  /**
   * Returns the objects that are associated with the smallest int value.
   * 
   * @return The objects that are associated with the smallest int value.
   */
  public Set<T> getObjectsWithSmallestInt() {
    return get(getSmallestInt());
  }

  // ==========================================================================

  /**
   * Returns the smallest int value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest int value that occurs at least 'freq'-times.
   */
  public int getSmallestIntOccuringAtLeast(int freq) {
    int smallestInt = Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i).size() >= freq && i < smallestInt) {
        smallestInt = i;
      }
    }
    return smallestInt;
  }

  /**
   * Returns the objects that are associated with the smallest int value that
   * occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The objects that are associated with the smallest int value that
   *         occurs at least 'freq'-times.
   */
  public Set<T> getObjectsWithSmallestIntOccuringAtLeast(int freq) {
    return get(getSmallestIntOccuringAtLeast(freq));
  }

  // ==========================================================================

  /**
   * Returns the smallest int value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The smallest int value that occurs at most 'freq'-times.
   */
  public int getSmallestIntOccuringAtMost(int freq) {
    int smallestInt = Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i).size() <= freq && i < smallestInt) {
        smallestInt = i;
      }
    }
    return smallestInt;
  }

  /**
   * Returns the objects that are associated with the smallest int value that
   * occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The objects that are associated with the smallest int value that
   *         occurs at most 'freq'-times.
   */
  public Set<T> getObjectsWithSmallestIntOccuringAtMost(int freq) {
    return get(getSmallestIntOccuringAtMost(freq));
  }

  // ==========================================================================

  /**
   * Returns the largest int value.
   * 
   * @return The largest int value.
   */
  public int getLargestInt() {
    if (this.isStatisticOutdated) {
      count();
    }
    return this.largestInt;
  }

  /**
   * Returns the objects that are associated with the largest int value.
   * 
   * @return The objects that are associated with the largest int value.
   */
  public Set<T> getObjectWithLargestInt() {
    return get(getLargestInt());
  }

  // ==========================================================================

  /**
   * Returns the largest int value that occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest int value that occurs at most 'freq'-times.
   */
  public int getLargestIntOccuringAtMost(int freq) {
    int largestInt = -Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i).size() <= freq && i > largestInt) {
        largestInt = i;
      }
    }
    return largestInt;
  }

  /**
   * Returns the objects that are associated with the largest int value that
   * occurs at most 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The objects that are associated with the largest int value that
   *         occurs at most 'freq'-times.
   */
  public Set<T> getObjectsWithLargestIntOccuringAtMost(int freq) {
    return get(getLargestIntOccuringAtMost(freq));
  }

  // ==========================================================================

  /**
   * Returns the largest int value that occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The largest int value that occurs at least 'freq'-times.
   */
  public int getLargestIntOccuringAtLeast(int freq) {
    int largestInt = -Integer.MAX_VALUE;
    for (int i : keys()) {
      if (get(i).size() >= freq && i > largestInt) {
        largestInt = i;
      }
    }
    return largestInt;
  }

  /**
   * Returns the objects that are associated with the largest int value that
   * occurs at least 'freq'-times.
   * 
   * @param freq
   *        The frequency.
   * @return The objects that are associated with the largest int value that
   *         occurs at least 'freq'-times.
   */
  public Set<T> getObjectsWithLargestIntOccuringAtLeast(int freq) {
    return get(getLargestIntOccuringAtLeast(freq));
  }

  // ==========================================================================

  /**
   * Counts the frequencies of the ints.
   */
  protected void count() {
    resetComputedValues();

    int sumIntegers = 0;
    int numIntegers = 0;
    TIntArrayList allMostFrequentIntegers = new TIntArrayList();
    TIntArrayList allLeastFrequentIntegers = new TIntArrayList();

    for (int f : keys()) {
      int count = get(f).size();

      if (count == 0) {
        continue;
      }

      if (f > this.largestInt) {
        this.largestInt = f;
      }

      if (f < this.smallestInt) {
        this.smallestInt = f;
      }

      if (count > this.mostFrequentIntFrequency) {
        this.mostFrequentInt = f;
        this.mostFrequentIntFrequency = count;
        allMostFrequentIntegers.clear();
        allMostFrequentIntegers.add(f);
      }

      if (count == this.mostFrequentIntFrequency) {
        allMostFrequentIntegers.add(f);
      }

      if (count < this.mostFrequentIntFrequency) {
        this.leastFrequentInt = f;
        this.leastFrequentIntFrequency = count;
        allLeastFrequentIntegers.clear();
        allMostFrequentIntegers.add(f);
      }

      if (count == this.leastFrequentIntFrequency) {
        allLeastFrequentIntegers.add(f);
      }

      sumIntegers += count * f;
      numIntegers += count;
    }

    this.averageValue = numIntegers > 0 ? (sumIntegers / numIntegers) : 0;
    this.allMostFrequentInts = allMostFrequentIntegers.toArray();
    this.allLeastFrequentInts = allLeastFrequentIntegers.toArray();

    this.isStatisticOutdated = false;
  }

  // ==========================================================================
  // Util methods.

  @Override
  public Set<T> get(int i) {
    if (containsKey(i)) {
      return super.get(i);
    }
    // Return an empty list if this counter does not contain the given int.
    return new HashSet<>();
  }
}
