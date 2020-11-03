package pdfact.core.model;

import java.util.List;

/**
 * An interface to implement by elements (like paragraphs or words) that have
 * multiple positions (e.g., on different text lines, columns or pages) in a
 * document.
 * 
 * @author Claudius Korzen
 */
public interface HasPositions {
  /**
   * Returns the list of positions of this element.
   * 
   * @return The list of positions of this element.
   */
  List<Position> getPositions();

  /**
   * Returns the first position of this element.
   * 
   * @return The first position or null if there are no positions.
   */
  Position getFirstPosition();

  /**
   * Returns the last position of this element.
   * 
   * @return The last position or null if there are no positions.
   */
  Position getLastPosition();

  // ==============================================================================================

  /**
   * Sets the positions of this element.
   * 
   * @param positions The list of positions of this element.
   */
  void setPositions(List<Position> positions);

  /**
   * Adds the given positions to this element.
   * 
   * @param positions The list of positions to add.
   */
  void addPositions(List<Position> positions);

  /**
   * Adds the given position to this element.
   * 
   * @param position The position to add.
   */
  void addPosition(Position position);
}
