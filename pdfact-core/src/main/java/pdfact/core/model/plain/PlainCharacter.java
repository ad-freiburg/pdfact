package pdfact.core.model.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.core.model.Character;
import pdfact.core.model.Color;
import pdfact.core.model.FontFace;
import pdfact.core.model.Position;

/**
 * A plain implementation of {@link Character}.
 * 
 * @author Claudius Korzen
 */
public class PlainCharacter extends PlainElement implements Character {
  /**
   * The text of this character.
   */
  protected String text;

  /**
   * The position of this character.
   */
  protected Position position;

  /**
   * The font face of this character.
   */
  protected FontFace fontFace;

  /**
   * The color of this character.
   */
  protected Color color;

  /**
   * The sequence number of this character in the extraction order.
   */
  protected int sequenceNumber;

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
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  // ==========================================================================

  @Override
  public FontFace getFontFace() {
    return this.fontFace;
  }

  @Override
  public void setFontFace(FontFace fontFace) {
    this.fontFace = fontFace;
  }

  // ==========================================================================

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
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
  public String toString() {
    return "PlainCharacter(" + getText() + ", " + getPosition() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Character) {
      Character otherCharacter = (Character) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherCharacter.getText());
      builder.append(getPosition(), otherCharacter.getPosition());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getSequenceNumber(), otherCharacter.getSequenceNumber());

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
    builder.append(getSequenceNumber());
    return builder.hashCode();
  }
}
