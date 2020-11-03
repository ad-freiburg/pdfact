package pdfact.core.model;

import pdfact.core.util.list.ElementList;

/**
 * An interface to implement by elements that consist of at least one paragraph.
 *
 * @author Claudius Korzen
 */
public interface HasParagraphs extends HasTextLineStatistic, HasCharacterStatistic {
  /**
   * Returns the paragraphs of this element.
   * 
   * @return The paragraphs of this element.
   */
  ElementList<Paragraph> getParagraphs();

  /**
   * Returns the first paragraph of this element.
   * 
   * @return The first paragraph or null if there are no paragraphs.
   */
  Paragraph getFirstParagraph();

  /**
   * Returns the last paragraph of this element.
   * 
   * @return The last paragraph or null if there are no paragraphs.
   */
  Paragraph getLastParagraph();

  // ==============================================================================================

  /**
   * Sets the paragraphs of this element.
   * 
   * @param paragraphs The paragraphs of this element.
   */
  void setParagraphs(ElementList<Paragraph> paragraphs);

  /**
   * Adds the given paragraphs to this element.
   * 
   * @param paragraphs The paragraphs to add.
   */
  void addParagraphs(ElementList<Paragraph> paragraphs);

  /**
   * Adds the given paragraph to this element.
   * 
   * @param paragraph The paragraph to add.
   */
  void addParagraph(Paragraph paragraph);
}
