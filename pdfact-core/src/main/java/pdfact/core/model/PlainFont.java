package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A plain implementation of {@link Font}.
 * 
 * @author Claudius Korzen
 */
public class PlainFont extends PlainResource implements Font {
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

  // ==========================================================================

  @Override
  public String getNormalizedName() {
    return this.normalizedName;
  }

  @Override
  public void setNormalizedName(String name) {
    this.normalizedName = name;
  }

  // ==========================================================================

  @Override
  public String getBaseName() {
    return this.basename;
  }

  @Override
  public void setBasename(String name) {
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
    return "Font(" + this.normalizedName + ", " + this.basename + ", "
        + this.fontFamilyName + ", id: " + this.id + ", isType3: "
        + this.isType3Font + ", isBold: " + this.isBold + ", isItalic: "
        + this.isItalic() + ")";
  }

  // ==========================================================================

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
