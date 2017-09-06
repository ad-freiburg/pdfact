package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that may have positions on
 * multiple pages in a PDF document (like paragraphs or words).
 * 
 * @author Claudius Korzen
 */
public interface HasPositions {
  /**
   * Returns the list of positions of the element.
   * 
   * @return The list of positions.
   */
  List<Position> getPositions();

  /**
   * Returns the first position of the element.
   * 
   * @return The first position.
   */
  Position getFirstPosition();
  
  /**
   * Returns the last position of the element.
   * 
   * @return The last position.
   */
  Position getLastPosition();
  
  // ==========================================================================
  
  /**
   * Sets the positions of the element.
   * 
   * @param positions
   *        The list positions.
   */
  void setPositions(List<Position> positions);

  /**
   * Adds the given positions to the element.
   * 
   * @param positions
   *        The list of positions.
   */
  void addPositions(List<Position> positions);

  /**
   * Adds the given position to the element.
   * 
   * @param position
   *        The position to add.
   */
  void addPosition(Position position);
}
