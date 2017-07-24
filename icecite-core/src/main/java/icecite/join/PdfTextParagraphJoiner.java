package icecite.join;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;

/**
 * A tokenizer that joins split paragraphs of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextParagraphJoiner {
  /**
   * Joins the text blocks of the given PDF document to paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return The list of identified paragraphs.
   */
  List<PdfParagraph> join(PdfDocument pdf);
  
  /**
   * The factory to create instances of PdfTextParagraphJoiner.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTextParagraphJoinerFactory {
    /**
     * Creates a new PdfTextParagraphJoiner.
     * 
     * @return An instance of PdfTextParagraphJoiner.
     */
    PdfTextParagraphJoiner create();
  }
}
