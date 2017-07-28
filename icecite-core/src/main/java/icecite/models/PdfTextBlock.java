package icecite.models;

// TODO: Eliminate Interfaces HasCharacters and HasTextLines and add HasWords.

/**
 * A text block in a page of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlock extends PdfElement, HasRole, HasCharacters,
    HasTextLines, HasText {
  
  /**
   * Returns the parent paragraph, which this block is a member of (needed to 
   * decide whether this text block was already matched to a paragraph).
   * 
   * @return The parent paragraph.
   */
  PdfParagraph getParentPdfParagraph();
  
  /**
   * Sets the parent paragraph, which this block is a member of.
   * 
   * @param paragraph The parent paragraph.
   */
  void setParentPdfParagraph(PdfParagraph paragraph);

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
   * The factory to create instances of {@link PdfTextBlock}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextBlockFactory {
    /**
     * Creates a PdfTextBlock.
     * 
     * @return An instance of {@link PdfTextBlock}.
     */
    PdfTextBlock create();
  }
}
