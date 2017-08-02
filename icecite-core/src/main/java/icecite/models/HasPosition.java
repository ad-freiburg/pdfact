package icecite.models;

import icecite.utils.geometric.HasRectangle;

/**
 * An interface that declares that the implementing object has a position in a
 * PDF document.
 *
 * @author Claudius Korzen
 */
public interface HasPosition extends HasRectangle {
  /**
   * Returns the position.
   * 
   * @return The position.
   */
  PdfPosition getPosition();

  /**
   * Sets the given position.
   * 
   * @param position
   *        The position.
   */
  void setPosition(PdfPosition position);
}
