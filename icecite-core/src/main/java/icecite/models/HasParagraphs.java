package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has paragraphs.
 *
 * @author Claudius Korzen
 */
public interface HasParagraphs
    extends HasTextLineStatistics, HasCharacterStatistics {
  /**
   * Returns the paragraphs.
   * 
   * @return The paragraphs.
   */
  List<PdfParagraph> getParagraphs();

  /**
   * Returns the first paragraph.
   * 
   * @return The first paragraph.
   */
  PdfParagraph getFirstParagraph();

  /**
   * Returns the last paragraph.
   * 
   * @return the last paragraph.
   */
  PdfParagraph getLastParagraph();

  // ==========================================================================

  /**
   * Sets the given paragraphs.
   * 
   * @param paragraphs
   *        The paragraphs.
   */
  void setParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds the given paragraphs.
   * 
   * @param paragraphs
   *        The paragraphs.
   */
  void addParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds the given paragraph.
   * 
   * @param paragraph
   *        The paragraph.
   */
  void addParagraph(PdfParagraph paragraph);
}
