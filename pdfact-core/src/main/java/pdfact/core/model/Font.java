package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A font of text in a document.
 * 
 * @author Claudius Korzen
 */
public class Font extends Resource {
  /**
   * The (normalized) name of this font.
   */
  protected String normalizedName;

  /**
   * The base name of this font.
   */
  protected String basename;

  /**
   * The name of the font family.
   */
  protected String fontFamilyName;

  /**
   * The boolean flag that indicates whether this font is a Type-3 font.
   */
  protected boolean isType3Font;

  /**
   * The boolean flag that indicates whether this font is a bold-faced font.
   */
  protected boolean isBold;

  /**
   * The boolean flag that indicates whether this font is an italic-faced font.
   */
  protected boolean isItalic;

  // ==============================================================================================

  /**
   * Returns the normalized name of this font. That is the lower cased name of the
   * font as it appears in the document without the substring till the "+" sign.
   * For example, if the name of font is "FPVPVX+NimbusRomNo9L-Medi", the
   * normalized name is "nimbusromno9l-medi".
   * 
   * @return The normalized name of this font.
   */
  public String getNormalizedName() {
    return this.normalizedName;
  }

  /**
   * Sets the normalized name of this font.
   * 
   * @param name The normalized name of this font.
   */
  public void setNormalizedName(String name) {
    this.normalizedName = name;
  }

  // ==============================================================================================

  /**
   * Returns the basename of this font, that is the normalized name of this font
   * without the substring behind the "-" sign, without any digits and without any
   * other special characters. For example, if the normalized name of the font is
   * "nimbusromno9l-medi", the base name is "nimbusromnol".
   * 
   * @return The basename of this font.
   */
  public String getBaseName() {
    return this.basename;
  }

  /**
   * Sets the basename of this font.
   * 
   * @param name The basename of this font.
   */
  public void setBasename(String name) {
    this.basename = name;
  }

  // ==============================================================================================

  /**
   * Returns the name of the font family to which this fonts belongs to. For
   * example, if the font is "cmr9", this method returns "computer modern".
   * 
   * @return The name of the font family.
   */
  public String getFontFamilyName() {
    return this.fontFamilyName;
  }

  /**
   * Sets the name of the font family to which this fonts belongs to.
   * 
   * @param name The name of the font family.
   */
  public void setFontFamilyName(String name) {
    this.fontFamilyName = name;
  }

  // ==============================================================================================

  /**
   * Returns true, if this font is a Type-3 font.
   * 
   * @return True, if this font is a Type-3 font; false otherwise.
   */
  public boolean isType3Font() {
    return this.isType3Font;
  }

  /**
   * Sets the boolean flag that indicates whether this font is a Type-3 font.
   * 
   * @param isType3Font The boolean flag that indicates whether this font is a
   *                    Type-3 font.
   */
  public void setIsType3Font(boolean isType3Font) {
    this.isType3Font = isType3Font;
  }

  // ==============================================================================================

  /**
   * Returns true, if this font is a bold-faced font.
   * 
   * @return True, if this font is a bold-faced font; false otherwise.
   */
  public boolean isBold() {
    return this.isBold;
  }

  /**
   * Sets the boolean flag that indicates whether this font is a bold-faced font.
   * 
   * @param isBold The boolean flag that indicates whether this font is a
   *               bold-faced font.
   */
  public void setIsBold(boolean isBold) {
    this.isBold = isBold;
  }

  // ==============================================================================================

  /**
   * Returns true, if this font is an italic-faced font.
   * 
   * @return True, if this font is an italic-faced font, false otherwise.
   */
  public boolean isItalic() {
    return this.isItalic;
  }

  /**
   * Sets the boolean flag that indicates whether this font is an italic-faced
   * font.
   * 
   * @param isItalic The boolean flag that indicates whether this font is an
   *                 italic-faced font.
   */
  public void setIsItalic(boolean isItalic) {
    this.isItalic = isItalic;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Font(" + this.normalizedName + ", " + this.basename + ", " + this.fontFamilyName 
        + ", id: " + this.id + ", isType3: " + this.isType3Font + ", isBold: " 
        + this.isBold + ", isItalic: " + this.isItalic() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Font) {
      Font otherFont = (Font) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getNormalizedName(), otherFont.getNormalizedName());
      builder.append(isType3Font(), otherFont.isType3Font());
      builder.append(isBold(), otherFont.isBold());
      builder.append(isItalic(), otherFont.isItalic());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getNormalizedName());
    builder.append(isType3Font());
    builder.append(isBold());
    builder.append(isItalic());
    return builder.hashCode();
  }
}
