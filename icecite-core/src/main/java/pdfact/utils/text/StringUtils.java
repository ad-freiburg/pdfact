package pdfact.utils.text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A collection of utility methods that deal with strings.
 * 
 * @author Claudius Korzen
 */
public class StringUtils {
  /**
   * Parses the given string to an integer. Returns 'defaultValue' if the input
   * can't be parsed to integer.
   * 
   * @param input
   *        The string to parse.
   * @param defaultValue
   *        The default value.
   * @return The integer that is represented by the given string or the default
   *         value if the string can't be parsed.
   */
  public static int parseInt(String input, int defaultValue) {
    try {
      return Integer.parseInt(input);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * Parses the given string to float. Returns 'defaultValue' if the input
   * can't be parsed to float.
   * 
   * @param input
   *        The string to parse.
   * @param defaultValue
   *        The default value.
   * @return The float that is represented by the given string or the default
   *         value if the string can't be parsed.
   */
  public static float parseFloat(String input, float defaultValue) {
    try {
      return Float.parseFloat(input);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  // ==========================================================================

  /**
   * Repeats the given string <repeats>-times.
   * 
   * @param string
   *        The string to repeat.
   * @param repeats
   *        The number of repeats.
   * @return The string containing the given string <repeats>-times.
   */
  public static String repeat(String string, int repeats) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < repeats; i++) {
      sb.append(string);
    }
    return sb.toString();
  }

  // ==========================================================================

  /**
   * Normalizes the given string, i.e. removes all non-alphanumeric characters
   * and transform all characters to lower cases. You can protect certain
   * non-alphanumeric characters from removing by specifying it as a protect.
   * 
   * @param text
   *        The text to normalize.
   * @param removeNumbers
   *        A boolean flag to indicate whether numbers should be removed.
   * @param removeWhitespaces
   *        A boolean flag to indicate whether white spaces should be removed.
   * @param toLowercases
   *        A boolean flag to indicate whether all letters should be
   *        transformed to lower cases.
   * @param protects
   *        The characters to protect from removing.
   * @return The normalized text.
   */
  public static String normalize(String text, boolean removeNumbers,
      boolean removeWhitespaces, boolean toLowercases, char... protects) {
    StringBuilder builder = new StringBuilder();

    HashSet<Character> protectSet = new HashSet<Character>();
    for (char protect : protects) {
      protectSet.add(protect);
    }

    boolean prevCharIsWhitespace = false;
    for (char c : text.toCharArray()) {
      if (protectSet.contains(c)) {
        builder.append(c);
        continue;
      }

      if (toLowercases) {
        c = Character.toLowerCase(c);
      }

      if (removeNumbers) {
        builder.append(Character.isLetter(c) ? c : "");
      } else {
        builder.append(Character.isLetterOrDigit(c) ? c : "");
      }

      if (!removeWhitespaces) {
        boolean charIsWhitespace = Character.isWhitespace(c);
        if (!prevCharIsWhitespace && charIsWhitespace) {
          builder.append(" ");
        }
        prevCharIsWhitespace = charIsWhitespace;
      }
    }
    return builder.toString().trim();
  }

  // ==========================================================================

  /**
   * Returns the first index within given haystack of the occurrence of one of
   * the specified needles.
   * 
   * @param haystack
   *        The text to search.
   * @param needles
   *        The patterns to find.
   *
   * @return The first found index.
   */
  public static int indexOf(String haystack, char... needles) {
    return indexOf(haystack, 0, needles);
  }

  /**
   * Returns the first index within given haystack of the occurrence of one of
   * the specified needles.
   * 
   * @param haystack
   *        The text to search.
   * @param fromIndex
   *        The index in the text where to start the search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The first found index.
   */
  public static int indexOf(String haystack, int fromIndex, char... needles) {
    if (haystack != null) {
      if (fromIndex < 0 || fromIndex >= haystack.length()) {
        return -1;
      }

      final char[] chars = haystack.toCharArray();
      for (int i = fromIndex; i < chars.length; i++) {
        for (char needle : needles) {
          if (chars[i] == needle) {
            return i;
          }
        }
      }
    }
    return -1;
  }

  /**
   * Returns the indexes within given haystack of all occurrences of the
   * specified needle.
   * 
   * @param haystack
   *        The text to search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The found indexes.
   */
  public static List<Integer> indexesOf(String haystack, char... needles) {
    List<Integer> indexes = new ArrayList<>();

    int index = indexOf(haystack, needles);

    while (index >= 0) {
      indexes.add(index);
      index = indexOf(haystack, index + 1, needles);
    }
    return indexes;
  }
}
