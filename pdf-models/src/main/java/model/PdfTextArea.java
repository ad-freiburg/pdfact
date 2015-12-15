package model;

import java.util.List;

import de.freiburg.iif.model.Rectangle;

/**
 * Interface that represents an arbitrary text area of a page.
 *
 * @author Claudius Korzen
 */
public interface PdfTextArea extends PdfArea, HasText, HasTextStatistics {
  /**
   * Returns the objects which are contained by the given rectangle.
   */
  public List<PdfCharacter> getCharactersContainedBy(Rectangle rect);

  /**
   * Returns the objects which are overlapped by the given rectangle.
   */
  public List<PdfCharacter> getCharactersOverlappedBy(Rectangle rect);
}
