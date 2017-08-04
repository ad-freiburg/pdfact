package pdfact.models;

/**
 * A font face in a PDF document - that is a pair consisting of a font and a
 * font size.
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
     * Creates a new instance of PdfFontFace.
     * 
     * @param font
     *        The font.
     * @param fontSize
     *        The font size.
     * 
     * @return A new instance of {@link PdfFontFace}.
     */
    PdfFontFace create(PdfFont font, float fontSize);
  }
}
