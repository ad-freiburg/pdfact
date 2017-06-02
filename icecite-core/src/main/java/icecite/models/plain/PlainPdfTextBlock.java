package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;

// TODO: Don't derive the bounding box in the model.

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The characters of this text block.
   */
  protected PdfCharacterList characters;

  /**
   * The text lines of this text block.
   */
  protected PdfTextLineList textLines;

  /**
   * The text of this text block.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacter}.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLine}.
   */
  @AssistedInject
  public PlainPdfTextBlock(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory) {
    this.characters = characterListFactory.create();
    this.textLines = textLineListFactory.create();
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
  public PdfTextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(PdfTextLineList textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLines(PdfTextLineList textLines) {
    for (PdfTextLine line : textLines) {
      addTextLine(line);
    }
  }

  @Override
  public void addTextLine(PdfTextLine line) {
    this.textLines.add(line);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextBlock(pos: " + getPosition() + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextBlock) {
      PdfTextBlock otherTextBlock = (PdfTextBlock) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherTextBlock.getPosition());
      builder.append(getTextLines(), otherTextBlock.getTextLines());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    builder.append(getTextLines());
    return builder.hashCode();
  }

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }
}
