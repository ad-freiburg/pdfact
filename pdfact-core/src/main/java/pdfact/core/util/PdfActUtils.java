package pdfact.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import gnu.trove.iterator.TCharIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TCharSet;
import pdfact.core.model.HasText;

/**
 * A collection of some common methods.
 * 
 * @author Claudius Korzen
 */
public class PdfActUtils {
  // ==========================================================================
  // Methods to join collections.

  /**
   * The default delimiter on joining collections.
   */
  protected static final String DEFAULT_DELIMITER = " ";

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the default delimiter.
   * 
   * @param objects
   *        The objects to join.
   *
   * @return A string containing the joined objects.
   */
  public static String join(Object[] objects) {
    return join(Arrays.asList(objects), DEFAULT_DELIMITER);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the default delimiter.
   * 
   * @param collection
   *        The collection to join.
   * 
   * @return A string containing the joined collection.
   */
  public static String join(Iterable<?> collection) {
    return join(collection, DEFAULT_DELIMITER);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the given delimiter.
   * 
   * @param objects
   *        The objects to join.
   * @param delimiter
   *        The delimiter to use on joining.
   * 
   * @return A string containing the joined objects.
   */
  public static String join(Object[] objects, String delimiter) {
    return join(Arrays.asList(objects), delimiter);
  }

  /**
   * Returns the string that results from joining the elements in the given
   * collection with the given delimiter.
   * 
   * @param iterable
   *        The iterable to join.
   * @param delimiter
   *        The delimiter to use on joining.
   * 
   * @return A string containing the joined objects.
   */
  public static String join(Iterable<?> iterable, String delimiter) {
    if (iterable == null) {
      return null;
    }

    String delim = delimiter != null ? delimiter : DEFAULT_DELIMITER;

    StringBuilder result = new StringBuilder();

    Iterator<?> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      Object element = iterator.next();
      result.append(element != null ? element.toString() : null);
      result.append(iterator.hasNext() ? delim : "");
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
   * 
   * @return The joined elements as a string.
   */
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

  // ==========================================================================
  // Some mathematical methods.

  /**
   * Rounds the given number and returns a floating number with the given number
   * of decimal places.
   * 
   * @param number
   *        The number to round.
   * @param numDecimalPlaces
   *        The number of decimal places.
   * 
   * @return The rounded number.
   */
  public static float round(float number, int numDecimalPlaces) {
    if (numDecimalPlaces < 1) {
      return Math.round(number);
    }
    float decimal = numDecimalPlaces * 10;
    return Math.round(number * decimal) / decimal;
  }

  /**
   * Ceils the given number and returns a floating number with the given number
   * of decimal places.
   * 
   * @param number
   *        The number to ceil.
   * @param numDecimalPlaces
   *        The number of decimal places.
   * 
   * @return The ceiled number.
   */
  public static float ceil(float number, int numDecimalPlaces) {
    if (numDecimalPlaces < 1) {
      return (float) Math.ceil(number);
    }
    float decimal = numDecimalPlaces * 10;
    return (float) Math.ceil(number * decimal) / decimal;
  }

  /**
   * Floors the given number and returns a floating number with the given number
   * of decimal places.
   * 
   * @param number
   *        The number to floor.
   * @param numDecimalPlaces
   *        The number of decimal places.
   * 
   * @return The floored number.
   */
  public static float floor(float number, int numDecimalPlaces) {
    if (numDecimalPlaces < 1) {
      return (float) Math.floor(number);
    }
    float decimal = numDecimalPlaces * 10;
    return (float) Math.floor(number * decimal) / decimal;
  }

  /**
   * Returns true, if the two given float numbers are equal, with respect to a
   * tolerance of 0.001.
   * 
   * @param float1
   *        The first float to compare.
   * @param float2
   *        The second float to compare.
   * 
   * @return True, if the two given float number are almost equal, with respect
   *         to a tolerance of 0.001.
   */
  public static boolean isEqual(float float1, float float2) {
    return isEqual(float1, float2, 0.001f);
  }

  /**
   * Returns true, if the two given float number are almost equal (with respect
   * to the given tolerance).
   * 
   * @param f1
   *        The first float to compare.
   * @param f2
   *        The second float to compare.
   * @param tolerance
   *        The tolerance.
   *
   * @return True, if the two given float number are almost equal.
   */
  public static boolean isEqual(float f1, float f2, float tolerance) {
    return Math.abs(f1 - f2) <= tolerance;
  }

  /**
   * Returns true, if the first given float is larger (with respect to the given
   * tolerance) than the second given float.
   * 
   * @param f1
   *        The first float to compare.
   * @param f2
   *        The second float to compare.
   * @param tolerance
   *        The tolerance.
   * 
   * @return True, if the first float is larger than the second float.
   */
  public static boolean isLarger(float f1, float f2, float tolerance) {
    return f1 - f2 > tolerance;
  }

  /**
   * Returns true, if the first given float is larger than or equal to the
   * second given float (with respect to the given tolerance).
   * 
   * @param f1
   *        The first float to compare.
   * @param f2
   *        The second float to compare.
   * @param tolerance
   *        The tolerance.
   * 
   * @return True, if the first float is larger than the second float.
   */
  public static boolean isLargerOrEqual(float f1, float f2, float tolerance) {
    return isLarger(f1, f2, tolerance) || isEqual(f1, f2, tolerance);
  }

  /**
   * Returns true, if the first given float is smaller (with respect to the
   * given tolerance) than the second given float.
   * 
   * @param f1
   *        The first float to compare.
   * @param f2
   *        The second float to compare.
   * @param tolerance
   *        The tolerance.
   * 
   * @return True, if the first float is smaller than the second float.
   */
  public static boolean isSmaller(float f1, float f2, float tolerance) {
    return f2 - f1 > tolerance;
  }

  /**
   * Returns true, if the first given float is smaller than or equal to the
   * second given float (with respect to the given tolerance).
   * 
   * @param f1
   *        The first float to compare.
   * @param f2
   *        The second float to compare.
   * @param tolerance
   *        The tolerance.
   * 
   * @return True, if the first float is smaller than the second float.
   */
  public static boolean isSmallerOrEqual(float f1, float f2, float tolerance) {
    return isSmaller(f1, f2, tolerance) || isEqual(f1, f2, tolerance);
  }

  // ==========================================================================
  // Some path methods.

  /**
   * Returns the base name of the given file, that is the filename without the
   * file-extension.
   * 
   * @param path
   *        The path to process.
   * @return The base name of the path.
   */
  public static String getBasename(String path) {
    return getBasename(Paths.get(path));
  }

  /**
   * Returns the basename of the given file, that is the filename without the
   * file-extension.
   * 
   * @param file
   *        The file to process.
   * @return The base name of the path.
   */
  public static String getBasename(Path file) {
    if (file != null) {
      Path filename = file.getFileName();
      if (filename != null) {
        String filenameStr = filename.toString();
        String[] tokens = filenameStr.split("\\.(?=[^\\.]+$)");
        return tokens[0];
      }
    }
    return null;
  }

  /**
   * Returns the file extension of the given file.
   * 
   * @param file
   *        The file to process.
   * @return The file extension of the file.
   */
  public static String getExtension(Path file) {
    if (file != null) {
      Path filename = file.getFileName();
      if (filename != null) {
        String filenameStr = filename.toString();
        String[] tokens = filenameStr.split("\\.(?=[^\\.]+$)");
        return tokens.length > 1 ? tokens[1] : "";
      }
    }
    return null;
  }

  /**
   * Reads the given directory non-recursively and returns a map with the names
   * and the streams of all files in this directory. Works also in jar files.
   * 
   * @param path
   *        The path to the directory to read.
   * @return A map with the names and the streams of all files in this directory
   * @throws IOException
   *         If reading the directory failed.
   */
  public static Map<String, InputStream> readDirectory(String path)
      throws IOException {
    Map<String, InputStream> streams = new HashMap<>();
    ClassLoader classLoader = PdfActUtils.class.getClassLoader();
    ProtectionDomain domain = PdfActUtils.class.getProtectionDomain();

    // We need to distinguish, if we are in a jar file or not.
    CodeSource codeSource = domain.getCodeSource();
    Path jarFile = Paths.get(codeSource.getLocation().getPath());

    // Check, if we are in jar file.
    if (Files.isRegularFile(jarFile)) {
      try (JarFile jar = new JarFile(jarFile.toFile())) {
        // Fetch all files in the jar.
        final Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
          final String name = entries.nextElement().getName();
          // filter according to the path

          if (name.startsWith(path) && !name.equals(path)) {
            streams.put(name, classLoader.getResourceAsStream(name));
          }
        }
      }
    } else {
      // We are not in a jar file.
      File directory = null;
      try {
        // Read the directory.
        directory = new File(classLoader.getResource(path).toURI());
      } catch (Exception e) {
        // Nothing to do.
      }

      if (directory != null) {
        File[] files = directory.listFiles();
        if (files != null) {
          for (File file : files) {
            try {
              streams.put(file.getName(), new FileInputStream(file));
            } catch (Exception e) {
              continue;
            }
          }
        }
      }
    }
    return streams;
  }

  // ==========================================================================

  /**
   * Returns the first index within the given haystack of the occurrence of one
   * of the specified needles.
   * 
   * @param haystack
   *        The text to search.
   * @param needles
   *        The patterns to find.
   *
   * @return The first found index.
   */
  public static int indexOf(String haystack, TCharSet... needles) {
    return indexOf(haystack, 0, needles);
  }

  /**
   * Returns the first index within the given haystack of the occurrence of one
   * of the specified needles.
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
   * Returns the first index within the given haystack of the occurrence of one
   * of the specified needles.
   * 
   * @param haystack
   *        The text to search.
   * @param from
   *        The index in the text where to start the search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The first found index.
   */
  public static int indexOf(String haystack, int from, TCharSet... needles) {
    if (haystack != null) {
      if (from < 0 || from >= haystack.length()) {
        return -1;
      }

      final char[] chars = haystack.toCharArray();
      for (int i = from; i < chars.length; i++) {
        for (TCharSet needle : needles) {
          TCharIterator itr = needle.iterator();
          while (itr.hasNext()) {
            if (chars[i] == itr.next()) {
              return i;
            }
          }
        }
      }
    }
    return -1;
  }

  /**
   * Returns the first index within given haystack of the occurrence of one of
   * the specified needles.
   * 
   * @param haystack
   *        The text to search.
   * @param from
   *        The index in the text where to start the search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The first found index.
   */
  public static int indexOf(String haystack, int from, char... needles) {
    if (haystack != null) {
      if (from < 0 || from >= haystack.length()) {
        return -1;
      }

      final char[] chars = haystack.toCharArray();
      for (int i = from; i < chars.length; i++) {
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
   * Returns the indexes within the given haystack of all occurrences of the
   * given needles.
   * 
   * @param haystack
   *        The text to search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The found indexes.
   */
  public static TIntList indexesOf(String haystack, TCharSet... needles) {
    TIntList indexes = new TIntArrayList();

    int index = indexOf(haystack, needles);

    while (index >= 0) {
      indexes.add(index);
      index = indexOf(haystack, index + 1, needles);
    }

    return indexes;
  }

  /**
   * Returns the indexes within the given haystack of all occurrences of the
   * given needles.
   * 
   * @param haystack
   *        The text to search.
   * @param needles
   *        The patterns to find.
   * 
   * @return The found indexes.
   */
  public static TIntList indexesOf(String haystack, char... needles) {
    TIntList indexes = new TIntArrayList();

    int index = indexOf(haystack, needles);

    while (index >= 0) {
      indexes.add(index);
      index = indexOf(haystack, index + 1, needles);
    }

    return indexes;
  }

  // ==========================================================================

  /**
   * Returns true, if the given haystack contains at least one of the given
   * needles.
   * 
   * @param haystack
   *        The collection to search.
   * @param needles
   *        The patterns to find.
   *
   * @return True, if the given haystack contains at least one of the given
   *         needles; False otherwise.
   */
  public static boolean containsOneOf(Collection<?> haystack,
      Object... needles) {
    if (haystack == null) {
      return false;
    }

    for (Object obj : haystack) {
      if (obj == null) {
        continue;
      }
      for (Object needle : needles) {
        if (obj.equals(needle)) {
          return true;
        }
      }
    }
    return false;
  }
}
