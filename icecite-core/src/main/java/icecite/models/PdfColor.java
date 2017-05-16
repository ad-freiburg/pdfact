package icecite.models;

/**
 * A color in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfColor extends PdfResource {
  /**
   * Returns the id of this color.
   * 
   * @return The id of this color.
   */
  String getId();
  
  /**
   * Sets the id of this color.
   * 
   * @param name The id of this color.
   */
  void setId(String name);
  
  // ==========================================================================
  
  /**
   * Returns the name of this color.
   * 
   * @return The name of this color.
   */
  String getName();
  
  /**
   * Sets the name of this color.
   * 
   * @param name The name of this color.
   */
  void setName(String name);
  
  // ==========================================================================
  
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

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfColor}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfColorFactory {
    /**
     * Creates a PdfColor.
     * 
     * @return An instance of {@link PdfColor}.
     */
    PdfColor create();

    /**
     * Creates a PdfColor.
     * 
     * @param rgb
     *        The RGB value of this color.
     * 
     * @return An instance of {@link PdfColor}.
     */
    PdfColor create(float[] rgb);
  }
}
