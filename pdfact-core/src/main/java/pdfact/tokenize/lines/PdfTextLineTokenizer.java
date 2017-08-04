package pdfact.tokenize.lines;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfTextLineList;

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
