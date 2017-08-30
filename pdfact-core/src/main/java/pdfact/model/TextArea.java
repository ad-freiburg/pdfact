package pdfact.model;

/**
 * A text area in a PDF document. A text area has no strict definition. Mainly,
 * a text area is used to group and to separate characters from different
 * columns.
 * 
 * @author Claudius Korzen
 */
public interface TextArea extends Element, HasCharacters, HasPosition {
  /**
   * The factory to create instances of {@link TextArea}.
   * 
   * @author Claudius Korzen
   */
  public interface TextAreaFactory {
    /**
     * Creates a new instance of {@link TextArea}.
     * 
     * @return A new instance of {@link TextArea}.
     */
    TextArea create();
  }
}
