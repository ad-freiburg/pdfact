package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have shapes (like 
 * lines, curves, paths, etc.).
 *
 * @author Claudius Korzen
 */
public interface HasShapes {
  /**
   * Returns the shapes of the element.
   * 
   * @return The shapes.
   */
  List<Shape> getShapes();

  /**
   * Returns the first shape of the element.
   * 
   * @return The first shape.
   */
  Shape getFirstShape();

  /**
   * Returns the last shape of the element.
   * 
   * @return The last shape.
   */
  Shape getLastShape();

  // ==========================================================================

  /**
   * Sets the shapes of the element.
   * 
   * @param shapes
   *        The shapes to set.
   */
  void setShapes(List<Shape> shapes);

  /**
   * Adds the given shapes to the element.
   * 
   * @param shapes
   *        The shapes to add.
   */
  void addShapes(List<Shape> shapes);

  /**
   * Adds the given shape to the element.
   * 
   * @param shape
   *        The shape to add.
   */
  void addShape(Shape shape);
}
