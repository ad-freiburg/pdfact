package pdfact.tokenize.words;

import pdfact.models.PdfCharacterList;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfWordList;

/**
 * Identifies words in PDF pages.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordTokenizer {
  /**
   * Tokenizes the given text line into words.
   * 
   * @param pdf
   *        The PDF document to which the page belongs to.
   * @param page
   *        The PDF page to process.
   * @param line 
   *        The characters of a text line to process.
   * 
   * @return The list of identified words.
   */
  PdfWordList tokenize(PdfDocument pdf, PdfPage page, PdfCharacterList line);
}
