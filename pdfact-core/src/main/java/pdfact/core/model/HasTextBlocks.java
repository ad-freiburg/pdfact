package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface that is implemented by PDF elements that have text blocks.
 *
 * @author Claudius Korzen
 */
public interface HasTextBlocks extends HasTextLineStatistic,
    HasCharacterStatistic {
  /**
   * Returns the text blocks of this element.
   * 
   * @return The text blocks of this element.
   */
  ElementList<TextBlock> getTextBlocks();

  /**
   * Returns the first text block of this element.
   * 
   * @return the first text block or null if there are no text lines.
   */
  TextBlock getFirstTextBlock();

  /**
   * Returns the last text block of this element.
   * 
   * @return the last text block or null if there are no text lines.
   */
  TextBlock getLastTextBlock();

  // ==========================================================================

  /**
   * Sets the text blocks of this element.
   * 
   * @param blocks
   *        The text blocks of this element.
   */
  void setTextBlocks(ElementList<TextBlock> blocks);

  /**
   * Adds the given text blocks to this element.
   * 
   * @param blocks
   *        The text blocks to add.
   */
  void addTextBlocks(ElementList<TextBlock> blocks);

  /**
   * Adds the given text block to this element.
   * 
   * @param block
   *        The text block to add.
   */
  void addTextBlock(TextBlock block);
}
