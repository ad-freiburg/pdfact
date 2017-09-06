package pdfact.core.model;

import pdfact.core.util.list.CharacterList;

/**
 * An interface that is implemented by PDF elements that consist of characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters extends HasCharacterStatistic {
  /**
   * Returns the characters.
   * 
   * @return The characters.
   */
  CharacterList getCharacters();

  /**
   * Returns the first character.
   * 
   * @return The first character.
   */
  Character getFirstCharacter();

  /**
   * Returns the last character.
   * 
   * @return The last character.
   */
  Character getLastCharacter();

  // ==========================================================================

  /**
   * Sets the characters of the element.
   * 
   * @param characters
   *        The characters to set.
   */
  void setCharacters(CharacterList characters);

  /**
   * Adds the given characters to the element.
   * 
   * @param characters
   *        The characters to add.
   */
  void addCharacters(CharacterList characters);

  /**
   * Adds the given character to the element.
   * 
   * @param character
   *        The character to add.
   */
  void addCharacter(Character character);
}
