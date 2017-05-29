package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has text blocks.
 *
 * @author Claudius Korzen
 */
public interface HasTextBlocks {
  /**
   * Returns the identified text blocks.
   * 
   * @return The list of text blocks.
   */
  List<PdfTextBlock> getTextBlocks();

  /**
   * Sets the text blocks.
   * 
   * @param blocks
   *        The list of text blocks to set.
   */
  void setTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds the given text blocks.
   * 
   * @param blocks
   *        The list of text blocks to add.
   */
  void addTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Adds the given text block.
   * 
   * @param block
   *        The text block to add.
   */
  void addTextBlock(PdfTextBlock block);
}
