package pdfact.models;

import pdfact.utils.geometric.HasRectangle;

/**
 * An interface that is implemented by PDF elements that have a position in a
 * page of a PDF document.
 *
 * @author Claudius Korzen
 */
public interface HasPosition extends HasRectangle {
  /**
   * Returns the position of the element.
   * 
   * @return The position.
   */
  PdfPosition getPosition();

  /**
   * Sets the position of the element.
   * 
   * @param position
   *        The position.
   */
  void setPosition(PdfPosition position);
}
