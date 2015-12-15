package drawer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;

/**
 * The interface for all pdf visualizers.
 *
 * @author Claudius Korzen
 */
public interface PdfDrawer {
  /**
   * Draws the given line on the given page.
   */
  void drawLine(Line line, int pageNum) throws IOException;
  
  /**
   * Draws the given line in the given color on the given page.
   */
  void drawLine(Line line, int pageNum, Color color) throws IOException;
  
  /**
   * Draws the given line with given color and given thickness on the given 
   * page.
   */
  void drawLine(Line line, int pageNum, Color color, float thickness) 
      throws IOException;
  
  // ___________________________________________________________________________
  
  /**
   * Draws the given rectangle on the given page.
   */
  void drawRectangle(Rectangle rectangle, int pageNum) throws IOException;
  
  /**
   * Draws the given rectangle in the given color on the given page.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color color) 
      throws IOException;
  
  /**
   * Draws the given rectangle with given color and given thickness on the given
   * page.
   */
  void drawRectangle(Rectangle rect, int pageNum, Color color, 
      float thickness) throws IOException;
  
  // ___________________________________________________________________________
  
  /**
   * Draws the given text on the given page.
   */
  void drawText(String text, int pageNum) throws IOException;
  
  /**
   * Draws the given text at the given position in the page.
   */
  void drawText(String text, int pageNum, Point pos) throws IOException;
  
  /**
   * Draws the given text in the given color at the given position in the page.
   */
  void drawText(String text, int pageNum, Point pos, Color color) 
      throws IOException;
  
  /**
   * Draws the given text in the given color and given fontsize at the given 
   * position in the page.
   */
  void drawText(String text, int pageNum, Point pos, Color color, 
      float fontsize) throws IOException;
  
  // ___________________________________________________________________________
  
  /**
   * Writes the visualization to the given output stream.
   */
  void writeTo(OutputStream stream) throws IOException;
}
