package icecite.models;

import java.util.List;

import icecite.utils.geometric.Rectangle;

/**
 * A word in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfWord extends PdfElement, HasCharacters, HasText {
  /**
   * Returns true, if this word is hyphenated.
   * 
   * @return True, if this word is hyphenated, false otherwise.
   */
  boolean isHyphenated();

  /**
   * Sets the boolean flag that indicates whether this word is hyphenated.
   * 
   * @param isHyphenated
   *        The boolean flag.
   */
  void setIsHyphenated(boolean isHyphenated);

  // ==========================================================================

  /**
   * Returns true, if this word was dehyphenated (this can only be the case if
   * the word is hyphenated).
   * 
   * @return True, if this word was dehyphenated, false otherwise.
   */
  boolean isDehyphenated();

  /**
   * Sets the boolean flag that indicates whether this word was dehyphenated.
   * 
   * @param isDehyphenated
   *        The boolean flag.
   */
  void setIsDehyphenated(boolean isDehyphenated);

  // ==========================================================================

  /**
   * Returns a list of the rectangles of the hyphenated syllables of this word.
   * In case of the word is not hyphenated, the list contains only a single
   * rectangle (the bounding box of this word).
   * 
   * @return The rectangles of the hyphenated syllables of this word.
   */
  List<Rectangle> getSyllableRectangles();

  /**
   * Sets the rectangles of the hyphenated syllables of this word.
   * 
   * @param rectangles
   *        The rectangles of the hyphenated syllables of this word.
   */
  void setSyllableRectangles(List<Rectangle> rectangles);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfWord}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfWordFactory {
    /**
     * Creates a PdfWord.
     * 
     * @param characters
     *        The characters of the word to create.
     * 
     * @return An instance of {@link PdfWord}.
     */
    PdfWord create(PdfCharacterList characters);
  }
}
