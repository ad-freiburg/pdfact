package pdfact.models;

/**
 * Any (visual) element in a PDF document, for example a character, a image, a
 * shape, a word, a text line, a text block, a paragraph, etc.
 * 
 * @author Claudius Korzen
 */
public interface PdfElement extends HasPosition {
  /**
   * Returns the feature of this PDF element.
   * 
   * @return The feature of this PDF element.
   */
  PdfTextUnit getTextUnit();
}
