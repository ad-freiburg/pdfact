package icecite.models;

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
  PdfCharacterList getCharacters();

  /**
   * Returns the first character.
   * 
   * @return The first character.
   */
  PdfCharacter getFirstCharacter();

  /**
   * Returns the last character.
   * 
   * @return The last character.
   */
  PdfCharacter getLastCharacter();

  // ==========================================================================

  /**
   * Sets the characters of the element.
   * 
   * @param characters
   *        The characters to set.
   */
  void setCharacters(PdfCharacterList characters);

  /**
   * Adds characters to the element.
   * 
   * @param characters
   *        The characters to add.
   */
  void addCharacters(PdfCharacterList characters);

  /**
   * Adds the given character to the element.
   * 
   * @param character
   *        The character to add.
   */
  void addCharacter(PdfCharacter character);
}
