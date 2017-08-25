package pdfact.model;

// TODO: Add some more properties to a shape.

/**
 * A shape in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface Shape extends Element, HasPosition, HasColor {
  /**
   * The factory to create instances of {@link Shape}.
   * 
   * @author Claudius Korzen
   */
  public interface ShapeFactory {
    /**
     * Creates a new instance of {@link Shape}.
     * 
     * @return A new instance of {@link Shape}.
     */
    Shape create();
  }
}
