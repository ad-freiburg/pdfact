package icecite.models.plain;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfTextBlock;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The characters of this page.
   */
  protected PdfCharacterList characters;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param characters
   *        The characters of this block.
   */
  @AssistedInject
  public PlainPdfTextBlock(@Assisted PdfCharacterList characters) {
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
}
