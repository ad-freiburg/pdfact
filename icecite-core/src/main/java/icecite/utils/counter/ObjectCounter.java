package icecite.utils.counter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A counter to compute some statistics about a collection of objects.
 * 
 * @param <S>
 *        The type of the keys.
 * @param <T>
 *        The type of the values.
 * 
 * @author Claudius Korzen
 */
public class ObjectCounter<S, T> {
  /**
   * The default initial capacity of this counter.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * The elements, grouped by the individual objects.
   */
  protected Map<S, Set<T>> elements;

  /**
   * A queue that holds the objects by frequency in ascending order.
   */
  protected ObjectPriorityQueue<S> freqsAsc;

  /**
   * A queue that holds the objects by frequency in descending order.
   */
  protected ObjectPriorityQueue<S> freqsDesc;

  /**
   * The number of elements in this counter.
   */
  protected int numElements;
  
  // ==========================================================================
  // Constructors.

  /**
   * Creates a new ObjectCounter with the default initial capacity.
   */
  public ObjectCounter() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new FloatCounter with the given initial capacity.
   * 
   * @param initialCapacity
   *        The initial capacity.
   */
  public ObjectCounter(int initialCapacity) {
    this.elements = new HashMap<>(initialCapacity);
    this.freqsAsc = new ObjectPriorityQueue<>(initialCapacity);
    this.freqsDesc = new ObjectPriorityQueue<>(initialCapacity, true);
  }

  // ==========================================================================
  // Public methods.

  /**
   * Adds the given object associated with the given element to this counter.
   * 
   * @param s
   *        The object to add.
   * @param element
   *        The associated element.
   */
  public void add(S s, T element) {
    if (!this.elements.containsKey(s)) {
      // Create a new bucket.
      this.elements.put(s, new HashSet<>());
    }

    // Extend the existing bucket.
    Set<T> bucket = this.elements.get(s);
    boolean added = bucket.add(element);
    
    if (added) {
      // Update the frequencies.
      float freq = this.freqsAsc.getPriority(s, 0);
      this.freqsAsc.updatePriority(s, freq + 1);
      this.freqsDesc.updatePriority(s, freq + 1);
      // Increment elements counter.
      this.numElements++;
    }
  }

  /**
   * Removes the given element with the given float from this counter.
   * 
   * @param s
   *        The object to remove.
   * @param element
   *        The element to remove.
   */
  public void remove(S s, Object element) {
    if (!this.elements.containsKey(s)) {
      return;
    }

    Set<T> set = this.elements.get(s);
    if (set == null || set.isEmpty()) {
      return;
    }
    boolean removed = set.remove(element);
    
    if (removed) {
      // Update the frequencies.
      float freq = this.freqsAsc.getPriority(s, 0);
      if (freq == 1) {
        this.freqsAsc.remove(s);
        this.freqsDesc.remove(s);
      } else if (freq > 1) {
        this.freqsAsc.updatePriority(s, freq - 1);
        this.freqsDesc.updatePriority(s, freq - 1);
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
    this.freqsAsc.clear();
    this.freqsDesc.clear();
    this.numElements = 0;
  }
  
  // ==========================================================================

  /**
   * Returns the most common object in this counter.
   * 
   * @return The most common object in this counter or null if the counter
   *         does not contain any objects.
   */
  public S getMostCommonObject() {
    return !this.freqsDesc.isEmpty() ? this.freqsDesc.peek() : null;
  }

  /**
   * Returns the elements associated with the most common object in this
   * counter.
   * 
   * @return The elements associated with the most common object in this counter
   *         or null if the counter does not contain any objects.
   */
  public Set<T> getElementsWithMostCommonObject() {
    return !this.freqsDesc.isEmpty() ? this.elements.get(this.freqsDesc.peek())
        : null;
  }

  // ==========================================================================

  /**
   * Returns the least common object in this counter.
   * 
   * @return The least common object in this counter or null if the counter
   *         does not contain any objects.
   */
  public S getLeastCommonObject() {
    return !this.freqsAsc.isEmpty() ? this.freqsAsc.peek() : null;
  }

  /**
   * Returns the elements associated with the least common objects in this
   * counter.
   * 
   * @return The elements associated with the least common object in this
   *         counter or null if the counter does not contain any objects.
   */
  public Set<T> getElementsWithLeastCommonObject() {
    return !this.freqsAsc.isEmpty() ? this.elements.get(this.freqsAsc.peek())
        : null;
  }
}

