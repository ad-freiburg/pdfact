package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
   * The characters of this text block.
   */
  protected PdfCharacterList characters;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param page
   *        The page in which this text block is located.
   * @param chars
   *        The characters of this text block.
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

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextBlock(page: " + this.page.getPageNumber() + "rect: "
        + this.boundingBox + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextBlock) {
      PdfTextBlock otherTextBlock = (PdfTextBlock) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherTextBlock.getPage());
      builder.append(getRectangle(), otherTextBlock.getRectangle());
      builder.append(getCharacters(), otherTextBlock.getCharacters());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPage());
    builder.append(getRectangle());
    builder.append(getCharacters());
    return builder.hashCode();
  }
}
