package icecite.utils.counter;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.map.hash.TFloatObjectHashMap;

/**
 * A counter to compute some statistics about a collection of float values.
 * 
 * @param <T>
 *        The type of the elements.
 * 
 * @author Claudius Korzen
 */
public class FloatCounter<T> {
  /**
   * The default initial capacity of this counter.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * The elements, grouped by the individual float values.
   */
  protected TFloatObjectHashMap<Set<T>> elements;

  /**
   * A queue that holds the floats in ascending order.
   */
  protected FloatPriorityQueue asc;

  /**
   * A queue that holds the floats in descending order.
   */
  protected FloatPriorityQueue desc;

  /**
   * A queue that holds the floats by frequency in ascending order.
   */
  protected FloatPriorityQueue freqsAsc;

  /**
   * A queue that holds the floats by frequency in descending order.
   */
  protected FloatPriorityQueue freqsDesc;

  /**
   * The average over all float values in this counter.
   */
  protected float avgFloat;

  /**
   * The number of elements in this counter.
   */
  protected int numElements;
  
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
    this.elements = new TFloatObjectHashMap<>(initialCapacity);
    this.asc = new FloatPriorityQueue(initialCapacity);
    this.desc = new FloatPriorityQueue(initialCapacity, true);
    this.freqsAsc = new FloatPriorityQueue(initialCapacity);
    this.freqsDesc = new FloatPriorityQueue(initialCapacity, true);
  }

  // ==========================================================================
  // Public methods.

  /**
   * Adds the given float associated with the given element to this counter.
   * 
   * @param f
   *        The float to add.
   * @param element
   *        The associated element.
   */
  public void add(float f, T element) {
    if (!this.elements.containsKey(f)) {
      // Create a new bucket.
      this.elements.put(f, new HashSet<>());
    }

    // Extend the existing bucket.
    Set<T> bucket = this.elements.get(f);
    boolean added = bucket.add(element);
    
    if (added) {
      // Update the queues.
      this.asc.insert(f, f);
      this.desc.insert(f, f);
      // Update the frequencies.
      float freq = this.freqsAsc.getPriority(f, 0);
      this.freqsAsc.updatePriority(f, freq + 1);
      this.freqsDesc.updatePriority(f, freq + 1);
      // Update the average value.
      this.avgFloat += (f - this.avgFloat) / (this.numElements + 1);
      // Increment elements counter.
      this.numElements++;
    }
  }

  /**
   * Removes the given element with the given float from this counter.
   * 
   * @param f
   *        The float to remove.
   * @param element
   *        The element to remove.
   */
  public void remove(float f, Object element) {
    if (!this.elements.containsKey(f)) {
      return;
    }

    Set<T> set = this.elements.get(f);
    if (set == null || set.isEmpty()) {
      return;
    }
    boolean removed = set.remove(element);
    
    if (removed) {
      // Update the queues.
      this.asc.remove(f);
      this.desc.remove(f);
      // Update the frequencies.
      float freq = this.freqsAsc.getPriority(f, 0);
      if (freq == 1) {
        this.freqsAsc.remove(f);
        this.freqsDesc.remove(f);
      } else if (freq > 1) {
        this.freqsAsc.updatePriority(f, freq - 1);
        this.freqsDesc.updatePriority(f, freq - 1);
      }
      // Update the average value.
      int n = this.numElements;
      if (n > 1) {
        this.avgFloat = ((n * this.avgFloat) - f) / (n - 1);
      } else {
        this.avgFloat = 0;
      }
      this.numElements--;
    }
  }

  /**
   * Returns the number of elements in this counter.
   * 
   * @return The number of elements in this counter.
   */
  public int size() {
    return this.numElements;
  }
  
  /**
   * Returns true if this counter has no elements.
   * 
   * @return True if this counter has no elements, False otherwise.
   */
  public boolean isEmpty() {
    return size() == 0;
  }
  
  // ==========================================================================
  
  /**
   * Clears this counter.
   */
  public void clear() {
    this.elements.clear();
    this.asc.clear();
    this.desc.clear();
    this.freqsAsc.clear();
    this.freqsDesc.clear();
    this.avgFloat = 0;
    this.numElements = 0;
  }
  
  // ==========================================================================

  /**
   * Returns the average value over all float values in this counter.
   * 
   * @return The average value.
   */
  public float getAverageFloat() {
    return this.avgFloat;
  }

  // ==========================================================================

  /**
   * Returns the largest float in this counter.
   * 
   * @return The largest float in this counter or Float.NaN if the counter does
   *         not contain any float values.
   */
  public float getLargestFloat() {
    return !this.desc.isEmpty() ? this.desc.peek() : Float.NaN;
  }

  /**
   * Returns the elements associated with the largest float in this counter.
   * 
   * @return The elements associated with the largest float in this counter or
   *         null if the counter does not contain any float values.
   */
  public Set<T> getElementsWithLargestFloat() {
    return !this.desc.isEmpty() ? this.elements.get(this.desc.peek()) : null;
  }

  // ==========================================================================

  /**
   * Returns the smallest float in this counter.
   * 
   * @return The smallest float in this counter or Float.NaN if the counter
   *         does not contain any float values.
   */
  public float getSmallestFloat() {
    return !this.asc.isEmpty() ? this.asc.peek() : Float.NaN;
  }

  /**
   * Returns the elements associated with the smallest float in this counter.
   * 
   * @return The elements associated with the smallest float in this counter or
   *         null if the counter does not contain any float values.
   */
  public Set<T> getElementsWithSmallestFloat() {
    return !this.asc.isEmpty() ? this.elements.get(this.asc.peek()) : null;
  }

  // ==========================================================================

  /**
   * Returns the most common float in this counter.
   * 
   * @return The most common float in this counter or Float.NaN if the counter
   *         does not contain any float values.
   */
  public float getMostCommonFloat() {
    return !this.freqsDesc.isEmpty() ? this.freqsDesc.peek() : Float.NaN;
  }

  /**
   * Returns the elements associated with the most common float in this
   * counter.
   * 
   * @return The elements associated with the most common float in this counter
   *         or null if the counter does not contain any float values.
   */
  public Set<T> getElementsWithMostCommonFloat() {
    return !this.freqsDesc.isEmpty() ? this.elements.get(this.freqsDesc.peek())
        : null;
  }

  // ==========================================================================

  /**
   * Returns the least common float in this counter.
   * 
   * @return The least common float in this counter or Float.NaN if the counter
   *         does not contain any float values.
   */
  public float getLeastCommonFloat() {
    return !this.freqsAsc.isEmpty() ? this.freqsAsc.peek() : Float.NaN;
  }

  /**
   * Returns the elements associated with the least common float in this
   * counter.
   * 
   * @return The elements associated with the least common float in this
   *         counter or null if the counter does not contain any float values.
   */
  public Set<T> getElementsWithLeastCommonFloat() {
    return !this.freqsAsc.isEmpty() ? this.elements.get(this.freqsAsc.peek())
        : null;
  }
}
