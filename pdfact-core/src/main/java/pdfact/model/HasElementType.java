package pdfact.model;

/**
 * An interface that is implemented by PDF elements that have a type.
 *
 * @author Claudius Korzen
 */
public interface HasElementType {
  /**
   * Returns the type of the element.
   * 
   * @return The type of the element.
   */
  ElementType getType();
}
