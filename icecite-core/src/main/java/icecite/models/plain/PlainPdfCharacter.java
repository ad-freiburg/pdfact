package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfColor;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfType;

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
  // The constructors.

  /**
   * Creates a new PdfCharacter.
   * 
   * @param page
   *        The page in which this character is located.
   */
  @AssistedInject
  public PlainPdfCharacter(@Assisted PdfPage page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public void setPage(PdfPage page) {
    this.page = page;
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
  public PdfType getType() {
    return PdfType.CHARACTERS;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfCharacter(" + this.text + ", " + this.boundingBox + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfCharacter) {
      PdfCharacter otherCharacter = (PdfCharacter) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherCharacter.getPage());
      builder.append(getText(), otherCharacter.getText());
      builder.append(getRectangle(), otherCharacter.getRectangle());
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
    builder.append(getPage());
    builder.append(getText());
    builder.append(getRectangle());
    builder.append(getFontFace());
    builder.append(getColor());
    builder.append(getText());
    return builder.hashCode();
  }
}
