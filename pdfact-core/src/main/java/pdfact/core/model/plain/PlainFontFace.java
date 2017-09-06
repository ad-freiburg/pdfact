package pdfact.core.model.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Font;
import pdfact.core.model.FontFace;

/**
 * A plain implementation of {@link FontFace}.
 * 
 * @author Claudius Korzen
 */
public class PlainFontFace implements FontFace {
  /**
   * The font.
   */
  protected Font font;

  /**
   * The font size.
   */
  protected float fontSize;

  /**
   * Creates a new font face.
   * 
   * @param font
   *        The font.
   * @param fontSize
   *        The font size.
   */
  @AssistedInject
  public PlainFontFace(@Assisted Font font, @Assisted float fontSize) {
    this.font = font;
    this.fontSize = fontSize;
  }

  // ==========================================================================

  @Override
  public Font getFont() {
    return this.font;
  }

  @Override
  public void setFont(Font font) {
    this.font = font;
  }

  // ==========================================================================

  @Override
  public float getFontSize() {
    return this.fontSize;
  }

  @Override
  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainFontFace(" + this.font + ", " + this.fontSize + ")";
  }

  // ==========================================================================

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
