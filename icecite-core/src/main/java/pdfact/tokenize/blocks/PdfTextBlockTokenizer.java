package pdfact.tokenize.blocks;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLineList;

/**
 * Identifies text blocks in PDF pages.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlockTokenizer {
  /**
   * Tokenizes the given PDF page into text blocks.
   * 
   * @param pdf
   *        The PDF document to which the page belongs to.
   * @param page
   *        The PDF page to process.
   * @param lines
   *        The lines to process.
   * 
   * @return The list of identified text blocks.
   */
  List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page,
      PdfTextLineList lines);
}
