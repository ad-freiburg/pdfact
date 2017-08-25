package pdfact.util.counter;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * A counter to compute some statistics about objects.
 * 
 * @param <T>
 *        The type of the objects to count.
 * 
 * @author Claudius Korzen
 */
public class ObjectCounter<T> extends TObjectIntHashMap<T> {
  /**
   * The default initial capacity of this counter.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * The most common object.
   */
  protected T mostCommonObject;

  /**
   * A flag that indicates whether the statistics were already computed.
   */
  protected boolean isStatisticsComputed;

  /**
   * Creates a new ObjectCounter with the default initial capacity.
   */
  public ObjectCounter() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new ObjectCounter with the given initial capacity.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  public ObjectCounter(int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  /**
   * Adds the given object to this counter.
   * 
   * @param o
   *        The object to add.
   */
  public void add(T o) {
    adjustOrPutValue(o, 1, 1);
  }

  /**
   * Adds the given ObjectCounter to this counter.
   * 
   * @param o
   *        The object to add.
   */
  public void add(ObjectCounter<T> o) {
    TObjectIntIterator<T> itr = o.iterator();
    while (itr.hasNext()) {
      itr.advance();
      T key = itr.key();
      int count = itr.value();
      adjustOrPutValue(key, count, count);
    }
  }

  /**
   * Returns the most common object.
   * 
   * @return The most common object in this counter or null if the counter is
   *         empty.
   */
  public T getMostCommonObject() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.mostCommonObject;
  }

  /**
   * Returns the frequency of the most common object.
   * 
   * @return The frequency of the most common object in this counter.
   */
  public int getMostCommonObjectFreq() {
    return get(getMostCommonObject());
  }

  /**
   * Computes some statistics about the objects.
   */
  protected void computeStatistics() {
    int largestFreq = -1;

    TObjectIntIterator<T> itr = iterator();
    while (itr.hasNext()) {
      itr.advance();
      T object = itr.key();
      int freq = itr.value();
      if (freq > largestFreq) {
        this.mostCommonObject = object;
        largestFreq = freq;
      }
    }
    this.isStatisticsComputed = true;
  }
}
