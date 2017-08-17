package pdfact.models;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that may have multiple
 * positions within a PDF document (such as paragraphs).
 * 
 * @author Claudius Korzen
 */
public interface HasPositions {
  /**
   * Returns the list of positions of the element.
   * 
   * @return The list of positions.
   */
  List<PdfPosition> getPositions();

  /**
   * Sets the position of the element.
   * 
   * @param positions
   *        The list positions.
   */
  void setPositions(List<PdfPosition> positions);

  /**
   * Adds the given positions to the element.
   * 
   * @param positions
   *        The list of positions.
   */
  void addPositions(List<PdfPosition> positions);

  /**
   * Adds the given position to the element.
   * 
   * @param position
   *        The position to add.
   */
  void addPosition(PdfPosition position);
}
