package icecite.models;

import java.util.List;
import java.util.Set;

/**
 * A list of PDF elements.
 * 
 * @param <T>
 *        The type of the PDF elements.
 * 
 * @author Claudius Korzen
 */
public interface PdfElementList<T extends PdfElement>
    extends List<T>, HasBoundingBox {
  /**
   * Returns the most common height over all PDF elements in this list.
   * 
   * @return The most common height over all PDF elements in this list.
   */
  float getMostCommonHeight();

  /**
   * Returns all elements with the most common height value.
   * 
   * @return All elements with the most common height value.
   */
  Set<T> getElementsWithMostCommonHeight();

  /**
   * Returns the average height over all PDF elements in this list.
   * 
   * @return The average height over all PDF elements in this list.
   */
  float getAverageHeight();

  // ==========================================================================

  /**
   * Returns the most common width over all PDF elements in this list.
   * 
   * @return The most common width over all PDF elements in this list.
   */
  float getMostCommonWidth();

  /**
   * Returns all elements with the most common width value.
   * 
   * @return All elements with the most common width value.
   */
  Set<T> getElementsWithMostCommonWidth();

  /**
   * Returns the average width over all PDF elements in this list.
   * 
   * @return The average width over all PDF elements in this list.
   */
  float getAverageWidth();

  // ==========================================================================

  /**
   * Returns the smallest minX value of all PDF elements in this list.
   * 
   * @return The smallest minX value of all PDF elements in this list.
   */
  float getSmallestMinX();

  /**
   * Returns all elements with the smallest minX value.
   * 
   * @return All elements with the smallest minX value.
   */
  Set<T> getElementsWithSmallestMinX();

  // ==========================================================================

  /**
   * Returns the smallest minY value of all PDF elements in this list.
   * 
   * @return The smallest minY value of all PDF elements in this list.
   */
  float getSmallestMinY();

  /**
   * Returns all elements with the smallest minY value.
   * 
   * @return All elements with the smallest minY value.
   */
  Set<T> getElementsWithSmallestMinY();

  // ==========================================================================

  /**
   * Returns the largest maxX value of all PDF elements in this list.
   * 
   * @return The largest maxX value of all PDF elements in this list.
   */
  float getLargestMaxX();

  /**
   * Returns all elements with the largest maxX value.
   * 
   * @return All elements with the largest maxX value.
   */
  Set<T> getElementsWithLargestMaxX();

  // ==========================================================================

  /**
   * Returns the largest maxY value of all PDF elements in this list.
   * 
   * @return The largest maxY value of all PDF elements in this list.
   */
  float getLargestMaxY();

  /**
   * Returns all elements with the largest maxY value.
   * 
   * @return All elements with the largest maxY value.
   */
  Set<T> getElementsWithLargestMaxY();

  // ==========================================================================

  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * @return An array of length 2, containing the both resulting views.
   */
  List<? extends PdfElementList<T>> split(int splitIndex);
  
  // ==========================================================================
  
  /**
   * The factory to create instances of {@link PdfElementList}.
   * 
   * @param <T>
   *        The concrete type of the PDF elements in the list to create.
   * 
   * @author Claudius Korzen
   */
  public interface PdfElementListFactory<T extends PdfElement> {
    /**
     * Creates a PdfElementList.
     * 
     * @return An instance of {@link PdfElementList}.
     */
    PdfElementList<T> create();

    /**
     * Creates a PdfElementList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return An instance of {@link PdfElementList}.
     */
    PdfElementList<T> create(int initialCapacity);
  }
}
