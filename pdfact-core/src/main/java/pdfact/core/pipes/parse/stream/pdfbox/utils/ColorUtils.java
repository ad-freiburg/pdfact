package pdfact.core.pipes.parse.stream.pdfbox.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
   * 
   * @return The RGB values in an array of three values.
   */
  public static float[] toRgbArray(int pixel) {
    float alpha = (pixel >> 24) & 0xff;
    float red = ((pixel >> 16) & 0xff) / 255f;
    float green = ((pixel >> 8) & 0xff) / 255f;
    float blue = ((pixel) & 0xff) / 255f;
    return new float[] { red, green, blue, alpha };
  }

  /**
   * Checks if the given image consists only of a single color and returns the
   * color if so. Returns null if there a at least two different colors.
   * 
   * @param im
   *        The image to process.
   * 
   * @return The color, if the image consists only of a single color; null
   *         otherwise.
   * @throws IOException
   *         If something went wrong on reading the image.
   */
  public static float[] getExclusiveColor(BufferedImage im) throws IOException {
    if (im == null) {
      return null;
    }

    int lastRgb = Integer.MAX_VALUE;
    for (int i = 0; i < im.getWidth(); i++) {
      for (int j = 0; j < im.getHeight(); j++) {
        int rgb = im.getRGB(i, j);
        if (lastRgb != Integer.MAX_VALUE && lastRgb != rgb) {
          return null;
        }
        lastRgb = rgb;
      }
    }

    if (lastRgb == Integer.MAX_VALUE) {
      return null;
    }

    return toRgbArray(lastRgb);
  }
}
