package icecite.models;

import java.util.List;
import java.util.Set;

/**
 * A list of PDF characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterList
    extends PdfElementList<PdfCharacter>, HasCharacters {
  /**
   * Returns the most common font of the characters in this list.
   * 
   * @return The most common font of the characters in this list.
   */
  PdfFont getMostCommonFont();

  /**
   * Returns all characters with the most common font.
   * 
   * @return All characters with the most common font.
   */
  Set<PdfCharacter> getCharactersWithMostCommonFont();

  // ==========================================================================

  /**
   * Returns the most common color of the characters in this list.
   * 
   * @return The most common color of the characters in this list.
   */
  PdfColor getMostCommonColor();

  /**
   * Returns all characters with the most common color.
   * 
   * @return All characters with the most common color.
   */
  Set<PdfCharacter> getCharactersWithMostCommonColor();

  // ==========================================================================

  /**
   * Returns the most common font size of the characters in this list.
   * 
   * @return The most common font size of the characters in this list.
   */
  float getMostCommonFontsize();

  /**
   * Returns all characters with the most common font size.
   * 
   * @return All characters with the most common font size.
   */
  Set<PdfCharacter> getCharactersWithMostCommonFontsize();

  /**
   * Returns the average font size of the characters in this list.
   * 
   * @return The average font size of the characters in this list.
   */
  float getAverageFontsize();

  // ==========================================================================

  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * @return A list of length 2, containing the two resulting views.
   */
  List<PdfCharacterList> cut(int splitIndex);

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