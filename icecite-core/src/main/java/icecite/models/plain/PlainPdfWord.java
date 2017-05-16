package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfType;
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
  public PdfType getType() {
    return PdfType.WORDS;
  }
}
