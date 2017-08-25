package pdfact.model;

import com.google.inject.assistedinject.Assisted;

// TODO: Implement the support of other color schemes (CMYK, etc.)

/**
 * A color in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Color extends Resource {
  /**
   * Returns the name of this color.
   * 
   * @return The name of this color.
   */
  String getName();

  /**
   * Sets the name of this color.
   * 
   * @param name
   *        The name of this color.
   */
  void setName(String name);

  // ==========================================================================

  /**
   * Returns the RGB value of this color.
   * 
   * @return The RGB value given as an array of three float values in [0,1].
   */
  float[] getRGB();
  
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
   *        The R value.
   * @param g
   *        The G value.
   * @param b
   *        The B value.
   */
  void setRGB(float r, float g, float b);

  // ==========================================================================

  /**
   * The factory to create instances of {@link Color}.
   * 
   * @author Claudius Korzen
   */
  public interface ColorFactory {
    /**
     * Creates a new instance of {@link Color}.
     * 
     * @return A new instance of {@link Color}.
     */
    Color create();

    /**
     * Creates a new instance of {@link Color}.
     * 
     * @param rgb
     *        The RGB value of the color.
     * 
     * @return A new instance of {@link Color}.
     */
    Color create(float[] rgb);

    /**
     * Creates a new instance of {@link Color}.
     * 
     * @param r
     *        The R value.
     * @param g
     *        The G value.
     * @param b
     *        The B value.
     * 
     * @return A new instance of {@link Color}.
     */
    Color create(
        @Assisted("r") float r,
        @Assisted("g") float g,
        @Assisted("b") float b);
  }
}
