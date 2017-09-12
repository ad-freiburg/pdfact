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
   * Returns the text lines of this element.
   * 
   * @return The text lines of this element.
   */
  TextLineList getTextLines();

  /**
   * Returns the first text line of this element.
   * 
   * @return The first text line or null if there are no text lines.
   */
  TextLine getFirstTextLine();

  /**
   * Returns the last text line of this element.
   * 
   * @return The last text line or null if there are no text lines.
   */
  TextLine getLastTextLine();

  // ==========================================================================

  /**
   * Sets the text lines of this element.
   * 
   * @param lines
   *        The text lines of this element.
   */
  void setTextLines(TextLineList lines);

  /**
   * Adds the given text lines to this element.
   * 
   * @param lines
   *        The text lines to add.
   */
  void addTextLines(TextLineList lines);

  /**
   * Adds the given text line to this element.
   * 
   * @param line
   *        The text line to add.
   */
  void addTextLine(TextLine line);
}