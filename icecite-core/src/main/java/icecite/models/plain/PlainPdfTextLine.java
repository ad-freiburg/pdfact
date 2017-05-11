package icecite.models.plain;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfFont;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;

/**
 * A plain implementation of {@link PdfTextLine}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLine extends PlainPdfElement implements PdfTextLine {
  /**
   * The characters of this page.
   */
  protected PdfCharacterSet characters;

  /**
   * The words of this text line.
   */
  protected List<PdfWord> words;

  /**
   * The text of this text line.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new text line.
   * 
   * @param characters
   *        The characters of this text line.
   */
  @AssistedInject
  public PlainPdfTextLine(@Assisted PdfCharacterSet characters) {
    this.characters = characters;
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
  public List<PdfWord> getWords() {
    return this.words;
  }

  @Override
  public void setWords(List<PdfWord> words) {
    this.words = words;
  }

  @Override
  public void addWord(PdfWord word) {
    this.words.add(word);
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
