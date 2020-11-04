package pdfact.cli.pipes.visualize;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import pdfact.core.model.HasRectangle;
import pdfact.core.model.Line;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;

/**
 * An interface to draw into a PDF file.
 *
 * @author Claudius Korzen
 */
public interface PdfDrawer {
  /**
   * Draws the given line on the given page.
   * 
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum) throws IOException;

  /**
   * Draws the given line in the given color on the given page.
   * 
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @param color
   *        The color to use.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum, Color color) throws IOException;

  /**
   * Draws the given line with given color and given thickness on the given
   * page.
   * 
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @param color
   *        The color to use.
   * @param thickness
   *        The thickness of the line.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum, Color color, float thickness)
      throws IOException;

  /**
   * Draws the given line on the given page.
   *
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the line should be
   *        considered as relative to the upper left vertex of the line.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws the given line in the given color on the given page.
   * 
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @param color
   *        The color to use.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the line should be
   *        considered as relative to the upper left vertex of the line.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum, Color color,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  /**
   * Draws the given line with given color and given thickness on the given
   * page.
   * 
   * @param line
   *        The line to draw.
   * @param pageNum
   *        The number of the page in which the line should be drawn.
   * @param color
   *        The color to use.
   * @param thickness
   *        The thickness of the line.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the line should be
   *        considered as relative to the upper left vertex of the line.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawLine(Line line, int pageNum, Color color, float thickness,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  // ==============================================================================================

  /**
   * Draws the given rectangle on the given page.
   * 
   * @param rectangle
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rectangle, int pageNum) throws IOException;

  /**
   * Draws the given rectangle in the given color on the given page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor) throws IOException;

  /**
   * Draws the given rectangle with given color and given thickness on the given
   * page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness) throws IOException;

  /**
   * Draws the given rectangle on the given page.
   * 
   * @param rectangle
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rectangle, int pageNum,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  /**
   * Draws the given rectangle in the given color on the given page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws the given rectangle with given color and given thickness on the given
   * page.
   *
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  // ==============================================================================================

  /**
   * Draws a bounding box on the given page.
   *
   * @param rectangle
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rectangle, int pageNum) throws IOException;

  /**
   * Draws a bounding box in the given color on the given page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor) throws IOException;

  /**
   * Draws a bounding box with given color and given thickness on the given
   * page.
   *
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness) throws IOException;

  /**
   * Draws a bounding box on the given page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rect, int pageNum,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  /**
   * Draws a bounding box in the given color on the given page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws a bounding box with given color and given thickness on the given
   * page.
   * 
   * @param rect
   *        The rectangle to draw.
   * @param pageNum
   *        The number of the page in which the rectangle should be drawn.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawBoundingBox(HasRectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  // ==============================================================================================

  /**
   * Draws the given position.
   *
   * @param position
   *        The position to draw.
   *
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position) throws IOException;

  /**
   * Draws the given position in the given color on the given page.
   * 
   * @param position
   *        The position to draw.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position, Color strokingColor,
      Color nonStrokingColor) throws IOException;

  /**
   * Draws the given position with given color and given thickness on the given
   * page.
   *
   * @param position
   *        The position to draw.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position, Color strokingColor,
      Color nonStrokingColor, float thickness) throws IOException;

  /**
   * Draws the given position on the given page.
   * 
   * @param position
   *        The position to draw.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws the given position in the given color on the given page.
   * 
   * @param position
   *        The position to draw.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position, Color strokingColor,
      Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws the given position with given color and given thickness on the given
   * page.
   * 
   * @param position
   *        The position to draw.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @param thickness
   *        The thickness of the border of the rectangle.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle should
   *        be considered as relative to the upper left vertex of the rectangle.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawPosition(Position position, Color strokingColor,
      Color nonStrokingColor, float thickness, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  // ==============================================================================================

  /**
   * Draws the given text on the given page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum) throws IOException;

  /**
   * Draws the given text at the given position in the page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos) throws IOException;

  /**
   * Draws the given text in the given color at the given position in the page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @param color
   *        The color of the text.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos, Color color)
      throws IOException;

  /**
   * Draws the given text in the given color and given fontsize at the given
   * position in the page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @param color
   *        The color of the text.
   * @param fontsize
   *        The font size of the text.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos, Color color,
      float fontsize) throws IOException;

  /**
   * Draws the given text on the given page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the text should be
   *        considered as relative to the upper left vertex.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException;

  /**
   * Draws the given text at the given position in the page.
   *
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the text should be
   *        considered as relative to the upper left vertex.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  /**
   * Draws the given text in the given color at the given position in the page.
   *
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @param color
   *        The color to use.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the text should be
   *        considered as relative to the upper left vertex.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos, Color color,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  /**
   * Draws the given text in the given color and given fontsize at the given
   * position in the page.
   * 
   * @param text
   *        The text to draw.
   * @param pageNum
   *        The number of the page in which the text should be drawn.
   * @param pos
   *        The position of the text.
   * @param color
   *        The color to use.
   * @param fontsize
   *        The font size of the text.
   * @param relativeToUpperLeft
   *        A flag to indicate whether the coordinates of the text should be
   *        considered as relative to the upper left vertex.
   * @param originInUpperLeft
   *        A flag to indicate whether the origin should be the upper left of
   *        the page.
   * @throws IOException
   *         If the drawing failed.
   */
  void drawText(String text, int pageNum, Point pos, Color color,
      float fontsize, boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException;

  // ==============================================================================================

  /**
   * Returns the drawn PDF file as byte array.
   * 
   * @return The drawn PDF file as byte array.
   * @throws IOException
   *         If getting the bytes has failed.
   */
  byte[] toByteArray() throws IOException;

  // ==============================================================================================

  /**
   * The factory to create instances of {@link PdfDrawer}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfDrawerFactory {
    /**
     * Creates a new PdfDrawer.
     * 
     * @param pdf
     *        The PDF file to process.
     * 
     * @return A new instance of {@link PdfDrawer}.
     */
    PdfDrawer create(File pdf);

    /**
     * Creates a new PdfDrawer.
     * 
     * @param pdf
     *        The PDF file to process.
     * 
     * @return A new instance of {@link PdfDrawer}.
     */
    PdfDrawer create(Path pdf);
  }
}
