package pdfact.models;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that consist of text blocks.
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
  List<PdfTextBlock> getTextBlocks();

  /**
   * Returns the first text block of the element.
   * 
   * @return the first text block.
   */
  PdfTextBlock getFirstTextBlock();

  /**
   * Returns the last text block of the element.
   * 
   * @return the last text block.
   */
  PdfTextBlock getLastTextBlock();

  // ==========================================================================

  /**
   * Sets the text blocks of the element.
   * 
   * @param blocks
   *        The text blocks to set.
   */
  void setTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds text blocks to the element.
   * 
   * @param blocks
   *        The text blocks to add.
   */
  void addTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds a text block to the element.
   * 
   * @param block
   *        The text block to add.
   */
  void addTextBlock(PdfTextBlock block);
}
