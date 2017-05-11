package icecite.utils.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.google.inject.Inject;

import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.utils.color.ColorUtils;

/**
 * A collection of utility methods that deal with images.
 * 
 * @author Claudius Korzen
 */
public class ImageUtils {
  /**
   * The factory to create instances of {@link PdfColor}.
   */
  // TODO: This won't work (because it's static).
  @Inject
  protected static PdfColorFactory pdfColorFactory;

  /**
   * Checks if the given image consists only of a single color and returns the
   * color if so. Returns null if there a at least two different colors.
   * 
   * @param image
   *        The image to process.
   * @return The color, if the image consists only of a single color; null
   *         otherwise.
   * @throws IOException
   *         if reading the image fails.
   */
  public static PdfColor getExclusiveColor(BufferedImage image)
      throws IOException {
    if (image == null) {
      return null;
    }

    int lastRgb = Integer.MAX_VALUE;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int rgb = image.getRGB(i, j);
        if (lastRgb != Integer.MAX_VALUE && lastRgb != rgb) {
          return null;
        }
        lastRgb = rgb;
      }
    }

    if (lastRgb == Integer.MAX_VALUE) {
      return null;
    }

    float[] rgbArray = ColorUtils.toRGBArray(lastRgb);
    return pdfColorFactory.create(rgbArray);
  }
}
