package icecite.tokenize;

import com.google.inject.Inject;

import icecite.models.PdfDocument;

/**
 * A plain implementation of {@link PdfDocumentTokenizer}.
 * 
 * @author Claudius Korzen
 *
 */
public class PlainPdfDocumentTokenizer implements PdfDocumentTokenizer {
  /**
   * The paragraph tokenizer.
   */
  protected PdfParagraphTokenizer paragraphTokenizer;
  
  // ==========================================================================
  // The constructor.
  
  /**
   * Creates a new PlainPdfDocumentTokenizer.
   * 
   * @param paragraphTokenizer The paragraph tokenizer.
   */
  @Inject
  public PlainPdfDocumentTokenizer(PdfParagraphTokenizer paragraphTokenizer) {
    this.paragraphTokenizer = paragraphTokenizer;
  }
  
  // ==========================================================================
  
  @Override
  public void tokenizePdfDocument(PdfDocument pdf) {
    pdf.setParagraphs(this.paragraphTokenizer.tokenize(pdf));
  }
}
