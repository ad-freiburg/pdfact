package icecite.models;

import java.util.Set;

/**
 * A list of PDF characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterList extends PdfElementList<PdfCharacter> {
  /**
   * Returns the most common font over all characters in this set.
   * 
   * @return The most common font over all characters in this set.
   */
  PdfFont getMostCommonFont();

  /**
   * Returns all elements with the most common font.
   * 
   * @return All elements with the most common font.
   */
  Set<PdfCharacter> getCharactersWithMostCommonFont();
  
  // ==========================================================================
  
  /**
   * Returns the most common color over all characters in this set.
   * 
   * @return The most common color over all characters in this set.
   */
  PdfColor getMostCommonColor();

  /**
   * Returns all elements with the most common color.
   * 
   * @return All elements with the most common color.
   */
  Set<PdfCharacter> getCharactersWithMostCommonColor();
  
  // ==========================================================================

  /**
   * Returns the most common font size over all characters in this set.
   * 
   * @return The most common font size over all characters in this set.
   */
  float getMostCommonFontsize();

  /**
   * Returns all elements with the most common font size.
   * 
   * @return All elements with the most common font size.
   */
  Set<PdfCharacter> getCharactersWithMostCommonFontsize();
  
  /**
   * Returns the average font size over all characters in this set.
   * 
   * @return The average font size over all characters in this set.
   */
  float getAverageFontsize();

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfCharacterList}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterListFactory {
    /**
     * Creates a PdfCharacterList.
     * 
     * @return An instance of {@link PdfCharacterList}.
     */
    PdfCharacterList create();

    /**
     * Creates a PdfCharacterList.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return An instance of {@link PdfCharacterList}.
     */
    PdfCharacterList create(int initialCapacity);
  }
}