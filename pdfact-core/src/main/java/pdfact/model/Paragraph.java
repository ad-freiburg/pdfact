package pdfact.model;

/**
 * A text paragraph in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Paragraph extends Element, HasWords, HasText, HasPositions,
    HasSemanticRole {
  /**
   * The factory to create instances of {@link Paragraph}.
   * 
   * @author Claudius Korzen
   */
  public interface ParagraphFactory {
    /**
     * Creates a new instance of {@link Paragraph}.
     * 
     * @return A new instance of {@link Paragraph}.
     */
    Paragraph create();
  }
}
