package icecite.models.plain;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfTextLine;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfTextLine}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLine extends PlainPdfElement implements PdfTextLine {
  /**
   * The characters of this text line.
   */
  protected PdfCharacterList characters;

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
  public PlainPdfTextLine(@Assisted PdfCharacterList characters) {
    this.characters = characters;
  }

  // ==========================================================================

  @Override
  public PdfCharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterList characters) {
    this.characters = characters;
  }
  
  @Override
  public void addCharacter(PdfCharacter character) {
    this.characters.add(character);
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
  public Rectangle getBoundingBox() {
    return this.characters.getBoundingBox();
  }

  @Override
  public void setBoundingBox(Rectangle boundingBox) {
    // The bounding box results from the characters of this text block.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return PdfType.TEXTLINES;
  }
}
