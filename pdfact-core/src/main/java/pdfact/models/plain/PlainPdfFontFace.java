package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfFont;
import pdfact.models.PdfFontFace;

/**
 * A plain implementation of {@link PdfFontFace}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFontFace implements PdfFontFace {
  /**
   * The font.
   */
  protected PdfFont font;

  /**
   * The font size.
   */
  protected float fontSize;

  // ==========================================================================
  // The constructors.

  /**
   * Creates a new font face.
   * 
   * @param font
   *        The font.
   * @param fontSize
   *        The font size.
   */
  @AssistedInject
  public PlainPdfFontFace(@Assisted PdfFont font, @Assisted float fontSize) {
    this.font = font;
    this.fontSize = fontSize;
  }

  // ==========================================================================

  @Override
  public PdfFont getFont() {
    return this.font;
  }

  @Override
  public void setFont(PdfFont font) {
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
    return "PlainPdfFontFace(" + this.font + ", " + this.fontSize + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfFontFace) {
      PdfFontFace otherCharacter = (PdfFontFace) other;

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
