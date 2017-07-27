package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfFeature;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.utils.geometric.Line;

// TODO: Do not derive the bounding box in the model.

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
   * @param characters
   *        The characters of this text line.
   * @param wordListFactory
   *        The factory to create instances of {@link PdfWordList}.
   */
  @AssistedInject
  public PlainPdfTextLine(@Assisted PdfCharacterList characters,
      PdfWordListFactory wordListFactory) {
    this.characters = characters;
    this.words = wordListFactory.create();
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
  public Line getBaseline() {
    return this.baseLine;
  }

  @Override
  public void setBaseline(Line baseLine) {
    this.baseLine = baseLine;
  }

  // ==========================================================================

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.TEXT_LINE;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextLine(" + this.text + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextLine) {
      PdfTextLine otherTextLine = (PdfTextLine) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getCharacters(), otherTextLine.getCharacters());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getCharacters());
    return builder.hashCode();
  }
}
