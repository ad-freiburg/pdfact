package pdfact.core.model;

// TODO: Add some more properties to a figure.

/**
 * A figure in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Figure extends Element, HasPosition {
  /**
   * The factory to create instances of {@link Figure}.
   * 
   * @author Claudius Korzen
   */
  public interface FigureFactory {
    /**
     * Creates a new instance of {@link Figure}.
     * 
     * @return A new instance of {@link Figure}.
     */
    Figure create();
  }
}
