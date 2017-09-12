package pdfact.core.util.counter;

import gnu.trove.iterator.TObjectIntIterator;

/**
 * A counter to compute some statistics about objects.
 * 
 * @param <T>
 *        The type of the objects to count.
 * 
 * @author Claudius Korzen
 */
public interface ObjectCounter<T> {
  /**
   * Adds the given object to this counter.
   * 
   * @param o
   *        The object to add.
   */
  void add(T o);

  /**
   * Adds the given ObjectCounter to this counter.
   * 
   * @param o
   *        The object to add.
   */
  void add(ObjectCounter<T> o);

  // ==========================================================================

  /**
   * Returns the most common object.
   * 
   * @return The most common object in this counter or null if the counter is
   *         empty.
   */
  T getMostCommonObject();

  /**
   * Returns the frequency of the most common object.
   * 
   * @return The frequency of the most common object in this counter.
   */
  int getMostCommonObjectFrequency();

  // ==========================================================================

  /**
   * Returns the objects in this counter.
   * 
   * @return The objects in this counter.
   */
  Object[] getObjects();

  /**
   * Returns the frequency of the given object in this counter.
   * 
   * @param object
   *        The object to process.
   * 
   * @return The frequency of the given object in this counter.
   */
  int getFrequency(T object);

  // ==========================================================================

  /**
   * Returns an iterator overt his counter.
   * 
   * @return An iterator overt his counter.
   */
  TObjectIntIterator<T> iterator();

  // ==========================================================================

  /**
   * Returns true if this counter is empty.
   * 
   * @return True if this counter is empty; false otherwise.
   */
  boolean isEmpty();

  /**
   * Returns the number of unique objects in this counter.
   * 
   * @return The number of unique objects in this counter.
   */
  int size();

  // ==========================================================================

  /**
   * The factory to create instances of {@link ObjectCounter}.
   * 
   * @author Claudius Korzen
   * 
   * @param <T>
   *        The type of the objects to count.
   */
  public interface ObjectCounterFactory<T> {
    /**
     * Creates a new instance of {@link ObjectCounter}.
     * 
     * @return A new instance of {@link ObjectCounter}.
     */
    ObjectCounter<T> create();

    /**
     * Creates a new instance of {@link ObjectCounter}.
     * 
     * @param initialCapacity
     *        The initial capacity of this counter.
     * 
     * @return A new instance of {@link ObjectCounter}.
     */
    ObjectCounter<T> create(int initialCapacity);
  }
}
