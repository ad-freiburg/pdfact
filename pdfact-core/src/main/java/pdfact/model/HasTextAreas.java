package pdfact.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have text areas.
 *
 * @author Claudius Korzen
 */
public interface HasTextAreas extends HasCharacterStatistic {
  /**
   * Returns the text areas of the element.
   * 
   * @return The text areas.
   */
  List<TextArea> getTextAreas();

  /**
   * Returns the first text area of the element.
   * 
   * @return the first text area.
   */
  TextArea getFirstTextArea();

  /**
   * Returns the last text area of the element.
   * 
   * @return the last text area.
   */
  TextArea getLastTextArea();

  // ==========================================================================

  /**
   * Sets the text areas of the element.
   * 
   * @param areas
   *        The text areas to set.
   */
  void setTextAreas(List<TextArea> areas);

  /**
   * Adds the given text areas to the element.
   * 
   * @param areas
   *        The text areas to add.
   */
  void addTextAreas(List<TextArea> areas);

  /**
   * Adds the given text area to the element.
   * 
   * @param area
   *        The text area to add.
   */
  void addTextArea(TextArea area);
}
