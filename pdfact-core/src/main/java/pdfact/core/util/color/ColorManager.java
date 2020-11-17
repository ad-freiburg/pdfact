package pdfact.core.util.color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import pdfact.core.model.Color;

/**
 * A converter that converts PDColor objects to {@link Color} objects.
 * 
 * @author Claudius Korzen
 */
public class ColorManager {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(ColorManager.class);

  /**
   * A map of the already known colors per name.
   */
  protected static Map<String, Color> knownColors = new HashMap<>();

  // ==============================================================================================

  /**
   * Converts the given PDColor object to a related {@link Color} object.
   * 
   * @param color      The color to convert.
   * @param colorSpace The color space.
   * 
   * @return The converted color.
   */
  public static Color getColor(PDColor color, PDColorSpace colorSpace) {
    return getColor(toRGB(color, colorSpace));
  }

  /**
   * Converts the given RGB array to a related {@link Color} object.
   * 
   * @param rgb The RGB array to convert.
   * 
   * @return The converted color.
   */
  public static Color getColor(int[] rgb) {
    if (rgb == null) {
      return null;
    }

    // Check if the color is already known.
    Color knownColor = getKnownColor(rgb);
    if (knownColor != null) {
      return knownColor;
    }

    // The color is not known. Create a new color.
    Color newColor = new Color();
    newColor.setId("color-" + knownColors.size());
    newColor.setName(computeColorName(rgb));
    newColor.setRGB(rgb);

    // Add the new color to the map of known colors.
    knownColors.put(newColor.getName(), newColor);
    log.debug("A new color was registered: " + newColor);

    return newColor;
  }

  // ==============================================================================================

  /**
   * Checks if the given color is an already known color.
   * 
   * @param rgb The color to check.
   *
   * @return True, if the given color is a known color; false otherwise.
   */
  protected static boolean isKnownColor(int[] rgb) {
    return getKnownColor(rgb) != null;
  }

  /**
   * Returns a {@link Color} object related to the given color if the color is already known; null
   * otherwise.
   * 
   * @param color The color to check.
   *
   * @return A {@link Color} object related to the given color if the color is already known; null
   *         otherwise.
   */
  protected static Color getKnownColor(int[] rgb) {
    if (rgb == null) {
      return null;
    }
    return knownColors.get(computeColorName(rgb));
  }

  /**
   * Computes a name for the given color.
   * 
   * @param rgb The color to process.
   * 
   * @return A name for the given color.
   */
  protected static String computeColorName(int[] rgb) {
    return Arrays.toString(rgb);
  }

  // ==============================================================================================

  /**
   * Translates the the given PDF color to an RGB-array.
   * 
   * @param color      The color to process.
   * @param colorSpace The color space of the color to process.
   * 
   * @return An array of length 3, containing the R, G and B values.
   */
  protected static int[] toRGB(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }

    int[] rgb = {0, 0, 0};

    try {
      float[] x = colorSpace.toRGB(color.getComponents());
      rgb[0] = (int) (x[0] * 255);
      rgb[1] = (int) (x[1] * 255);
      rgb[2] = (int) (x[2] * 255);
      return rgb;
    } catch (Exception e) {
      return rgb;
    }
  }
}
