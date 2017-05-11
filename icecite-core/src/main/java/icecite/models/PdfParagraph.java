package icecite.models;

import java.util.List;

/**
 * A text paragraph in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraph extends PdfElement, HasCharacters, HasText {
  /**
   * Returns the text lines of this paragraph.
   * 
   * @return The text lines of this paragraph.
   */
  List<PdfTextLine> getTextLines();

  /**
   * Sets the text lines of this paragraph.
   * 
   * @param textLines
   *        The text lines to set.
   */
  void setTextLines(List<PdfTextLine> textLines);

  /**
   * Adds the given text line to this paragraph.
   * 
   * @param textLine
   *        The text line to add.
   */
  void addTextLine(PdfTextLine textLine);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfParagraph}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfParagraphFactory {
    /**
     * Creates a PdfTextParagraph.
     * 
     * @param textLines
     *        The lines of the paragraph to create.
     * 
     * @return An instance of {@link PdfParagraph}.
     */
    PdfParagraph create(List<PdfTextLine> textLines);
  }
}
