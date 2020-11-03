package pdfact.core.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.core.util.list.ElementList;

/**
 * A word in a document.
 * 
 * @author Claudius Korzen
 */
public class Word extends Element implements HasCharacters, HasText, HasPositions {
  /**
   * The characters of this word.
   */
  protected ElementList<Character> characters;

  /**
   * The text of this word.
   */
  protected String text;

  /**
   * The positions of this word.
   */
  protected List<Position> positions;

  /**
   * The boolean flag that indicates whether this word is hyphenated.
   */
  protected boolean isHyphenated;

  /**
   * The boolean flag that indicates whether this word is dehyphenated.
   */
  protected boolean isDehyphenated;

  /**
   * The statistic about the characters.
   */
  protected CharacterStatistic characterStatistic;

  // ==============================================================================================

  /**
   * Creates a new word.
   */
  public Word() {
    this.characters = new ElementList<>();
  }

  // ==============================================================================================

  @Override
  public ElementList<Character> getCharacters() {
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
  public void setCharacters(ElementList<Character> characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(ElementList<Character> characters) {
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(Character character) {
    this.characters.add(character);
  }

  // ==============================================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ==============================================================================================

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

  // ==============================================================================================

  /**
   * Returns true if this word is hyphenated.
   * 
   * @return True if this word is hyphenated; false otherwise.
   */
  public boolean isHyphenated() {
    return this.isHyphenated;
  }

  /**
   * Sets the boolean flag that indicates whether this word is hyphenated.
   * 
   * @param isHyphenated The boolean flag that indicates whether this word is
   *                     hyphenated.
   */
  public void setIsHyphenated(boolean isHyphenated) {
    this.isHyphenated = isHyphenated;
  }

  // ==============================================================================================

  /**
   * Returns true if this word is dehyphenated.
   * 
   * @return True if this word is dehyphenated; false otherwise.
   */
  public boolean isDehyphenated() {
    return this.isDehyphenated;
  }

  /**
   * Sets the boolean flag that indicates whether this word is dehyphenated.
   * 
   * @param isDehyphenated The boolean flag that indicates whether this word is
   *                       dehyphenated.
   */
  public void setIsDehyphenated(boolean isDehyphenated) {
    this.isDehyphenated = isDehyphenated;
  }

  @Override
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistics) {
    this.characterStatistic = statistics;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Word(" + this.text + ")";
  }

  // ==============================================================================================

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
