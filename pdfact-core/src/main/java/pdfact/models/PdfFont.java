package pdfact.models;

/**
 * A font in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFont extends PdfResource {
  /**
   * Returns the normalized name of this font. That is the lower cased name of
   * the font as it appears in the PDF document without the substring till the
   * "+" sign. For example, if the name of font is "FPVPVX+NimbusRomNo9L-Medi",
   * the normalized name is "nimbusromno9l-medi".
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
   * Returns the basename of this font, that is the normalized name of this font
   * without the substring behind the "-" sign, without any digits and without
   * any other special characters. For example, if the normalized name of the
   * font is "nimbusromno9l-medi", the base name is "nimbusromnol".
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
   * Returns the name of the font family to which this fonts belongs to. For
   * example, if the font is "cmr9", this method returns "computer modern".
   * 
   * @return The name of the font family.
   */
  String getFontFamilyName();

  /**
   * Sets the name of the font family to which this fonts belongs to.
   * 
   * @param name
   *        The name of the font family.
   */
  void setFontFamilyName(String name);

  // ==========================================================================

  /**
   * Returns true, if this font is a Type-3 font.
   * 
   * @return True, if this font is a Type-3 font, false otherwise.
   */
  boolean isType3Font();

  /**
   * Sets the boolean flag that indicates whether this font is a Type-3 font.
   * 
   * @param isType3Font
   *        The boolean flag that indicates whether this font is a Type-3 font.
   */
  void setIsType3Font(boolean isType3Font);

  // ==========================================================================

  /**
   * Returns true, if this font is bold.
   * 
   * @return True, if this font is bold, false otherwise.
   */
  boolean isBold();

  /**
   * Sets the boolean flag that indicates whether this font is bold.
   * 
   * @param isBold
   *        The boolean flag that indicates whether this font is bold.
   */
  void setIsBold(boolean isBold);

  // ==========================================================================

  /**
   * Returns true, if this font is italic.
   * 
   * @return True, if this font is italic, false otherwise.
   */
  boolean isItalic();

  /**
   * Sets the boolean flag that indicates whether this font is an italic font.
   * 
   * @param isItalic
   *        The boolean flag that indicates whether this font is an italic font.
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
     * Creates a new instance of PdfFont.
     * 
     * @return An instance of {@link PdfFont}.
     */
    PdfFont create();
  }
}
