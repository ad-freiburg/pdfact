package pdfact.core.pipes.parse.stream.pdfbox.convert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import com.google.inject.Inject;

import pdfact.core.model.Color;
import pdfact.core.model.Color.ColorFactory;
import pdfact.core.util.log.InjectLogger;

/**
 * A converter that converts PDColor objects to {@link Color} objects.
 * 
 * @author Claudius Korzen
 */
public class PDColorConverter {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create instances of {@link Color}.
   */
  protected ColorFactory colorFactory;

  /**
   * A map of the already known colors per name.
   */
  protected Map<String, Color> knownColors;

  /**
   * Creates a new color converter.
   * 
   * @param colorFactory
   *        The factory to create instances of {@link Color}.
   */
  @Inject
  public PDColorConverter(ColorFactory colorFactory) {
    this.colorFactory = colorFactory;
    this.knownColors = new HashMap<>();
  }

  // ==========================================================================

  /**
   * Converts the given PDColor object to a related {@link Color} object.
   * 
   * @param color
   *        The color to convert.
   * @param colorSpace
   *        The color space.
   * 
   * @return The converted color.
   */
  public Color convert(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }

    // Check if the color is already known.
    Color knownColor = getKnownColor(color, colorSpace);
    if (knownColor != null) {
      return knownColor;
    }

    // The color is not known. Create a new color.
    Color newColor = this.colorFactory.create();
    newColor.setId("color-" + this.knownColors.size());
    newColor.setName(computeColorName(color, colorSpace));
    newColor.setRGB(computeRGB(color, colorSpace));

    // Add the new color to the map of known colors.
    this.knownColors.put(newColor.getName(), newColor);
    log.debug("A new color was registered: " + newColor);

    return newColor;
  }

  // ==========================================================================

  /**
   * Checks if the given color is an already known color.
   * 
   * @param color
   *        The color to check.
   * @param colorSpace
   *        The color space of the color to check.
   *
   * @return True, if the given color is a known color; false otherwise.
   */
  protected boolean isKnownColor(PDColor color, PDColorSpace colorSpace) {
    return getKnownColor(color, colorSpace) != null;
  }

  /**
   * Returns a {@link Color} object related to the given color and given color
   * space if the color is already known; null otherwise.
   * 
   * @param color
   *        The color to check.
   * @param colorSpace
   *        The color space of the color to check.
   *
   * @return A {@link Color} object related to the given color and given color
   *         space if the color is already known; null otherwise.
   */
  protected Color getKnownColor(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }
    return this.knownColors.get(computeColorName(color, colorSpace));
  }

  /**
   * Computes a name for the given color.
   * 
   * @param color
   *        The color to process.
   * @param colorSpace
   *        The color space of the color to process.
   * 
   * @return A name for the given color.
   */
  protected String computeColorName(PDColor color, PDColorSpace colorSpace) {
    return Arrays.toString(computeRGB(color, colorSpace));
  }

  /**
   * Computes the RGB-value for the given color.
   * 
   * @param color
   *        The color to process.
   * @param colorSpace
   *        The color space of the color to process.
   * 
   * @return An array of length 3, containing the R, G and B values.
   */
  protected float[] computeRGB(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }

    try {
      return colorSpace.toRGB(color.getComponents());
    } catch (Exception e) {
      // TODO: Handle the exception.
      return null;
    }
  }
}
