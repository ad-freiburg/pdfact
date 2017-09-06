package pdfact.core.util.counter;

import com.google.inject.assistedinject.Assisted;

/**
 * A counter to compute some statistics about float values.
 * 
 * @author Claudius Korzen
 */
public interface FloatCounter {
  /**
   * Adds the given float to this counter.
   * 
   * @param f
   *        The float to add.
   */
  void add(float f);

  /**
   * Adds the given float counter to this counter.
   * 
   * @param f
   *        The float to add.
   */
  void add(FloatCounter f);

  // ==========================================================================

  /**
   * Returns the most common float.
   * 
   * @return The most common float in this counter or Float.NaN if the counter
   *         is empty.
   */
  float getMostCommonFloat();

  /**
   * Returns the frequency of the most common float.
   * 
   * @return The frequency of the most common float in this counter.
   */
  float getMostCommonFloatFrequency();

  // ==========================================================================

  /**
   * Returns the average float.
   * 
   * @return The average value of the float values.
   */
  float getAverageFloat();

  // ==========================================================================

  /**
   * Returns the floats in this counter.
   * 
   * @return The floats in this counter.
   */
  float[] getFloats();

  /**
   * Returns the frequency of the given float in this counter.
   * 
   * @param value
   *        The float to process.
   * 
   * @return The frequency of the given float in this counter.
   */
  int getFrequency(float value);

  // ==========================================================================

  /**
   * Returns true if this counter is empty.
   * 
   * @return True if this counter is empty; false otherwise.
   */
  boolean isEmpty();

  // ==========================================================================

  /**
   * The factory to create instances of {@link FloatCounter}.
   * 
   * @author Claudius Korzen
   */
  public interface FloatCounterFactory {
    /**
     * Creates a new instance of {@link FloatCounter}.
     * 
     * @return A new instance of {@link FloatCounter}.
     */
    FloatCounter create();

    /**
     * Creates a new instance of {@link ObjectCounter}.
     * 
     * @param initialCapacity
     *        The initial capacity of this counter.
     * 
     * @return A new instance of {@link ObjectCounter}.
     */
    FloatCounter create(@Assisted int initialCapacity);
  }
}
