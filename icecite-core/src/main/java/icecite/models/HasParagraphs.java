package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has paragraphs.
 *
 * @author Claudius Korzen
 */
public interface HasParagraphs {
  /**
   * Returns the paragraphs.
   * 
   * @return The paragraphs.
   */
  List<PdfParagraph> getParagraphs();

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
