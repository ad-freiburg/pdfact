package icecite.models;

/**
 * A font face, that is a pair of font and font size.
 * 
 * @author Claudius Korzen
 */
public interface PdfFontFace {
  /**
   * Returns the font.
   * 
   * @return The font.
   */
  PdfFont getFont();

  /**
   * Sets the font.
   * 
   * @param font
   *        The font.
   */
  void setFont(PdfFont font);

  // ==========================================================================

  /**
   * Returns the font size.
   * 
   * @return The font size.
   */
  float getFontSize();

  /**
   * Sets the font size.
   * 
   * @param fontSize
   *        The font size.
   */
  void setFontSize(float fontSize);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfFontFace}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfFontFaceFactory {
    /**
     * Creates a new PdfFontFace.
     * 
     * @param font
     *        The font.
     * @param fontSize
     *        The font size.
     * 
     * @return An instance of {@link PdfFontFace}.
     */
    PdfFontFace create(PdfFont font, float fontSize);
  }
}
