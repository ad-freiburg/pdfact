package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has text blocks.
 *
 * @author Claudius Korzen
 */
public interface HasTextBlocks extends HasTextLineStatistics,
    HasCharacterStatistics {
  /**
   * Returns the text blocks.
   * 
   * @return The text blocks.
   */
  List<PdfTextBlock> getTextBlocks();

  /**
   * Returns the first text block.
   * 
   * @return the first text block.
   */
  PdfTextBlock getFirstTextBlock();

  /**
   * Returns the last text block.
   * 
   * @return the last text block.
   */
  PdfTextBlock getLastTextBlock();

  // ==========================================================================

  /**
   * Sets the given text blocks.
   * 
   * @param blocks
   *        The text blocks.
   */
  void setTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds the given text blocks.
   * 
   * @param blocks
   *        The text blocks.
   */
  void addTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds the given text block.
   * 
   * @param block
   *        The text block.
   */
  void addTextBlock(PdfTextBlock block);
}
