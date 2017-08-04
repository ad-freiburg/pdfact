package icecite.tokenize.blocks;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLineList;

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
