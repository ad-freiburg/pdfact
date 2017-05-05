package icecite.utils.geometric;

import com.google.inject.assistedinject.Assisted;

/**
 * The factory to create instances of {@link Rectangle}.
 * 
 * @author Claudius Korzen
 */
public interface RectangleFactory {
  /**
   * Creates a new Rectangle.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create();

  /**
   * Creates a new Rectangle from given rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create(Rectangle rect);

  /**
   * Creates a new Rectangle from given rectangle.
   * 
   * @param rect
   *        The rectangle to copy.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create(java.awt.Rectangle rect);

  /**
   * Creates a new Rectangle.
   * 
   * @param point1
   *        The lower left vertex.
   * @param point2
   *        The upper right vertex.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create(@Assisted("point1") Point point1,
      @Assisted("point2") Point point2);

  /**
   * Creates a new Rectangle.
   * 
   * @param minX
   *        The x value of the lower left vertex.
   * @param minY
   *        The y value of the lower left vertex.
   * @param maxX
   *        The x value of the upper right vertex.
   * @param maxY
   *        The y value of the upper right vertex.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create(@Assisted("minX") double minX,
      @Assisted("minY") double minY, @Assisted("maxX") double maxX,
      @Assisted("maxY") double maxY);

  /**
   * Creates a new Rectangle.
   * 
   * @param minX
   *        The x value of the lower left vertex.
   * @param minY
   *        The y value of the lower left vertex.
   * @param maxX
   *        The x value of the upper right vertex.
   * @param maxY
   *        The y value of the upper right vertex.
   * 
   * @return An instance of Rectangle.
   */
  Rectangle create(@Assisted("minX") float minX, @Assisted("minY") float minY,
      @Assisted("maxX") float maxX, @Assisted("maxY") float maxY);
}
