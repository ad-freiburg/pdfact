package icecite.models;

import java.util.Collection;
import java.util.Set;

/**
 * A set of PDF elements.
 * 
 * @param <T>
 *        The type of the PDF elements.
 * 
 * @author Claudius Korzen
 */
public interface PdfElementSet<T extends PdfElement>
    extends Set<T>, HasBoundingBox {
  /**
   * Returns the most common height over all PDF elements in this set.
   * 
   * @return The most common height over all PDF elements in this set.
   */
  float getMostCommonHeight();

  /**
   * Returns the average height over all PDF elements in this set.
   * 
   * @return The average height over all PDF elements in this set.
   */
  float getAverageHeight();

  // ==========================================================================

  /**
   * Returns the most common width over all PDF elements in this set.
   * 
   * @return The most common width over all PDF elements in this set.
   */
  float getMostCommonWidth();

  /**
   * Returns the average width over all PDF elements in this set.
   * 
   * @return The average width over all PDF elements in this set.
   */
  float getAverageWidth();

  // ==========================================================================

  /**
   * Returns the most common minX value over all PDF elements in this set.
   * 
   * @return The most common minX value over all PDF elements in this set.
   */
  float getMostCommonMinX();

  /**
   * Returns the most common minY value over all PDF elements in this set.
   * 
   * @return The most common minY value over all PDF elements in this set.
   */
  float getMostCommonMinY();

  /**
   * Returns the most common maxX value over all PDF elements in this set.
   * 
   * @return The most common maxX value over all PDF elements in this set.
   */
  float getMostCommonMaxX();

  /**
   * Returns the most common maxY value over all PDF elements in this set.
   * 
   * @return The most common maxY value over all PDF elements in this set.
   */
  float getMostCommonMaxY();

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfElementSet}.
   * 
   * @param <T>
   *        The concrete type of the PDF elements in the set to create.
   * 
   * @author Claudius Korzen
   */
  public interface PdfElementSetFactory<T extends PdfElement> {
    /**
     * Creates a PdfElementSet.
     * 
     * @return An instance of {@link PdfElementSet}.
     */
    PdfElementSet<T> create();

    /**
     * Creates a PdfElementSet.
     * 
     * @param elements
     *        The elements of the set.
     * 
     * @return An instance of {@link PdfElementSet}.
     */
    PdfElementSet<T> create(Collection<T> elements);
  }
}
