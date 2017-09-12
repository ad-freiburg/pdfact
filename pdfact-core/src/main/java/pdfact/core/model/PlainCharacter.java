package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
   * The rank of this character in the extraction order of PDF elements.
   */
  protected int extractionRank;

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
  public int getExtractionRank() {
    return this.extractionRank;
  }

  @Override
  public void setExtractionRank(int num) {
    this.extractionRank = num;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "Character(" + getText() + ", " + getPosition() + ")";
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
