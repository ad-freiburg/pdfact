package icecite.utils.color;

/**
 * A collection of utility methods that deal with colors.
 * 
 * @author Claudius Korzen
 */
public class ColorUtils {
  /**
   * Transforms the given packed RGB value into an array of three values in
   * range [0,255] representing the R, G and B values.
   * 
   * @param pixel
   *        The packed RGB value.
   * @return The RGB values in an array of three values.
   */
  public static float[] toRGBArray(int pixel) {
    float alpha = (pixel >> 24) & 0xff;
    float red = ((pixel >> 16) & 0xff) / 255f;
    float green = ((pixel >> 8) & 0xff) / 255f;
    float blue = ((pixel) & 0xff) / 255f;
    return new float[] { red, green, blue, alpha };
  }
}
