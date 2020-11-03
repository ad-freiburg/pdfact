package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A font face of text in a document, that is: a pair consisting of a font and a
 * font size.
 * 
 * @author Claudius Korzen
 */
public class FontFace {
  /**
   * The font.
   */
  protected Font font;

  /**
   * The font size.
   */
  protected float fontSize;

  // ==============================================================================================

  /**
   * Creates a new font face.
   * 
   * @param font     The font.
   * @param fontSize The font size.
   */
  public FontFace(Font font, float fontSize) {
    this.font = font;
    this.fontSize = fontSize;
  }

  // ==============================================================================================

  /**
   * Returns the font.
   * 
   * @return The font.
   */
  public Font getFont() {
    return this.font;
  }

  /**
   * Sets the font.
   * 
   * @param font The font.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  // ==============================================================================================

  /**
   * Returns the font size.
   * 
   * @return The font size.
   */
  public float getFontSize() {
    return this.fontSize;
  }

  /**
   * Sets the font size.
   * 
   * @param fontSize The font size.
   */
  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "FontFace(" + this.font + ", " + this.fontSize + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof FontFace) {
      FontFace otherCharacter = (FontFace) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getFont(), otherCharacter.getFont());
      builder.append(getFontSize(), otherCharacter.getFontSize());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getFont());
    builder.append(getFontSize());
    return builder.hashCode();
  }
}
