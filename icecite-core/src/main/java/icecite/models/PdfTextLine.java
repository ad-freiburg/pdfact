package icecite.models;

/**
 * A text line in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLine
    extends PdfElement, HasPage, HasCharacters, HasWords, HasText {
  /**
   * The factory to create instances of {@link PdfTextLine}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextLineFactory {
    /**
     * Creates a PdfTextLine.
     * 
     * @param page
     *        The page in which the lien is located.
     * @param characters
     *        The characters of the text line to create.
     * 
     * @return An instance of {@link PdfTextLine}.
     */
    PdfTextLine create(PdfPage page, PdfCharacterList characters);
  }
}
