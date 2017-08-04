package pdfact.models;

// TODO: Implement the support of other color schemes (CMYK, etc.)

/**
 * A color in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfColor extends PdfResource {
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
   * Sets the RGB value of this color.
   * 
   * @param rgb
   *        The RGB value given as an array of three float values in range
   *        [0,1].
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

  /**
   * Returns the RGB value of this color.
   * 
   * @return The RGB value given as an array of three float values in range
   *         [0,1].
   */
  float[] getRGB();

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfColor}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfColorFactory {
    /**
     * Creates a new instance of PdfColor.
     * 
     * @return A new instance of {@link PdfColor}.
     */
    PdfColor create();

    /**
     * Creates a new instance of PdfColor.
     * 
     * @param rgb
     *        The RGB value of the color.
     * 
     * @return A new instance of {@link PdfColor}.
     */
    PdfColor create(float[] rgb);
  }
}
