package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has paragraphs.
 *
 * @author Claudius Korzen
 */
public interface HasParagraphs {
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
   * Adds the given paragraphs.
   * 
   * @param paragraphs
   *        The paragraphs to add.
   */
  void addParagraphs(List<PdfParagraph> paragraphs);

  /**
   * Adds the given paragraph to this page.
   * 
   * @param paragraph
   *        The paragraph to add.
   */
  void addParagraph(PdfParagraph paragraph);
}
