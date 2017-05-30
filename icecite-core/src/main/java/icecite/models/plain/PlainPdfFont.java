package icecite.models.plain;

import icecite.models.PdfFont;

/**
 * A plain implementation of {@link PdfFont}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFont implements PdfFont {
  /**
   * The full name of this font.
   */
  protected String name;

  /**
   * The base name of this font.
   */
  protected String basename;

  /**
   * The name of the font family of this font.
   */
  protected String fontFamilyName;

  /**
   * The boolean flag that indicates whether this font is a Type3 font.
   */
  protected boolean isType3Font;

  /**
   * The boolean flag that indicates whether this font is a bold font.
   */
  protected boolean isBold;

  /**
   * The boolean flag that indicates whether this font is an italic font.
   */
  protected boolean isItalic;

  // ==========================================================================

  @Override
  public String getNormalizedName() {
    return this.name;
  }

  @Override
  public void setNormalizedName(String name) {
    this.name = name;
  }

  // ==========================================================================

  @Override
  public String getBaseName() {
    return this.basename;
  }

  @Override
  public void setBaseName(String name) {
    this.basename = name;
  }

  // ==========================================================================

  @Override
  public String getFontFamilyName() {
    return this.fontFamilyName;
  }

  @Override
  public void setFontFamilyName(String name) {
    this.fontFamilyName = name;
  }

  // ==========================================================================

  @Override
  public boolean isType3Font() {
    return this.isType3Font;
  }

  @Override
  public void setIsType3Font(boolean isType3Font) {
    this.isType3Font = isType3Font;
  }

  // ==========================================================================

  @Override
  public boolean isBold() {
    return this.isBold;
  }

  @Override
  public void setIsBold(boolean isBold) {
    this.isBold = isBold;
  }

  // ==========================================================================

  @Override
  public boolean isItalic() {
    return this.isItalic;
  }

  @Override
  public void setIsItalic(boolean isItalic) {
    this.isItalic = isItalic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfFont(" + this.name + ", " + this.basename + ", "
        + this.fontFamilyName + ", isType3: " + this.isType3Font + ", isBold: "
        + this.isBold + ", isItalic: " + this.isItalic() + ")";
  }
}
