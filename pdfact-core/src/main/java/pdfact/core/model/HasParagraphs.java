package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have paragraphs.
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
  List<Paragraph> getParagraphs();

  /**
   * Returns the first paragraph of the element.
   * 
   * @return The first paragraph.
   */
  Paragraph getFirstParagraph();

  /**
   * Returns the last paragraph of the element.
   * 
   * @return The last paragraph.
   */
  Paragraph getLastParagraph();

  // ==========================================================================

  /**
   * Sets the paragraphs of the element.
   * 
   * @param paragraphs
   *        The paragraphs to set.
   */
  void setParagraphs(List<Paragraph> paragraphs);

  /**
   * Adds paragraphs to the element.
   * 
   * @param paragraphs
   *        The paragraphs to add.
   */
  void addParagraphs(List<Paragraph> paragraphs);

  /**
   * Adds a paragraph to the element.
   * 
   * @param paragraph
   *        The paragraph to add.
   */
  void addParagraph(Paragraph paragraph);
}
