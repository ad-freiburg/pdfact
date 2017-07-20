package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import icecite.models.PdfCharacter;
import icecite.models.PdfColor;
import icecite.models.PdfFeature;
import icecite.models.PdfFontFace;

/**
 * A plain implementation of {@link PdfCharacter}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacter extends PlainPdfElement implements PdfCharacter {
  /**
   * The position of this character in the sequence of extracted PDF elements.
   */
  protected int posInExtractionOrder;

  /**
   * The text of this character.
   */
  protected String text;

  /**
   * The font face of this character.
   */
  protected PdfFontFace fontFace;

  /**
   * The color of this character.
   */
  protected PdfColor color;
  
  // ==========================================================================

  @Override
  public PdfColor getColor() {
    return this.color;
  }

  @Override
  public void setColor(PdfColor color) {
    this.color = color;
  }

  // ==========================================================================

  @Override
  public PdfFontFace getFontFace() {
    return this.fontFace;
  }

  @Override
  public void setFontFace(PdfFontFace fontFace) {
    this.fontFace = fontFace;
  }

  // ==========================================================================

  @Override
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String getText() {
    return this.text;
  }

  // ==========================================================================

  @Override
  public void setPositionInExtractionOrder(int pos) {
    this.posInExtractionOrder = pos;
  }

  @Override
  public int getPositionInExtractionOrder() {
    return this.posInExtractionOrder;
  }

  // ==========================================================================

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.CHARACTER;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfCharacter(" + getText() + ", " + getPosition() + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfCharacter) {
      PdfCharacter otherCharacter = (PdfCharacter) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherCharacter.getText());
      builder.append(getPosition(), otherCharacter.getPosition());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getText(), otherCharacter.getText());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getText());
    builder.append(getPosition());
    builder.append(getFontFace());
    builder.append(getColor());
    builder.append(getText());
    return builder.hashCode();
  }
}
