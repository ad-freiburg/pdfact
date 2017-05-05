package icecite.utils.text;

/**
 * A collection of utility methods that deal with strings.
 * 
 * @author Claudius Korzen
 */
public class StringUtils {
  /**
   * Parses the given string to int. Returns 'defaultValue' if the input can't
   * be parsed to int.
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
}
