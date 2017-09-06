package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have text blocks.
 *
 * @author Claudius Korzen
 */
public interface HasTextBlocks extends HasTextLineStatistic,
    HasCharacterStatistic {
  /**
   * Returns the text blocks of the element.
   * 
   * @return The text blocks.
   */
  List<TextBlock> getTextBlocks();

  /**
   * Returns the first text block of the element.
   * 
   * @return the first text block.
   */
  TextBlock getFirstTextBlock();

  /**
   * Returns the last text block of the element.
   * 
   * @return the last text block.
   */
  TextBlock getLastTextBlock();

  // ==========================================================================

  /**
   * Sets the text blocks of the element.
   * 
   * @param blocks
   *        The text blocks to set.
   */
  void setTextBlocks(List<TextBlock> blocks);

  /**
   * Adds the given text blocks to the element.
   * 
   * @param blocks
   *        The text blocks to add.
   */
  void addTextBlocks(List<TextBlock> blocks);

  /**
   * Adds a text block to the element.
   * 
   * @param block
   *        The text block to add.
   */
  void addTextBlock(TextBlock block);
}
