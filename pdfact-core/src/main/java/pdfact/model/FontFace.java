package pdfact.model;

/**
 * A font face in a PDF document - that is a pair consisting of a font and a
 * font size.
 * 
 * @author Claudius Korzen
 */
public interface FontFace {
  /**
   * Returns the font.
   * 
   * @return The font.
   */
  Font getFont();

  /**
   * Sets the font.
   * 
   * @param font
   *        The font.
   */
  void setFont(Font font);

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
   * The factory to create instances of {@link FontFace}.
   * 
   * @author Claudius Korzen
   */
  public interface FontFaceFactory {
    /**
     * Creates a new instance of {@link FontFace}.
     * 
     * @param font
     *        The font.
     * @param fontSize
     *        The font size.
     * 
     * @return A new instance of {@link FontFace}.
     */
    FontFace create(Font font, float fontSize);
  }
}
