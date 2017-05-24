package icecite.models;

/**
 * Any text block with no special definition.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlock extends PdfElement, HasCharacters {
  /**
   * The factory to create instances of {@link PdfTextBlock}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextBlockFactory {
    /**
     * Creates a PdfTextBlock.
     * 
     * @param page
     *        The page of in which this text block is located. 
     * @param characters
     *        The characters of this text block.
     * @return An instance of {@link PdfTextBlock}.
     */
    PdfTextBlock create(PdfPage page, PdfCharacterList characters);
  }
}
