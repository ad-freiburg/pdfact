package icecite.models;

import java.util.Set;

/**
 * An interface that declares that the implementing object has multiple shapes.
 *
 * @author Claudius Korzen
 */
public interface HasShapes {
  /**
   * Returns the set of shapes.
   * 
   * @return The set of shapes.
   */
  Set<PdfShape> getShapes();

  /**
   * Sets the shapes.
   * 
   * @param shapes
   *        The shapes to set.
   */
  void setShapes(Set<PdfShape> shapes);
  
  /**
   * Adds the given shape.
   * 
   * @param shape
   *        The shape to add.
   */
  void addShape(PdfShape shape);
}
