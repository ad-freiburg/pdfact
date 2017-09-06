package pdfact.core.model;

import pdfact.core.util.list.TextLineList;

/**
 * An interface that is implemented by PDF elements that have text lines.
 *
 * @author Claudius Korzen
 */
public interface HasTextLines extends HasTextLineStatistic,
    HasCharacterStatistic {
  /**
   * Returns the text lines of the element.
   * 
   * @return The text lines.
   */
  TextLineList getTextLines();

  /**
   * Returns the first text line of the element.
   * 
   * @return The first text line.
   */
  TextLine getFirstTextLine();

  /**
   * Returns the last text line of the element.
   * 
   * @return The last text line.
   */
  TextLine getLastTextLine();

  // ==========================================================================

  /**
   * Sets the text lines of the element.
   * 
   * @param lines
   *        The text lines to set.
   */
  void setTextLines(TextLineList lines);

  /**
   * Adds the given text lines to the element.
   * 
   * @param lines
   *        The text lines to add.
   */
  void addTextLines(TextLineList lines);

  /**
   * Adds a text line to the element.
   * 
   * @param line
   *        The text line to add.
   */
  void addTextLine(TextLine line);
}