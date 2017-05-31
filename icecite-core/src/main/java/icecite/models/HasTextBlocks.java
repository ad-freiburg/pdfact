package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has text blocks.
 *
 * @author Claudius Korzen
 */
public interface HasTextBlocks {
  /**
   * Returns the text blocks.
   * 
   * @return The text blocks.
   */
  List<PdfTextBlock> getTextBlocks();

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
