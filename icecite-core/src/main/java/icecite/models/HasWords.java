package icecite.models;

/**
 * An interface that declares that the implementing object has words.
 *
 * @author Claudius Korzen
 */
public interface HasWords {
  /**
   * Returns the words.
   * 
   * @return The words.
   */
  PdfWordList getWords();

  /**
   * Returns the first word.
   * 
   * @return The first word or null if there are no words.
   */
  PdfWord getFirstWord();

  /**
   * Returns the last word.
   * 
   * @return The last word or null if there are no words.
   */
  PdfWord getLastWord();

  // ==========================================================================

  /**
   * Sets the given words.
   * 
   * @param words
   *        The words.
   */
  void setWords(PdfWordList words);

  /**
   * Adds the given words.
   * 
   * @param words
   *        The words.
   */
  void addWords(PdfWordList words);

  /**
   * Adds the given word.
   * 
   * @param word
   *        The word.
   */
  void addWord(PdfWord word);
}
