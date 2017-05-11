package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfFont;
import icecite.models.PdfWord;

/**
 * A plain implementation of {@link PdfWord}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWord extends PlainPdfElement implements PdfWord {
  /**
   * The characters of this page.
   */
  protected PdfCharacterSet characters;

  /**
   * The text of this word.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new word.
   * 
   * @param characters
   *        The characters of this word.
   */
  @AssistedInject
  public PlainPdfWord(@Assisted PdfCharacterSet characters) {
    this.characters = characters;
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
  public PdfCharacterSet getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterSet characters) {
    this.characters = characters;
  }
  
  // ==========================================================================
  
  @Override
  public PdfFont getMostCommonFont() {
    return this.characters.getMostCommonFont();
  }

  @Override
  public PdfColor getMostCommonColor() {
    return this.characters.getMostCommonColor();
  }

  @Override
  public float getMostCommonFontsize() {
    return this.characters.getMostCommonFontsize();
  }

  @Override
  public float getAverageFontsize() {
    return this.characters.getAverageFontsize();
  }

  @Override
  public float getMostCommonHeight() {
    return this.characters.getMostCommonHeight();
  }

  @Override
  public float getAverageHeight() {
    return this.characters.getAverageHeight();
  }

  @Override
  public float getMostCommonWidth() {
    return this.characters.getMostCommonWidth();
  }

  @Override
  public float getAverageWidth() {
    return this.characters.getAverageWidth();
  }
}
