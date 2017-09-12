package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.CharacterList;
import pdfact.core.util.list.WordList;
import pdfact.core.util.list.CharacterList.CharacterListFactory;
import pdfact.core.util.list.WordList.WordListFactory;

/**
 * A plain implementation of {@link TextLine}.
 * 
 * @author Claudius Korzen
 */
public class PlainTextLine extends PlainElement implements TextLine {
  /**
   * The characters of this text line.
   */
  protected CharacterList characters;

  /**
   * The words of this text line.
   */
  protected WordList words;

  /**
   * The text of this text line.
   */
  protected String text;

  /**
   * The position of this text line.
   */
  protected Position position;

  /**
   * The baseline of this text line.
   */
  protected Line baseLine;

  /**
   * The statistics about the characters in this text line.
   */
  protected CharacterStatistic characterStatistic;

  // ==========================================================================

  /**
   * Creates a new text line.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   * @param wordListFactory
   *        The factory to create instances of {@link WordList}.
   */
  @AssistedInject
  public PlainTextLine(CharacterListFactory characterListFactory,
      WordListFactory wordListFactory) {
    this.characters = characterListFactory.create();
    this.words = wordListFactory.create();
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
  public WordList getWords() {
    return this.words;
  }

  @Override
  public Word getFirstWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(0);
  }

  @Override
  public Word getLastWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(this.words.size() - 1);
  }

  @Override
  public void setWords(WordList words) {
    this.words = words;
  }

  @Override
  public void addWords(WordList words) {
    this.words.addAll(words);
  }

  @Override
  public void addWord(Word word) {
    this.words.add(word);
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
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
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
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "TextLine(" + this.text + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof TextLine) {
      TextLine otherTextLine = (TextLine) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getCharacters(), otherTextLine.getCharacters());
      builder.append(getWords(), otherTextLine.getWords());
      builder.append(getText(), otherTextLine.getText());
      builder.append(getPosition(), otherTextLine.getPosition());
      builder.append(getBaseline(), otherTextLine.getBaseline());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getCharacters());
    builder.append(getWords());
    builder.append(getText());
    builder.append(getPosition());
    builder.append(getBaseline());
    return builder.hashCode();
  }
}
