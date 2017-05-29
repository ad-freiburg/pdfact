package icecite.models;

import java.util.List;

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
  List<PdfShape> getShapes();

  /**
   * Sets the shapes.
   * 
   * @param shapes
   *        The shapes to set.
   */
  void setShapes(List<PdfShape> shapes);

  /**
   * Adds the given shapes.
   * 
   * @param shapes
   *        The shapes to add.
   */
  void addShapes(List<PdfShape> shapes);

  /**
   * Adds the given shape.
   * 
   * @param shape
   *        The shape to add.
   */
  void addShape(PdfShape shape);
}
