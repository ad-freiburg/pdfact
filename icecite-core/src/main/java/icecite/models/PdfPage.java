package icecite.models;

import java.util.List;

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage extends HasCharacters, HasFigures, HasShapes {

  /**
   * Returns the number of this page in the PDF document.
   * 
   * @return The page number.
   */
  int getPageNumber();

  /**
   * Sets the number of this page in the PDF document.
   * 
   * @param pageNumber
   *        The page number.
   */
  void setPageNumber(int pageNumber);

  // ==========================================================================
  // TODO: Remove this getters and setters at any time.

  /**
   * Returns the identified text blocks in this page.
   * 
   * @return The list of text blocks in this page.
   */
  List<PdfTextBlock> getTextBlocks();

  /**
   * Sets the text blocks of this page.
   * 
   * @param blocks
   *        The list of text blocks to set.
   */
  void setTextBlocks(List<PdfTextBlock> blocks);

  /**
   * Returns the text lines of this page.
   * 
   * @return The text lines of this page.
   */
  List<PdfTextLine> getTextLines();

  /**
   * Sets the text lines of this page.
   * 
   * @param lines
   *        The list of text lines to set.
   */
  void setTextLines(List<PdfTextLine> lines);

  // ==========================================================================

  /**
   * Returns the identified paragraphs in this page.
   * 
   * @return The list of paragraphs in this page.
   */
  List<PdfParagraph> getParagraphs();

  /**
   * Sets the paragraphs of this page.
   * 
   * @param paragraphs
   *        The list of paragraphs to set.
   */
  void setParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds the given paragraph to this page.
   * 
   * @param paragraph
   *        The paragraph to add.
   */
  void addParagraph(PdfParagraph paragraph);

  // ==========================================================================

  /**
   * The factory to creates instances of {@link PdfPage}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfPageFactory {
    /**
     * Creates a new PDF page.
     * 
     * @return An instance of {@link PdfPage}.
     */
    PdfPage create();

    /**
     * Creates a new PDF page.
     * 
     * @param pageNum
     *        The number of the page in the PDF document.
     * 
     * @return An instance of {@link PdfPage}.
     */
    PdfPage create(int pageNum);
  }
}
