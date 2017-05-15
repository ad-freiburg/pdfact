package icecite.models.plain;

import icecite.models.PdfCharacter;
import icecite.models.PdfColor;
import icecite.models.PdfFont;

/**
 * A plain implementation of {@link PdfCharacter}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacter extends PlainPdfElement implements PdfCharacter {
  /**
   * The extraction order number.
   */
  protected int extractionOrderNumber;

  /**
   * The text of this character.
   */
  protected String text;

  /**
   * The font of this character.
   */
  protected PdfFont font;

  /**
   * The font size of this character.
   */
  protected float fontsize;

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
  public PdfFont getFont() {
    return this.font;
  }

  @Override
  public void setFont(PdfFont font) {
    this.font = font;
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
  public void setFontSize(float fontsize) {
    this.fontsize = fontsize;
  }

  @Override
  public float getFontSize() {
    return this.fontsize;
  }

  // ==========================================================================

  @Override
  public void setExtractionOrderNumber(int num) {
    this.extractionOrderNumber = num;
  }

  @Override
  public int getExtractionOrderNumber() {
    return this.extractionOrderNumber;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return this.text;
    // return "PlainPdfCharacter(" + this.text + ", " + this.boundingBox + ")";
  }
}
