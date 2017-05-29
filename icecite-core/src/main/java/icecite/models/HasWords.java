package icecite.models;

import java.util.List;

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
  List<PdfWord> getWords();

  /**
   * Sets the words.
   * 
   * @param words
   *        The words to set.
   */
  void setWords(List<PdfWord> words);

  /**
   * Adds the words.
   * 
   * @param words
   *        The words to add.
   */
  void addWords(List<PdfWord> words);

  /**
   * Adds the given word.
   * 
   * @param word
   *        The word to add.
   */
  void addWord(PdfWord word);
}
