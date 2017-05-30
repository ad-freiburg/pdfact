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
   * @return The first word or null if the list of words is empty.
   */
  PdfWord getFirstWord();
  
  /**
   * Returns the last word.
   * 
   * @return The last word or null if the list of words is empty.
   */
  PdfWord getLastWord();
  
  // ==========================================================================
  
  /**
   * Sets the words.
   * 
   * @param words
   *        The words to set.
   */
  void setWords(PdfWordList words);

  /**
   * Adds the words.
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
