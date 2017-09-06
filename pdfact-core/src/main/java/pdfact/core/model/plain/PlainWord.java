package pdfact.core.model.plain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Position;
import pdfact.core.model.Word;
import pdfact.core.util.list.CharacterList;
import pdfact.core.util.list.CharacterList.CharacterListFactory;

/**
 * A plain implementation of {@link Word}.
 * 
 * @author Claudius Korzen
 */
public class PlainWord extends PlainElement implements Word {
  /**
   * The characters of this word.
   */
  protected CharacterList characters;

  /**
   * The text of this word.
   */
  protected String text;

  /**
   * The positions of this word.
   */
  protected List<Position> positions;

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
  protected CharacterStatistic characterStatistic;

  // ==========================================================================

  /**
   * Creates a new word.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   */
  @AssistedInject
  public PlainWord(CharacterListFactory characterListFactory) {
    this.characters = characterListFactory.create();
  }

  // ==========================================================================

  @Override
  public CharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public Character getFirstCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(0);
  }

  @Override
  public Character getLastCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(this.characters.size() - 1);
  }

  @Override
  public void setCharacters(CharacterList characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(CharacterList characters) {
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(Character character) {
    this.characters.add(character);
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
  public List<Position> getPositions() {
    return this.positions;
  }

  @Override
  public Position getFirstPosition() {
    if (this.positions == null || this.positions.isEmpty()) {
      return null;
    }
    return this.positions.get(0);
  }

  @Override
  public Position getLastPosition() {
    if (this.positions == null || this.positions.isEmpty()) {
      return null;
    }
    return this.positions.get(this.positions.size() - 1);
  }

  @Override
  public void setPositions(List<Position> positions) {
    this.positions = positions;
  }

  @Override
  public void addPositions(List<Position> positions) {
    this.positions.addAll(positions);
  }

  @Override
  public void addPosition(Position position) {
    this.positions.add(position);
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
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistics) {
    this.characterStatistic = statistics;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainWord(" + this.text + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Word) {
      Word otherWord = (Word) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getCharacters(), otherWord.getCharacters());
      builder.append(getText(), otherWord.getText());
      builder.append(getPositions(), otherWord.getPositions());
      builder.append(isHyphenated(), otherWord.isHyphenated());
      builder.append(isDehyphenated(), otherWord.isDehyphenated());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getCharacters());
    builder.append(getText());
    builder.append(getPositions());
    builder.append(isHyphenated());
    builder.append(isDehyphenated());
    return builder.hashCode();
  }
}
