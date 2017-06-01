package icecite.parse.stream.pdfbox.convert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import com.google.inject.Inject;

import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;

/**
 * A converter that converts PDColor objects into PdfColor objects.
 * 
 * @author Claudius Korzen
 */
public class PDColorConverter {
  /**
   * The factory to create instances of PdfColor.
   */
  protected PdfColorFactory colorFactory;

  /**
   * The already known PdfColor objects per name.
   */
  protected Map<String, PdfColor> knownColors;

  // ==========================================================================
  // The constructors.

  /**
   * Creates a new PDColorConverter.
   * 
   * @param colorFactory
   *        The factory to create instances of PdfColor.
   */
  @Inject
  public PDColorConverter(PdfColorFactory colorFactory) {
    this.colorFactory = colorFactory;
    this.knownColors = new HashMap<>();
  }

  // ==========================================================================

  /**
   * Converts the given PDColor object to a related PdfColor object.
   * 
   * @param color
   *        The color to convert.
   * @param colorSpace
   *        The color space.
   * 
   * @return The converted color.
   */
  public PdfColor convert(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }

    // Check if the color is already known.
    PdfColor knownColor = getKnownColor(color, colorSpace);
    if (knownColor != null) {
      return knownColor;
    }

    // The color is not known. Create a new color.
    float[] rgb = computeRGB(color, colorSpace);
    if (rgb == null) {
      return null;
    }
    
    PdfColor newColor = this.colorFactory.create();
    newColor.setId("color" + this.knownColors.size());
    newColor.setName(computeColorName(color, colorSpace));
    newColor.setRGB(rgb);
    
    this.knownColors.put(newColor.getName(), newColor);

    return newColor;
  }

  // ==========================================================================

  /**
   * Returns true if the given color is a known color.
   * 
   * @param color
   *        The color to check.
   * @param colorSpace
   *        The color space of the color.
   * @return True, if the given color is a known color, false otherwise.
   */
  protected boolean isKnownColor(PDColor color, PDColorSpace colorSpace) {
    return getKnownColor(color, colorSpace) != null;
  }

  /**
   * Returns true if the given color is a known color.
   * 
   * @param color
   *        The color to check.
   * @param colorSpace
   *        The color space of the color.
   *
   * @return True, if the given color is a known color, false otherwise.
   */
  protected PdfColor getKnownColor(PDColor color, PDColorSpace colorSpace) {
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
   *        The color space of the color.
   * @return A name for the given color.
   */
  protected String computeColorName(PDColor color, PDColorSpace colorSpace) {
    return Arrays.toString(computeRGB(color, colorSpace));
  }
  
  /**
   * Computes a name for the given color.
   * 
   * @param color
   *        The color to process.
   * @param colorSpace
   *        The color space of the color.
   * @return A name for the given color.
   */
  protected float[] computeRGB(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }
    
    try {
      return colorSpace.toRGB(color.getComponents());
    } catch (Exception e) {
      return null;
    }
  }
}
