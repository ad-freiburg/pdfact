package icecite.models;

/**
 * An interface that declares that the implementing object consists of multiple
 * characters.
 *
 * @author Claudius Korzen
 */
public interface HasCharacters extends HasPdfElements<PdfCharacter> {
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
  
  // ==========================================================================
  
  /**
   * Returns the most common font over all characters.
   * 
   * @return The most common font over all characters.
   */
  PdfFont getMostCommonFont();
     
  /**
   * Returns the most common color over all characters.
   * 
   * @return The most common color over all characters.
   */
  PdfColor getMostCommonColor();
    
  /**
   * Returns the most common font size over all characters.
   * 
   * @return The most common font size over all characters.
   */
  float getMostCommonFontsize();
  
  /**
   * Returns the average font size over all characters.
   * 
   * @return The average font size over all characters.
   */
  float getAverageFontsize();
}
