package pdfact.core.util.normalize;

import gnu.trove.set.TCharSet;
import pdfact.core.model.Word;

/**
 * A word normalizer.
 * 
 * @author Claudius Korzen
 */
public interface WordNormalizer {
  /**
   * Normalizes the given word.
   * 
   * @param word
   *        The word to normalize.
   * 
   * @return The normalized string.
   */
  String normalize(Word word);

  // ==========================================================================

  /**
   * Sets the flag that indicates whether words should be transformed to lower
   * cases or not.
   * 
   * @param toLowerCase
   *        The flag to set.
   */
  void setIsToLowerCase(boolean toLowerCase);

  /**
   * Returns the flag that indicates whether words should be transformed to
   * lower cases or not.
   * 
   * @return True if words should be transformed to lower cases; False
   *         otherwise.
   */
  boolean isToLowerCase();

  // ==========================================================================

  /**
   * Sets the characters to keep on removing leading characters. All leading
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters
   *        The characters to keep.
   */
  void setLeadingCharactersToKeep(TCharSet... characters);

  /**
   * Sets the characters to keep on removing leading characters. All leading
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters
   *        The characters to keep.
   */
  void setLeadingCharactersToKeep(char... characters);

  /**
   * Returns the characters to keep on removing leading characters.
   * 
   * @return The set of characters to keep on removing leading characters.
   */
  TCharSet getLeadingCharactersToKeep();
  
  // ==========================================================================

  /**
   * Sets the characters to keep on removing trailing characters. All trailing
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters
   *        The characters to keep.
   */
  void setTrailingCharactersToKeep(TCharSet... characters);

  /**
   * Sets the characters to keep on removing trailing characters. All trailing
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters
   *        The characters to keep.
   */
  void setTrailingCharactersToKeep(char... characters);

  /**
   * Returns the characters to keep on removing trailing characters.
   * 
   * @return The set of characters to keep on removing trailing characters.
   */
  TCharSet getTrailingCharactersToKeep();

  // ==========================================================================

  /**
   * The factory to create instances of {@link WordNormalizer}.
   * 
   * @author Claudius Korzen
   */
  public interface WordNormalizerFactory {
    /**
     * Creates a new instance of {@link WordNormalizer}.
     * 
     * @return A new instance of {@link WordNormalizer}.
     */
    WordNormalizer create();
  }
}
