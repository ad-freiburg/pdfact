package icecite.join;

import icecite.models.PdfDocument;

/**
 * A class, that iterates through the text elements of PDF documents in order
 * to join text elements that belong logically together but are split by column
 * breaks, page breaks or other text elements (caption, headers, footer,
 * footnotes, etc.).
 * 
 * @author Claudius Korzen
 */
public interface PdfTextJoiner {
  /**
   * Joins split text elements that belongs logically together.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  void join(PdfDocument pdf);
  
  // ========================================================================== 
  
  /**
   * The factory to create instances of PdfTextJoiner.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextJoinerFactory {
    /**
     * Creates a new PdfTextJoiner.
     * 
     * @return An instance of PdfTextJoiner.
     */
    PdfTextJoiner create();
  }
}
