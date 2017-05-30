package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Rectangle;

// TODO: Do not derive the bounding box in the model.

/**
 * A plain implementation of {@link PdfTextLine}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLine extends PlainPdfElement implements PdfTextLine {
  /**
   * The page in which this line is located.
   */
  protected PdfPage page;

  /**
   * The characters of this text line.
   */
  protected PdfCharacterList characters;

  /**
   * The words of this text line.
   */
  protected PdfWordList words;

  /**
   * The text of this text line.
   */
  protected String text;

  /**
   * The baseline of this text line.
   */
  protected Line baseLine;

  // ==========================================================================

  /**
   * Creates a new text line.
   * 
   * @param page
   *        The page in which this line is located.
   * @param characters
   *        The characters of this text line.
   * @param wordListFactory
   *        The factory to create instances of PdfWordList.
   */
  @AssistedInject
  public PlainPdfTextLine(@Assisted PdfPage page,
      @Assisted PdfCharacterList characters,
      PdfWordListFactory wordListFactory) {
    this.page = page;
    this.characters = characters;
    this.words = wordListFactory.create();
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
  public PdfCharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterList characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(PdfCharacterList characters) {
    for (PdfCharacter character : characters) {
      addCharacter(character);
    }
  }

  @Override
  public void addCharacter(PdfCharacter character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public PdfWordList getWords() {
    return this.words;
  }

  @Override
  public PdfWord getFirstWord() {
    if (this.words != null && !this.words.isEmpty()) {
      return this.words.get(0);
    }
    return null;
  }

  @Override
  public PdfWord getLastWord() {
    if (this.words != null && !this.words.isEmpty()) {
      return this.words.get(this.words.size() - 1);
    }
    return null;
  }

  @Override
  public void setWords(PdfWordList words) {
    this.words = words;
  }

  @Override
  public void addWords(PdfWordList words) {
    for (PdfWord word : words) {
      addWord(word);
    }
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
  public Rectangle getRectangle() {
    return this.characters.getRectangle();
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    // The bounding box results from the characters of this text block.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public Line getBaseline() {
    return this.baseLine;
  }

  @Override
  public void setBaseline(Line baseLine) {
    this.baseLine = baseLine;
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return PdfType.TEXTLINES;
  }
}
