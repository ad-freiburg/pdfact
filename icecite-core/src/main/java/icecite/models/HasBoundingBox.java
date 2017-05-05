package icecite.models;

import icecite.utils.geometric.Rectangle;

/**
 * An interface that declares that the implementing object lives within a
 * bounding box.
 *
 * @author Claudius Korzen
 */
public interface HasBoundingBox {
  /**
   * Returns the bounding box of the implementing object.
   * 
   * @return The bounding box of the implementing object.
   */
  Rectangle getBoundingBox();

  /**
   * Sets the bounding box of the implementing object.
   * 
   * @param boundingBox
   *        The bounding box of the implementing object.
   */
  void setBoundingBox(Rectangle boundingBox);
}