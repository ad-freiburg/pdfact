package icecite.utils.counter;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * A class that takes objects and computes some statistics about them.
 * 
 * @author Claudius Korzen
 *
 * @param <K>
 *        The type of objects to count.
 */
public class ObjectCounter<K> extends TObjectIntHashMap<K> {
  /**
   * Flag that indicates whether the statistics need to be recomputed.
   */
  protected boolean isStatisticsOutdated = true;

  /**
   * (One of) the most frequent object(s).
   */
  protected K mostFrequentObject = null;

  /**
   * All most frequent objects.
   */
  protected Set<K> allMostFrequentObjects = new HashSet<>();

  /**
   * The frequency of the most frequent object.
   */
  protected int mostFrequentObjectCount = -Integer.MAX_VALUE;

  /**
   * (One of) the least frequent object(s).
   */
  protected K leastFrequentObject = null;

  /**
   * All least frequent objects.
   */
  protected Set<K> allLeastFrequentObjects = new HashSet<>();

  /**
   * The frequency of the least frequent object.
   */
  protected int leastFrequentObjectCount = Integer.MAX_VALUE;

  // ==========================================================================
  // Add methods.

  /**
   * Adds all given objects to this counter.
   * 
   * @param objects
   *        The objects to add.
   */
  public void addAll(Iterable<K> objects) {
    if (objects == null) {
      return;
    }

    for (K object : objects) {
      add(object);
    }
  }

  /**
   * Adds the given object to this counter.
   * 
   * @param object
   *        The object to add.
   */
  public void add(K object) {
    add(object, 1);
  }

  /**
   * Adds all objects in the given counter with its associated frequency to
   * this counter.
   * 
   * @param counter
   *        The counter to add.
   */
  public void add(ObjectCounter<K> counter) {
    if (counter == null) {
      return;
    }

    for (K key : counter.getObjects()) {
      add(key, counter.getFrequency(key));
    }
  }

  /**
   * Adds the given object with given frequency to this counter.
   * 
   * @param object
   *        The object to add.
   * @param freq
   *        The frequency of the object.
   */
  public void add(K object, int freq) {
    int count = 0;
    if (containsKey(object)) {
      count = get(object);
    }
    put(object, count + freq);
    this.isStatisticsOutdated = true;
  }

  // ==========================================================================
  // Discount methods.

  /**
   * Discounts 1 from the counts of all given objects.
   * 
   * @param objects
   *        The objects to process.
   */
  public void discount(Iterable<K> objects) {
    if (objects == null) {
      return;
    }

    for (K object : objects) {
      remove(object);
    }
  }

  /**
   * Discounts 1 from the count of the given object.
   * 
   * @param object
   *        The object to process.
   */
  public void discount(K object) {
    discount(object, 1);
  }

  /**
   * Discounts the associated frequency from the count of each object in the
   * given counter.
   * 
   * @param counter
   *        The counter to process.
   */
  public void discount(ObjectCounter<K> counter) {
    if (counter == null) {
      return;
    }

    for (K key : counter.getObjects()) {
      discount(key, counter.getFrequency(key));
    }
  }

  /**
   * Discounts the given frequency from the count of the given object.
   * 
   * @param object
   *        The object to process.
   * @param freq
   *        The frequency of the object.
   */
  public void discount(K object, int freq) {
    int count = 0;
    if (containsKey(object)) {
      count = get(object);
    }
    put(object, Math.max(0, count - freq));
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
   * Returns all different objects (that are the keys) in this counter.
   * 
   * @return The objects in this counter.
   */
  public Set<K> getObjects() {
    return keySet();
  }

  /**
   * Returns the frequency of the given object.
   * 
   * @param object
   *        The object.
   * @return The frequency of the object.
   */
  public int getFrequency(K object) {
    return containsKey(object) ? get(object) : 0;
  }

  /**
   * Returns the frequency of the most frequent object.
   * 
   * @return The frequency of the most frequent object.
   */
  public int getMostFrequentObjectFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentObjectCount;
  }

  /**
   * Returns the most frequent object.
   * 
   * @return The most frequent object.
   */
  public K getMostFrequentObject() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.mostFrequentObject;
  }

  /**
   * Returns all most frequent objects in a set.
   * 
   * @return All most frequent objects in a set.
   */
  public Set<K> getAllMostFrequentObjects() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allMostFrequentObjects;
  }

  /**
   * Returns the frequency of the least frequent object.
   * 
   * @return The frequency of the least frequent object.
   */
  public int getLeastFrequentObjectFrequency() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentObjectCount;
  }

  /**
   * Returns the least frequent object.
   * 
   * @return The least frequent object.
   */
  public K getLeastFrequentObject() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.leastFrequentObject;
  }

  /**
   * Returns all least frequent objects in a set.
   * 
   * @return All least frequent objects in a set.
   */
  public Set<K> getAllLeastFrequentObjects() {
    if (this.isStatisticsOutdated) {
      count();
    }
    return this.allLeastFrequentObjects;
  }

  /**
   * Counts the frequencies of the objects.
   */
  protected void count() {
    resetComputedValues();

    for (K object : keySet()) {
      int count = get(object);

      if (count == 0) {
        continue;
      }
      
      if (count > this.mostFrequentObjectCount) {
        this.mostFrequentObject = object;
        this.mostFrequentObjectCount = get(object);
        this.allMostFrequentObjects.clear();
        this.allMostFrequentObjects.add(object);
      }

      if (count == this.mostFrequentObjectCount) {
        this.allMostFrequentObjects.add(object);
      }

      if (count < this.leastFrequentObjectCount) {
        this.leastFrequentObject = object;
        this.leastFrequentObjectCount = get(object);
        this.allLeastFrequentObjects.clear();
        this.allLeastFrequentObjects.add(object);
      }

      if (count == this.leastFrequentObjectCount) {
        this.allLeastFrequentObjects.add(object);
      }
    }
    this.isStatisticsOutdated = false;
  }

  /**
   * Resets the internal counters.
   */
  protected void resetComputedValues() {
    this.mostFrequentObject = null;
    this.allMostFrequentObjects = new HashSet<>();
    this.mostFrequentObjectCount = -Integer.MAX_VALUE;
    this.leastFrequentObject = null;
    this.allLeastFrequentObjects = new HashSet<>();
    this.leastFrequentObjectCount = Integer.MAX_VALUE;
  }
}
