package pdfact.core.model;

/**
 * A text block in a page of a PDF document. In principal, a text block is equal
 * to a paragraph, if a paragraph spans only a single page. In contrast, if a
 * paragraph spans two pages, the paragraph consists of two text blocks.
 * 
 * @author Claudius Korzen
 */
public interface TextBlock extends Element, HasTextLines, HasText, HasPosition,
    HasSemanticRole {
  /**
   * The factory to create instances of {@link TextBlock}.
   * 
   * @author Claudius Korzen
   */
  public interface TextBlockFactory {
    /**
     * Creates a new instance of {@link TextBlock}.
     * 
     * @return A new instance of {@link TextBlock}.
     */
    TextBlock create();
  }
}
