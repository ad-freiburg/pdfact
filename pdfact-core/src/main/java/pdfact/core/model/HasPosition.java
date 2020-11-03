package pdfact.core.model;

/**
 * An interface to implement by elements that have a fixed position in any page
 * of a document.
 *
 * @author Claudius Korzen
 */
public interface HasPosition {
  /**
   * Returns the position of this element.
   * 
   * @return The position of this element.
   */
  Position getPosition();

  /**
   * Sets the position of this element.
   * 
   * @param position The position of this element.
   */
  void setPosition(Position position);
}
