package icecite.models;

import java.util.Collection;
import java.util.Set;

/**
 * A set of PDF characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterSet extends PdfElementSet<PdfCharacter> {
  /**
   * Returns the most common font over all characters in this set.
   * 
   * @return The most common font over all characters in this set.
   */
  PdfFont getMostCommonFont();

  /**
   * Returns the most common color over all characters in this set.
   * 
   * @return The most common color over all characters in this set.
   */
  PdfColor getMostCommonColor();

  // ==========================================================================

  /**
   * Returns the most common font size over all characters in this set.
   * 
   * @return The most common font size over all characters in this set.
   */
  float getMostCommonFontsize();

  /**
   * Returns the average font size over all characters in this set.
   * 
   * @return The average font size over all characters in this set.
   */
  float getAverageFontsize();

  // ==========================================================================

  /**
   * Returns all characters with the minimal minX value in this set.
   *
   * @return All characters with the minimal minX value in this set.
   */
  Set<PdfCharacter> getLeftMostCharacters();

  /**
   * Returns all characters with the maximal maxX value in this set.
   *
   * @return All characters with the maximal maxX value in this set.
   */
  Set<PdfCharacter> getRightMostCharacters();

  /**
   * Returns all characters with the minimal minY value in this set.
   *
   * @return All characters with the minimal minY value in this set.
   */
  Set<PdfCharacter> getLowerMostCharacters();

  /**
   * Returns all characters with the maximal maxY value in this set.
   *
   * @return All characters with the maximal maxY value in this set.
   */
  Set<PdfCharacter> getUpperMostCharacters();

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfCharacterSet}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterSetFactory {
    /**
     * Creates a PdfCharactersSet.
     * 
     * @return An instance of {@link PdfCharacterSet}.
     */
    PdfCharacterSet create();

    /**
     * Creates a PdfCharactersSet.
     * 
     * @param characters
     *        The characters of the set.
     * 
     * @return An instance of {@link PdfCharacterSet}.
     */
    PdfCharacterSet create(Collection<PdfCharacter> characters);
  }
}
