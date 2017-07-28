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
