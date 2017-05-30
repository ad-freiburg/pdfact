package icecite.models;

/**
 * A font in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFont extends PdfResource {
  /**
   * Returns the normalized name of this font. That is the lower cased name of
   * the font as it appears in the PDF document without the substring up to the
   * "+" sign. For example, if the name of font is "FPVPVX+NimbusRomNo9L-Medi", 
   * this method returns "nimbusromno9l-medi".
   * 
   * @return The normalized name of this font.
   */
  String getNormalizedName();

  /**
   * Sets the normalized name of this font.
   * 
   * @param name
   *        The normalized name of this font.
   */
  void setNormalizedName(String name);

  // ==========================================================================

  /**
   * Returns the basename of this font, that is the basename of this font
   * without the substring behind the "-" sign and without any digits or any
   * other special characters. For example, if the basename of the font is 
   * "nimbusromno9l-medi", this method returns "nimbusromnol".
   * 
   * @return The basename of this font.
   */
  String getBaseName();

  /**
   * Sets the basename of this font.
   * 
   * @param name
   *        The basename of this font.
   */
  void setBaseName(String name);


  // ==========================================================================

  /**
   * Returns the name of the font family of this font. For example, if the font
   * is "cmr9", this method returns "computer modern".
   * 
   * @return The name of the font family of this font.
   */
  String getFontFamilyName();

  /**
   * Sets the name of the font family of this font.
   * 
   * @param name
   *        The font family name.
   */
  void setFontFamilyName(String name);
  
  // ==========================================================================

  /**
   * Returns true, if this font is a Type3 font.
   * 
   * @return True, if this font is a Type3 font. False otherwise.
   */
  boolean isType3Font();

  /**
   * Sets the boolean flag that indicates whether this font is a Type3 font.
   * 
   * @param isType3Font
   *        The boolean flag that indicates whether this font is a Type3 font.
   */
  void setIsType3Font(boolean isType3Font);

  // ==========================================================================

  /**
   * Returns true, if this font represents a font with a bold font face.
   * 
   * @return True, if this font represents a font with a bold font face, False
   *         otherwise.
   */
  boolean isBold();

  /**
   * Sets the boolean flag that indicates whether this font represents a font
   * with a bold font face.
   * 
   * @param isBold
   *        The boolean flag that indicates whether this font represents a font
   *        with a bold font face.
   */
  void setIsBold(boolean isBold);

  // ==========================================================================

  /**
   * Returns true, if this font represents a font with a bold font face.
   * 
   * @return True, if this font represents a font with a bold font face, False
   *         otherwise.
   */
  boolean isItalic();

  /**
   * Sets the boolean flag that indicates whether this font represents a font
   * with an italic bold font face.
   * 
   * @param isItalic
   *        The boolean flag that indicates whether this font represents a font
   *        with an italic font face.
   */
  void setIsItalic(boolean isItalic);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfFont}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfFontFactory {
    /**
     * Creates a new PdfFont.
     * 
     * @return An instance of {@link PdfFont}.
     */
    PdfFont create();
  }
}
