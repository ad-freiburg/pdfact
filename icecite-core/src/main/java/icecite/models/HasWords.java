package icecite.models;

/**
 * An interface that is implemented by PDF elements that consist of words.
 *
 * @author Claudius Korzen
 */
public interface HasWords extends HasCharacterStatistic {
  /**
   * Returns the words of the element.
   * 
   * @return The words.
   */
  PdfWordList getWords();

  /**
   * Returns the first word of the element.
   * 
   * @return The first word.
   */
  PdfWord getFirstWord();

  /**
   * Returns the last word of the element.
   * 
   * @return The last word.
   */
  PdfWord getLastWord();

  // ==========================================================================

  /**
   * Sets the words of the element.
   * 
   * @param words
   *        The words to set.
   */
  void setWords(PdfWordList words);

  /**
   * Adds words to the element.
   * 
   * @param words
   *        The words to add.
   */
  void addWords(PdfWordList words);

  /**
   * Adds the given word.
   * 
   * @param word
   *        The word to add.
   */
  void addWord(PdfWord word);
}
