package icecite.models;

/**
 * An interface that declares that the implementing object consists of multiple
 * characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters {
  /**
   * Returns the list of characters.
   * 
   * @return The list of characters.
   */
  PdfCharacterList getCharacters();

  /**
   * Sets the characters.
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
