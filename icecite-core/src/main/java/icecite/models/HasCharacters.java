package icecite.models;

/**
 * An interface that declares that the implementing object consists of multiple
 * characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters {
  /**
   * Returns the set of characters.
   * 
   * @return The set of characters.
   */
  PdfCharacterSet getCharacters();

  /**
   * Sets the characters.
   * 
   * @param characters
   *        The characters to set.
   */
  void setCharacters(PdfCharacterSet characters);
}
