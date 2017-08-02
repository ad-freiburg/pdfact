package icecite.tokenize.lines;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLineList;

/**
 * Identifies text lines in PDF pages.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineTokenizer {
  /**
   * Identifies text lines in the given PDF page.
   * 
   * @param pdf
   *        The PDF document to which the page belongs to.
   * @param page
   *        The PDF page to process.
   * 
   * @return The list of identified text lines.
   */
  PdfTextLineList tokenize(PdfDocument pdf, PdfPage page);
}
