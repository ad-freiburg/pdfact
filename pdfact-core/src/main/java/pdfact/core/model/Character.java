package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A single character in a document.
 * 
 * @author Claudius Korzen
 */
public class Character extends Element implements HasText, HasPosition, HasFontFace, HasColor {
  /**
   * The text of this character.
   */
  protected String text;

  /**
   * The position of this character in the document.
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
   * The rank of this character in the extraction order of elements.
   */
  protected int extractionRank;

  // ==============================================================================================

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  // ==============================================================================================

  public Position getPosition() {
    return this.position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  // ==============================================================================================

  public FontFace getFontFace() {
    return this.fontFace;
  }

  public void setFontFace(FontFace fontFace) {
    this.fontFace = fontFace;
  }

  // ==============================================================================================

  public Color getColor() {
    return this.color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  // ==============================================================================================

  public int getExtractionRank() {
    return this.extractionRank;
  }

  public void setExtractionRank(int num) {
    this.extractionRank = num;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Character(" + getText() + ", " + getPosition() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Character) {
      Character otherCharacter = (Character) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherCharacter.getText());
      builder.append(getPosition(), otherCharacter.getPosition());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getExtractionRank(), otherCharacter.getExtractionRank());

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
    builder.append(getExtractionRank());
    return builder.hashCode();
  }
}
