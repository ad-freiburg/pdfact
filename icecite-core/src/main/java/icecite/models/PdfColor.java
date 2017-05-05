package icecite.models;

/**
 * A color in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfColor extends PdfResource {
  /**
   * Sets the RGB value of this color.
   * 
   * @param rgb
   *        The RGB value given as an array of three float values in [0,1].
   */
  void setRGB(float[] rgb);

  /**
   * Sets the RGB value of this color.
   * 
   * @param r
   *        The R value of this color.
   * @param g
   *        The G value of this color.
   * @param b
   *        The B value of this color.
   */
  void setRGB(float r, float g, float b);

  /**
   * Returns the rgb value of this color.
   * 
   * @return The rgb value given as array of three float values in [0,1].
   */
  float[] getRGB();
}
