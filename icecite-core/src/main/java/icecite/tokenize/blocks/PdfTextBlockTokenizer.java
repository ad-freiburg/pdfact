package icecite.tokenize.blocks;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;

/**
 * A tokenizer that tokenizes text lines into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextBlockTokenizer {
  /**
   * Tokenizes the given page into text blocks.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param page
   *        The page to process.
   *
   * @return The list of identified text blocks.
   */
  List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page);
}
