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
   * The sequence number of this characters in the extraction order.
   */
  protected int sequenceNumber;

  /**
   * The font face of this character.
   */
  protected PdfFontFace fontFace;

  /**
   * The color of this character.
   */
  protected PdfColor color;
  
  /**
   * The text of this character.
   */
  protected String text;

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
  public void setSequenceNumber(int pos) {
    this.sequenceNumber = pos;
  }

  @Override
  public int getSequenceNumber() {
    return this.sequenceNumber;
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
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getText(), otherCharacter.getText());
      builder.append(getSequenceNumber(), otherCharacter.getSequenceNumber());
      builder.append(getFeature(), otherCharacter.getFeature());
      builder.append(getPosition(), otherCharacter.getPosition());
      builder.append(getText(), otherCharacter.getText());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getColor());
    builder.append(getFontFace());
    builder.append(getText());
    builder.append(getSequenceNumber());
    builder.append(getFeature());
    builder.append(getPosition());
    builder.append(getText());
    return builder.hashCode();
  }
}
