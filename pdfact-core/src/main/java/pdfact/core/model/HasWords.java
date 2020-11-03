package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface to implement by elements that consist of at least one word.
 *
 * @author Claudius Korzen
 */
public interface HasWords extends HasCharacterStatistic {
  /**
   * Returns the words of this element.
   * 
   * @return The words of this element.
   */
  ElementList<Word> getWords();

  /**
   * Returns the first word of this element.
   * 
   * @return The first word or null if there are no words.
   */
  Word getFirstWord();

  /**
   * Returns the last word of this element.
   * 
   * @return The last word or null if there are no words.
   */
  Word getLastWord();

  // ==============================================================================================

  /**
   * Sets the words of this element.
   * 
   * @param words The words of this element.
   */
  void setWords(ElementList<Word> words);

  /**
   * Adds the given words to this element.
   * 
   * @param words The words to add.
   */
  void addWords(ElementList<Word> words);

  /**
   * Adds the given word to this element.
   * 
   * @param word The word to add.
   */
  void addWord(Word word);
}
