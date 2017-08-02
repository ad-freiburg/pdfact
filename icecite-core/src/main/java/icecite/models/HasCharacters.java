package icecite.models;

/**
 * An interface that declares that the implementing object has characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters extends HasCharacterStatistics {
  /**
   * Returns the list of characters.
   * 
   * @return The list of characters.
   */
  PdfCharacterList getCharacters();

  /**
   * Returns the first character.
   * 
   * @return the first character.
   */
  PdfCharacter getFirstCharacter();

  /**
   * Returns the last character.
   * 
   * @return the last character.
   */
  PdfCharacter getLastCharacter();

  // ==========================================================================

  /**
   * Sets the given characters.
   * 
   * @param characters
   *        The characters to set.
   */
  void setCharacters(PdfCharacterList characters);

  /**
   * Adds the given characters.
   * 
   * @param characters
   *        The characters to add.
   */
  void addCharacters(PdfCharacterList characters);

  /**
   * Adds the given character.
   * 
   * @param character
   *        The character to add.
   */
  void addCharacter(PdfCharacter character);
}
