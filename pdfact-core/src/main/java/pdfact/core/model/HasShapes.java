package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface to implement by elements that consist of at least on shapes
 * (e.g., lines, curves, paths, etc.).
 *
 * @author Claudius Korzen
 */
public interface HasShapes {
  /**
   * Returns the shapes of this element.
   * 
   * @return The shapes of this element.
   */
  ElementList<Shape> getShapes();

  /**
   * Returns the first shape of this element.
   * 
   * @return The first shape or null if there are no shapes.
   */
  Shape getFirstShape();

  /**
   * Returns the last shape of this element.
   * 
   * @return The last shape or null if there are no shapes.
   */
  Shape getLastShape();

  // ==============================================================================================

  /**
   * Sets the shapes of this element.
   * 
   * @param shapes The shapes of this element.
   */
  void setShapes(ElementList<Shape> shapes);

  /**
   * Adds the given shapes to this element.
   * 
   * @param shapes The shapes to add.
   */
  void addShapes(ElementList<Shape> shapes);

  /**
   * Adds the given shape to this element.
   * 
   * @param shape The shape to add.
   */
  void addShape(Shape shape);
}
