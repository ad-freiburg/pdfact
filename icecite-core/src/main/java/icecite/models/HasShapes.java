package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has shapes.
 *
 * @author Claudius Korzen
 */
public interface HasShapes {
  /**
   * Returns the shapes.
   * 
   * @return The shapes.
   */
  List<PdfShape> getShapes();

  /**
   * Sets the given shapes.
   * 
   * @param shapes
   *        The shapes.
   */
  void setShapes(List<PdfShape> shapes);

  /**
   * Adds the given shapes.
   * 
   * @param shapes
   *        The shapes.
   */
  void addShapes(List<PdfShape> shapes);

  /**
   * Adds the given shape.
   * 
   * @param shape
   *        The shape.
   */
  void addShape(PdfShape shape);
}
