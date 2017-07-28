package icecite.models;

// TODO: Move the getFirst* and getLast* methods to the related interfaces.

/**
 * A text paragraph in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraph extends PdfElement, HasRole, HasWords, HasText {
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
   * Returns the first character.
   * 
   * @return the first character.
   */
  PdfCharacter getFirstCharacter();
  
  /**
   * Returns the last character.
   * 
   * @return the last character.
   */
  PdfCharacter getLastCharacter();
  
  // ==========================================================================
  
  /**
   * The factory to create instances of {@link PdfParagraph}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfParagraphFactory {
    /**
     * Creates an empty PdfParagraph.
     * 
     * @return An instance of {@link PdfParagraph}.
     */
    PdfParagraph create();
  }
}
