package icecite.models;

import java.util.List;

/**
 * A text line in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLine extends PdfElement, HasCharacters, HasText {  
  /**
   * Returns the words of this text line.
   * 
   * @return The words of this text line.
   */
  List<PdfWord> getWords();

  /**
   * Sets the words of this text line.
   * 
   * @param words
   *        The words to set.
   */
  void setWords(List<PdfWord> words);

  /**
   * Adds the given word to this text line.
   * 
   * @param word
   *        The word to add.
   */
  void addWord(PdfWord word);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfTextLine}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineFactory {
    /**
     * Creates a PdfTextLine.
     * 
     * @param characters
     *        The characters of the text line to create.
     * 
     * @return An instance of {@link PdfTextLine}.
     */
    PdfTextLine create(PdfCharacterSet characters);
  }
}
