package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfFeature;
import icecite.models.PdfWord;

/**
 * A plain implementation of {@link PdfWord}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWord extends PlainPdfElement implements PdfWord {
  /**
   * The characters of this word.
   */
  protected PdfCharacterList characters;

  /**
   * The text of this word.
   */
  protected String text;

  /**
   * The boolean flag that indicates if this word is hyphenated.
   */
  protected boolean isHyphenated;

  /**
   * The boolean flag that indicates if this word is dehyphenated.
   */
  protected boolean isDehyphenated;

  /**
   * The statistics about the characters in this word.
   */
  protected PdfCharacterStatistic characterStatistic;

  // ==========================================================================

  /**
   * Creates a new word.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   */
  @AssistedInject
  public PlainPdfWord(PdfCharacterListFactory characterListFactory) {
    this.characters = characterListFactory.create();
  }

  /**
   * Creates a new word.
   * 
   * @param characters
   *        The characters of this word.
   */
  @AssistedInject
  public PlainPdfWord(@Assisted PdfCharacterList characters) {
    this.characters = characters;
  }

  // ==========================================================================

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.WORD;
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
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(PdfCharacter character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public boolean isHyphenated() {
    return this.isHyphenated;
  }

  @Override
  public void setIsHyphenated(boolean isHyphenated) {
    this.isHyphenated = isHyphenated;
  }

  // ==========================================================================

  @Override
  public boolean isDehyphenated() {
    return this.isDehyphenated;
  }

  @Override
  public void setIsDehyphenated(boolean isDehyphenated) {
    this.isDehyphenated = isDehyphenated;
  }

  // ==========================================================================

  @Override
  public PdfCharacter getFirstCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(0);
  }

  @Override
  public PdfCharacter getLastCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(this.characters.size() - 1);
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(PdfCharacterStatistic statistics) {
    this.characterStatistic = statistics;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfWord(" + this.text + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfWord) {
      PdfWord otherWord = (PdfWord) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherWord.getPosition());
      builder.append(getText(), otherWord.getText());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    builder.append(getText());
    return builder.hashCode();
  }
}
