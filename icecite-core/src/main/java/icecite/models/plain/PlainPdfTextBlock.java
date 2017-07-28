package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfParagraph;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfWord;

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The parent paragraph.
   */
  protected PdfParagraph parentParagraph;

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

  /**
   * The role of this paragraph.
   */
  protected PdfRole role;

  /**
   * The secondary role of this paragraph.
   */
  protected PdfRole secondaryRole;

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
  public PdfParagraph getParentPdfParagraph() {
    return this.parentParagraph;
  }

  @Override
  public void setParentPdfParagraph(PdfParagraph paragraph) {
    this.parentParagraph = paragraph;
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
  public PdfTextLine getFirstTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(0);
  }

  @Override
  public PdfTextLine getLastTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(this.textLines.size() - 1);
  }

  // ==========================================================================

  @Override
  public PdfWord getFirstWord() {
    PdfTextLine firstLine = getFirstTextLine();
    if (firstLine == null) {
      return null;
    }
    return firstLine.getFirstWord();
  }

  @Override
  public PdfWord getLastWord() {
    PdfTextLine lastLine = getLastTextLine();
    if (lastLine == null) {
      return null;
    }
    return lastLine.getLastWord();
  }

  // ==========================================================================

  @Override
  public PdfCharacter getFirstCharacter() {
    PdfWord firstWord = getFirstWord();
    if (firstWord == null) {
      return null;
    }
    return firstWord.getFirstCharacter();
  }

  @Override
  public PdfCharacter getLastCharacter() {
    PdfWord lastWord = getLastWord();
    if (lastWord == null) {
      return null;
    }
    return lastWord.getLastCharacter();
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
  public PdfRole getRole() {
    return this.role;
  }

  @Override
  public void setRole(PdfRole role) {
    this.role = role;
  }

  // ==========================================================================

  @Override
  public PdfRole getSecondaryRole() {
    return this.secondaryRole;
  }

  @Override
  public void setSecondaryRole(PdfRole secondaryRole) {
    this.secondaryRole = secondaryRole;
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
}
