package pdfact.models;

/**
 * A single character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacter extends PdfSinglePositionElement, HasColor, 
    HasFontFace, HasText {
  /**
   * Returns the sequence number of this character - that is the rank in the
   * extraction order of PDF elements.
   *
   * @return The sequence number.
   */
  int getSequenceNumber();

  /**
   * Sets the sequence number of this character.
   *
   * @param num
   *        The sequence number.
   */
  void setSequenceNumber(int num);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfCharacter}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfCharacterFactory {
    /**
     * Creates a new PdfCharacter.
     * 
     * @return An instance of {@link PdfCharacter}.
     */
    PdfCharacter create();
  }
}
