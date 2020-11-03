package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface to implement by elements that consist of at least one text line.
 *
 * @author Claudius Korzen
 */
public interface HasTextLines extends HasTextLineStatistic, HasCharacterStatistic {
  /**
   * Returns the text lines of this element.
   * 
   * @return The text lines of this element.
   */
  ElementList<TextLine> getTextLines();

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

  // ==============================================================================================

  /**
   * Sets the text lines of this element.
   * 
   * @param lines The text lines of this element.
   */
  void setTextLines(ElementList<TextLine> lines);

  /**
   * Adds the given text lines to this element.
   * 
   * @param lines The text lines to add.
   */
  void addTextLines(ElementList<TextLine> lines);

  /**
   * Adds the given text line to this element.
   * 
   * @param line The text line to add.
   */
  void addTextLine(TextLine line);
}