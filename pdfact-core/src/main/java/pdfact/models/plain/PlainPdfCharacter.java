package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfColor;
import pdfact.models.PdfElementType;
import pdfact.models.PdfFontFace;

/**
 * A plain implementation of {@link PdfCharacter}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacter extends PlainPdfSinglePositionElement
    implements PdfCharacter {
  /**
   * The sequence number of this character in the extraction order.
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
  public PdfElementType getType() {
    return PdfElementType.CHARACTER;
  }

  // ==========================================================================

  @Override
  public int getSequenceNumber() {
    return this.sequenceNumber;
  }

  @Override
  public void setSequenceNumber(int num) {
    this.sequenceNumber = num;
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
  public PdfColor getColor() {
    return this.color;
  }

  @Override
  public void setColor(PdfColor color) {
    this.color = color;
  }

  // ==========================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfCharacter(" + getText() + ", " + getPosition() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfCharacter) {
      PdfCharacter otherCharacter = (PdfCharacter) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getSequenceNumber(), otherCharacter.getSequenceNumber());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getText(), otherCharacter.getText());
      builder.append(getPosition(), otherCharacter.getPosition());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getSequenceNumber());
    builder.append(getFontFace());
    builder.append(getColor());
    builder.append(getText());
    builder.append(getPosition());
    return builder.hashCode();
  }
}
