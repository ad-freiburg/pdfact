package model;

import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;

/**
 * Interface that represents an arbitrary area of a page.
 *
 * @author Claudius Korzen
 */
public interface PdfArea extends HasPdfDocument, HasPdfPage, HasRectangle, 
    HasDimensionStatistics {    
  /**
   * Returns all objects within this area.
   */
  List<? extends HasRectangle> getElements();
  
  /**
   * Returns the objects which are contained by the given rectangle.
   */
  List<? extends HasRectangle> getElementsContainedBy(Rectangle rect);
  
  /**
   * Returns the objects which are overlapped by the given rectangle.
   */
  List<? extends HasRectangle> getElementsOverlappedBy(Rectangle rect);
}
