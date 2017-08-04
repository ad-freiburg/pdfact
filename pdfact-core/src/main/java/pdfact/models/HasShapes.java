package pdfact.models;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that consist of shapes 
 * (lines, curves, paths, etc.).
 *
 * @author Claudius Korzen
 */
public interface HasShapes {
  /**
   * Returns the shapes of the element.
   * 
   * @return The shapes.
   */
  List<PdfShape> getShapes();

  /**
   * Returns the first shape of the element.
   * 
   * @return The first shape.
   */
  PdfShape getFirstShape();

  /**
   * Returns the last shape of the element.
   * 
   * @return The last shape.
   */
  PdfShape getLastShape();

  // ==========================================================================

  /**
   * Sets the shapes of the element.
   * 
   * @param shapes
   *        The shapes to set.
   */
  void setShapes(List<PdfShape> shapes);

  /**
   * Adds shapes to the element.
   * 
   * @param shapes
   *        The shapes to add.
   */
  void addShapes(List<PdfShape> shapes);

  /**
   * Adds the given shape to the element.
   * 
   * @param shape
   *        The shape to add.
   */
  void addShape(PdfShape shape);
}
