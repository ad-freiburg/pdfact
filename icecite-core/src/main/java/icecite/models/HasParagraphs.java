package icecite.models;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that consist of paragraphs.
 *
 * @author Claudius Korzen
 */
public interface HasParagraphs extends HasTextLineStatistic,
    HasCharacterStatistic {
  /**
   * Returns the paragraphs of the element.
   * 
   * @return The paragraphs.
   */
  List<PdfParagraph> getParagraphs();

  /**
   * Returns the first paragraph of the element.
   * 
   * @return The first paragraph.
   */
  PdfParagraph getFirstParagraph();

  /**
   * Returns the last paragraph of the element.
   * 
   * @return The last paragraph.
   */
  PdfParagraph getLastParagraph();

  // ==========================================================================

  /**
   * Sets the paragraphs of the element.
   * 
   * @param paragraphs
   *        The paragraphs to set.
   */
  void setParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds paragraphs to the element.
   * 
   * @param paragraphs
   *        The paragraphs to add.
   */
  void addParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds a paragraph to the element.
   * 
   * @param paragraph
   *        The paragraph to add.
   */
  void addParagraph(PdfParagraph paragraph);
}
