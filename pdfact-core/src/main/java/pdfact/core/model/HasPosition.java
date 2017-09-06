package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have a fixed position 
 * in any page of a PDF document.
 *
 * @author Claudius Korzen
 */
public interface HasPosition {
  /**
   * Returns the position of the element.
   * 
   * @return The position.
   */
  Position getPosition();

  /**
   * Sets the position of the element.
   * 
   * @param position
   *        The position.
   */
  void setPosition(Position position);
}
