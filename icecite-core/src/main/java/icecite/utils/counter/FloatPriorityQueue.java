package icecite.utils.counter;

import gnu.trove.impl.Constants;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.map.hash.TFloatIntHashMap;

/**
 * A priority queue of primitive float values, where the elements are ordered
 * by given priorities.
 * 
 * @author Claudius Korzen
 */
public class FloatPriorityQueue {
  /**
   * The default initial capacity of this queue.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * The default flag that indicates whether this queue is max-based or not.
   */
  protected static final boolean DEFAULT_IS_MAX_BASED = false;

  /**
   * The default value that indicates a null value in this.indexes (see below).
   */
  protected static final int DEFAULT_NO_INDEX = -1;

  /**
   * The values (given as floats) of this queue.
   */
  protected TFloatArrayList floats;

  /**
   * The priorities of the values in this queue.
   */
  protected TFloatArrayList priorities;

  /**
   * The frequencies of the values in this queue.
   */
  protected TIntArrayList frequencies;

  /**
   * Maps the values of this queue to their positions in the floats array.
   */
  protected TFloatIntMap indexes;

  /**
   * A boolean flag that indicates whether this queue is max-based or not.
   */
  protected boolean maxBased;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new priority queue with the default initial capacity.
   */
  public FloatPriorityQueue() {
    this(DEFAULT_IS_MAX_BASED);
  }

  /**
   * Creates a new priority queue with the default initial capacity.
   * 
   * @param maxBased
   *        A boolean to define whether this queue should be max-based.
   */
  public FloatPriorityQueue(boolean maxBased) {
    this(DEFAULT_INITIAL_CAPACITY, maxBased);
  }

  /**
   * Creates a new priority queue with the given initial capacity.
   * 
   * @param initialCapacity
   *        The initial capacity of this queue.
   */
  public FloatPriorityQueue(int initialCapacity) {
    this(initialCapacity, DEFAULT_IS_MAX_BASED);
  }

  /**
   * Creates a new priority queue with the given initial capacity.
   * 
   * @param capacity
   *        The initial capacity of this queue.
   * @param maxBased
   *        A boolean to define whether this queue should be max-based.
   */
  public FloatPriorityQueue(int capacity, boolean maxBased) {
    this.floats = new TFloatArrayList(capacity);
    this.frequencies = new TIntArrayList(capacity);
    this.priorities = new TFloatArrayList(capacity);
    this.indexes = new TFloatIntHashMap(capacity, Constants.DEFAULT_LOAD_FACTOR,
        DEFAULT_NO_INDEX, DEFAULT_NO_INDEX);
    this.maxBased = maxBased;
  }

  // ==========================================================================

  /**
   * Inserts a new float with the given priority to this queue.
   * 
   * @param f
   *        The float to insert.
   * @param priority
   *        The priority of the float.
   */
  public void insert(float f, float priority) {
    int index = indexOf(f);

    // Check if the queue already contains the given float.
    if (index != DEFAULT_NO_INDEX) {
      // The queue already contains the given float.
      // Increment its frequency and update the priority.
      this.frequencies.set(index, this.frequencies.get(index) + 1);
      updatePriority(f, priority);
      return;
    }

    // The queue does not contain the given float yet. Insert it.
    this.floats.add(f);
    this.priorities.add(this.maxBased ? -priority : priority);
    this.frequencies.add(1);
    this.indexes.put(f, this.floats.size() - 1);
    repairHeapUpwards(this.floats.size() - 1);
  }

  /**
   * Updates the priority of the given float.
   * 
   * @param f
   *        The float to update.
   * @param priority
   *        The new priority.
   */
  public void updatePriority(float f, float priority) {
    int index = indexOf(f);

    // Check if the queue already contains the given float.
    if (index == DEFAULT_NO_INDEX) {
      // The queue does not contain the given float yet. Insert it.
      insert(f, priority);
      return;
    }

    // The queue already contains the given float. Update the priority.
    float oldPriority = this.priorities.get(index);
    float newPriority = this.maxBased ? -priority : priority;

    this.priorities.set(index, newPriority);

    // Check, if the new priority is larger or smaller than the old one and
    // repair the heap accordingly.
    // There is nothing to do, if newPriority == oldPriority.
    if (oldPriority > newPriority) {
      repairHeapUpwards(index);
    } else if (oldPriority < newPriority) {
      repairHeapDownwards(index);
    }
  }

  /**
   * Removes the given float from this queue.
   * 
   * @param f
   *        The float to remove.
   */
  public void remove(float f) {
    int index = indexOf(f);
    if (index != DEFAULT_NO_INDEX) {
      // Instead of removing the float from the heap, decrement its frequency.
      int newFrequency = Math.max(0, this.frequencies.get(index) - 1);
      this.frequencies.set(index, newFrequency);
    }
  }

  // ==========================================================================
  
  /**
   * Clears this queue.
   */
  public void clear() {
    this.floats.clear();
    this.priorities.clear();
    this.frequencies.clear();
    this.indexes.clear();
  }
  
  // ==========================================================================

  /**
   * Retrieves, but does not remove, the head of this queue. Returns Float.NaN
   * if this queue is empty.
   * 
   * @return The head of this queue.
   */
  public float peek() {
    deleteFloatsWithNoFrequency();
    return !isEmpty() ? this.floats.get(0) : Float.NaN;
  }
  
  /**
   * Retrieves and removes the head of this queue. Returns Float.NaN if this
   * queue is empty.
   * 
   * @return The head of this queue.
   */
  public float poll() {
    return poll(true);
  }

  /**
   * Retrieves and removes the head of this queue. Returns Float.NaN if this
   * queue is empty.
   * 
   * @param deleteFloatsWithNoFrequency
   *        A flag that indicates whether all values with frequency 0 should be
   *        removed from the head in advance (this flag was needed to avoid an
   *        infinite loop on calling this method in the method
   *        deleteFloatsWithNoFrequency()).
   * @return The head of this queue.
   */
  protected float poll(boolean deleteFloatsWithNoFrequency) {
    if (deleteFloatsWithNoFrequency) {
      // Remove all values with frequency 0.
      deleteFloatsWithNoFrequency();
    }

    if (!isEmpty()) {
      int endIndex = this.floats.size() - 1;
      // Swap the minimum to the "end" of the heap.
      swap(0, endIndex);
      // Delete the minimum.
      float f = this.floats.removeAt(endIndex);
      this.priorities.removeAt(endIndex);
      this.frequencies.removeAt(endIndex);
      this.indexes.remove(f);
      repairHeapDownwards(0);
      return f;
    }
    return Float.NaN;
  }

  // ==========================================================================

  /**
   * Returns the number of elements in this queue.
   * 
   * @return The number of elements in this queue.
   */
  public int size() {
    return this.floats.size();
  }

  /**
   * Returns true if this queue is empty.
   * 
   * @return True if this queue is empty; False otherwise.
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns true, if this queue contains the given float.
   * 
   * @param f
   *        The float to check.
   * @return True, if this queue contains the given float, False otherwise.
   */
  public boolean containsFloat(float f) {
    return this.indexes.get(f) != DEFAULT_NO_INDEX;
  }

  /**
   * Returns the frequency of the given float in this queue.
   * 
   * @param f
   *        The float to check.
   * @return The frequency of the given float in this queue.
   */
  public int getFrequency(float f) {
    int index = this.indexes.get(f);
    if (index != DEFAULT_NO_INDEX) {
      return this.frequencies.get(index);
    }
    return 0;
  }

  /**
   * Returns the index of the given float in the heap array.
   * 
   * @param f
   *        The float to process.
   * @return The index of the given float in the heap array.
   */
  public int indexOf(float f) {
    return this.indexes.get(f);
  }

  /**
   * Returns the priority of the given float or the given default value if this
   * queue does not contain the given float.
   * 
   * @param f
   *        The float to check.
   * @param defaultPriority
   *        The default value to return if this queue does not contain the
   *        given float.
   * @return The priority of the given float.
   */
  public float getPriority(float f, int defaultPriority) {
    int index = indexOf(f);
    if (index != DEFAULT_NO_INDEX) {
      return this.priorities.get(index);
    }
    return defaultPriority;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return this.floats.toString();
  }

  // ==========================================================================

  /**
   * Deletes all floats from the head of this queue, which have frequency 0.
   */
  protected void deleteFloatsWithNoFrequency() {
    // Ignore all floats marked as removed.
    while (!this.frequencies.isEmpty() && this.frequencies.get(0) == 0) {
      poll(false);
    }
  }

  /**
   * Repair the heap from the given array position downwards.
   * 
   * @param i
   *        The array position where to start the repairing.
   */
  protected void repairHeapDownwards(int i) {
    // The position of the smaller child of element.
    int childPos;

    // Iterate over heap until there are still child-nodes for element at
    // position i.
    while ((childPos = (2 * i) + 1) < size()) {
      // Check, if there is a right-hand child.
      if (childPos != size() - 1) {
        // Adjust minChildPos, if the right-hand child is smaller than the
        // right-hand child.
        if (this.priorities.get(childPos + 1) < this.priorities.get(childPos)) {
          childPos++;
        }
      }

      // Compare element heap[i] with its smaller child.
      if (this.priorities.get(i) > this.priorities.get(childPos)) {
        // Element at pos i is larger than its smallest child and hence, the
        // heap-propery doesn't hold. So swap both elements.
        swap(i, childPos);

        // Hence the indices of elements have changed, update i.
        i = childPos;
      } else {
        // Element array[i] is smaller than its smallest child.
        // There is nothing to do anymore, because the heap-property holds.
        break;
      }
    }
  }

  /**
   * Repair the heap from the given array position upwards.
   *
   * @param i
   *        The array position where to start the repairing.
   */
  protected void repairHeapUpwards(int i) {
    // The position of the parent element.
    int parentPos;

    // Iterate over heap until there is a parent-node for element at pos i.
    while ((parentPos = (int) (Math.ceil((i / 2d)) - 1)) >= 0) {
      // Compare element heap[i] with its parent node.
      if (this.priorities.get(i) < this.priorities.get(parentPos)) {
        // Heap property doesn't hold. Swap.
        swap(i, parentPos);
        i = parentPos;
      } else {
        break;
      }
    }
  }

  /**
   * Swaps two floats in the heap.
   * 
   * @param pos1
   *        The position of the first float.
   * @param pos2
   *        The position of the second float.
   */
  protected void swap(int pos1, int pos2) {
    float f1 = this.floats.get(pos1);
    float f2 = this.floats.get(pos2);
    this.floats.set(pos2, f1);
    this.floats.set(pos1, f2);
    
    float priority1 = this.priorities.get(pos1);
    float priority2 = this.priorities.get(pos2);
    this.priorities.set(pos2, priority1);
    this.priorities.set(pos1, priority2);
    
    int freq1 = this.frequencies.get(pos1);
    int freq2 = this.frequencies.get(pos2);
    this.frequencies.set(pos2, freq1);
    this.frequencies.set(pos1, freq2);
    
    this.indexes.put(f1, pos2);
    this.indexes.put(f2, pos1);
  }
}
