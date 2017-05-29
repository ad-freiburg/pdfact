package icecite.utils.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import icecite.models.HasText;

/**
 * A collection of utility methods that deal with collections.
 * 
 * @author Claudius Korzen
 */
public class CollectionUtils {
  /**
   * The default delimiter on joining collections.
   */
  protected static final String DEFAULT_DELIMITER = " ";

  /**
   * Returns true if the given collection is null or empty.
   * 
   * @param c
   *        The collection to check.
   * @return True if the given collection is null or empty.
   */
  public static boolean isNullOrEmpty(Collection<?> c) {
    return c == null || c.isEmpty();
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the whitespace (" ").
   * 
   * @param objects
   *        The objects to join.
   * @return A string containing the joined objects.
   */
  public static String join(Object[] objects) {
    return join(Arrays.asList(objects), DEFAULT_DELIMITER);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the given delimiter.
   * 
   * @param objects
   *        The objects to join.
   * @param delimiter
   *        The delimiter to use on joining.
   * @return A string containing the joined objects.
   */
  public static String join(Object[] objects, String delimiter) {
    return join(Arrays.asList(objects), delimiter);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the whitespace (" ").
   * 
   * @param collection
   *        The collection to join.
   * @return A string containing the joined collection.
   */
  public static String join(Iterable<?> collection) {
    return join(collection, DEFAULT_DELIMITER);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the given delimiter.
   * 
   * @param iterable
   *        The iterable to join.
   * @param delimiter
   *        The delimiter to use on joining.
   * @return A string containing the joined objects.
   */
  public static String join(Iterable<?> iterable, String delimiter) {
    if (iterable == null) {
      return null;
    }

    if (delimiter == null) {
      delimiter = DEFAULT_DELIMITER;
    }

    StringBuilder result = new StringBuilder();

    Iterator<?> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      Object element = iterator.next();
      result.append(element != null ? element.toString() : null);
      result.append(iterator.hasNext() ? delimiter : "");
    }

    return result.toString();
  }

  /**
   * Joins the given text elements.
   * 
   * @param elements
   *        The text elements to join.
   * @param delim
   *        The delimiter to use on joining.
   * @return The joined elements as a string.
   */
  // TODO: Move it to another util?
  public static String join(List<? extends HasText> elements, String delim) {
    if (elements == null) {
      return null;
    }

    StringBuilder result = new StringBuilder();

    Iterator<? extends HasText> iterator = elements.iterator();
    while (iterator.hasNext()) {
      HasText element = iterator.next();
      result.append(element != null ? element.getText() : null);
      result.append(iterator.hasNext() ? delim : "");
    }

    return result.toString();
  }
}
