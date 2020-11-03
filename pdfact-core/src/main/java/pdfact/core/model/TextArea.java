package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * A text area in a document. A text area has no strict definition. Mainly,
 * a text area is used to group and to separate characters from different
 * columns.
 * 
 * @author Claudius Korzen
 */
public class TextArea extends Element implements HasCharacters, HasPosition {
  /**
   * The characters of this text area.
   */
  protected ElementList<Character> characters;

  /**
   * The position of this text area.
   */
  protected Position position;

  /**
   * The statistic about the characters in this area.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * The default constructor.
   */
  public TextArea() {
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
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==============================================================================================

  @Override
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }
}
