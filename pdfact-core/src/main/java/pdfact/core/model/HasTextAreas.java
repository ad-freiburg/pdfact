package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have text areas.
 *
 * @author Claudius Korzen
 */
public interface HasTextAreas extends HasCharacterStatistic {
  /**
   * Returns the text areas of this element.
   * 
   * @return The text areas of this element.
   */
  List<TextArea> getTextAreas();

  /**
   * Returns the first text area of this element.
   * 
   * @return The first text area or null if there are no text areas.
   */
  TextArea getFirstTextArea();

  /**
   * Returns the last text area of this element.
   * 
   * @return The last text area or null if there are no text areas.
   */
  TextArea getLastTextArea();

  // ==========================================================================

  /**
   * Sets the text areas of this element.
   * 
   * @param areas
   *        The text areas of this element.
   */
  void setTextAreas(List<TextArea> areas);

  /**
   * Adds the given text areas to this element.
   * 
   * @param areas
   *        The text areas to add.
   */
  void addTextAreas(List<TextArea> areas);

  /**
   * Adds the given text area to this element.
   * 
   * @param area
   *        The text area to add.
   */
  void addTextArea(TextArea area);
}
