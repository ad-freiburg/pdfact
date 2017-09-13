package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface that is implemented by PDF elements that has characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters extends HasCharacterStatistic {
  /**
   * Returns the characters of this element.
   * 
   * @return The characters of this element.
   */
  ElementList<Character> getCharacters();

  /**
   * Returns the first character of this element.
   * 
   * @return The first character or null if there are no characters.
   */
  Character getFirstCharacter();

  /**
   * Returns the last character of this element.
   * 
   * @return The last character or null if there are no characters.
   */
  Character getLastCharacter();

  // ==========================================================================

  /**
   * Sets the characters of this element.
   * 
   * @param characters
   *        The characters to set.
   */
  void setCharacters(ElementList<Character> characters);

  /**
   * Adds the given characters to this element.
   * 
   * @param characters
   *        The characters to add.
   */
  void addCharacters(ElementList<Character> characters);

  /**
   * Adds the given character to this element.
   * 
   * @param character
   *        The character to add.
   */
  void addCharacter(Character character);
}
