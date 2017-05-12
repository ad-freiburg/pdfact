package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;

/**
 * A tokenizer that tokenizes text lines into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphTokenizer {
  /**
   * Tokenizes the given list of text lines into paragraphs.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The page in which the text lines are located.
   * @param textLines
   *        The text lines to tokenize.
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> tokenize(PdfDocument pdf, PdfPage page,
      List<PdfTextLine> textLines);
}
