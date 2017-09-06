package pdfact.core.model;

import pdfact.core.util.list.WordList;

/**
 * An interface that is implemented by PDF elements that have words.
 *
 * @author Claudius Korzen
 */
public interface HasWords extends HasCharacterStatistic {
  /**
   * Returns the words of the element.
   * 
   * @return The words.
   */
  WordList getWords();

  /**
   * Returns the first word of the element.
   * 
   * @return The first word.
   */
  Word getFirstWord();

  /**
   * Returns the last word of the element.
   * 
   * @return The last word.
   */
  Word getLastWord();

  // ==========================================================================

  /**
   * Sets the words of the element.
   * 
   * @param words
   *        The words to set.
   */
  void setWords(WordList words);

  /**
   * Adds the given words to the element.
   * 
   * @param words
   *        The words to add.
   */
  void addWords(WordList words);

  /**
   * Adds the given word.
   * 
   * @param word
   *        The word to add.
   */
  void addWord(Word word);
}
