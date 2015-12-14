package utils.math;

/**
 * Math util.
 *
 * @author Claudius Korzen
 */
public class MathUtil {
  
  /**
   * Rounds the given number and returns a floating number with the given number
   * of decimal places.
   * 
   * @param number
   *          the number to round.
   * @param numDecimalPlaces
   *          the number of decimal places.
   * @return the rounded number.
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
   *          the number to ceil.
   * @param numDecimalPlaces
   *          the number of decimal places.
   * @return the ceiled number.
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
   *          the number to floor.
   * @param numDecimalPlaces
   *          the number of decimal places.
   * @return the floored number.
   */
  public static float floor(float number, int numDecimalPlaces) {
    if (numDecimalPlaces < 1) {
      return (float) Math.floor(number);
    }
    float decimal = numDecimalPlaces * 10;
    return (float) Math.floor(number * decimal) / decimal;
  }
  
  /**
   * Returns true, if the two given float number are almost equal. 
   * 
   * @param float1 the first float to compare.
   * @param float2 the second float to compare.
   * @return true, if the two given float number are almost equal. 
   */
  public static boolean isEqual(float float1, float float2) {
    return isEqual(float1, float2, 0.001f);
  }
  
  /**
   * Returns true, if the two given float number are almost equal 
   * (with respect to the given tolerance). 
   * 
   * @param f1 the first float to compare.
   * @param f2 the second float to compare.
   * @param tolerance the tolerance.
   * @return true, if the two given float number are almost equal. 
   */
  public static boolean isEqual(float f1, float f2, float tolerance) {
    return Math.abs(f1 - f2) <= tolerance;
  }
  
  /**
   * Returns true, if the first given float is larger (with respect to the given
   * tolerance) than the second given float. 
   * 
   * @param f1 the first float to compare.
   * @param f2 the second float to compare.
   * @param tolerance the tolerance.
   * @return true, if the first float is larger than the second float. 
   */
  public static boolean isLarger(float f1, float f2, float tolerance) {
    return f1 - f2 > tolerance;
  }
  
  /**
   * Returns true, if the first given float is smaller (with respect to the 
   * given tolerance) than the second given float. 
   * 
   * @param f1 the first float to compare.
   * @param f2 the second float to compare.
   * @param tolerance the tolerance.
   * @return true, if the first float is smaller than the second float.  
   */
  public static boolean isSmaller(float f1, float f2, float tolerance) {
    return f2 - f1 > tolerance;
  }
}
