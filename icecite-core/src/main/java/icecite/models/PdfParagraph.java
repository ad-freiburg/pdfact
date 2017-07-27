package icecite.models;

/**
 * A text paragraph in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraph extends PdfElement, HasTextBlocks, HasText {
  /**
   * Returns the first text block.
   * 
   * @return The first text block or null if there are no text blocks.
   */
  PdfTextBlock getFirstTextBlock();

  /**
   * Returns the last text block.
   * 
   * @return The last text block or null if there are no text blocks.
   */
  PdfTextBlock getLastTextBlock();
  
  // ==========================================================================
  
  /**
   * Returns the first text line.
   * 
   * @return The first text line or null if there are no text lines.
   */
  PdfTextLine getFirstTextLine();

  /**
   * Returns the last text line.
   * 
   * @return The last text line or null if there are no text lines.
   */
  PdfTextLine getLastTextLine();
  
  // ==========================================================================
  
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
