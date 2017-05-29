package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The page in which this text block is located.
   */
  protected PdfPage page;

  /**
   * The characters of this page.
   */
  protected PdfCharacterList characters;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param page
   *        The page in which this block is located.
   * @param chars
   *        The characters of this block.
   */
  @AssistedInject
  public PlainPdfTextBlock(@Assisted PdfPage page,
      @Assisted PdfCharacterList chars) {
    this.page = page;
    this.characters = chars;
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
  public Rectangle getRectangle() {
    return this.characters.getRectangle();
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    // The bounding box results from the characters of this text block.
    throw new UnsupportedOperationException();
  }
}
