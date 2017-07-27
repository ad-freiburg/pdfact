package icecite.tokenize.paragraphs;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;

/**
 * A tokenizer that identifies the paragraphs of PDF documents. This tokenizer
 * needs identified text blocks with identified semantics.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphTokenizer {
  /**
   * Tokenizes the given PDF document into paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> tokenize(PdfDocument pdf);
  
  /**
   * The factory to create instances of PdfTextParagraphTokenizer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfParagraphTokenizerFactory {
    /**
     * Creates a new PdfTextParagraphJoiner.
     * 
     * @return An instance of PdfTextParagraphJoiner.
     */
    PdfParagraphTokenizer create();
  }
}
