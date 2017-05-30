package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfPage;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.utils.geometric.Rectangle;

// TODO: Do not derive the bounding box in the model.

/**
 * A plain implementation of {@link PdfWord}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWord extends PlainPdfElement implements PdfWord {
  /**
   * The page in which this word is located.
   */
  protected PdfPage page;
  
  /**
   * The characters of this page.
   */
  protected PdfCharacterList characters;

  /**
   * The text of this word.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new word.
   * 
   * @param page
   *        The page in which the word is located.
   * @param characters
   *        The characters of this word.
   */
  @AssistedInject
  public PlainPdfWord(@Assisted PdfPage page,
      @Assisted PdfCharacterList characters) {
    this.page = page;
    this.characters = characters;
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
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
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
  public PdfType getType() {
    return PdfType.WORDS;
  }
}
