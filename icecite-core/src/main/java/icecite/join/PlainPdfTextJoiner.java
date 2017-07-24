package icecite.join;

import com.google.inject.Inject;

import icecite.join.PdfTextParagraphJoiner.PdfTextParagraphJoinerFactory;
import icecite.models.PdfDocument;

/**
 * A plain implementation of {@link PdfTextJoiner}.
 * 
 * @author Claudius Korzen
 *
 */
public class PlainPdfTextJoiner implements PdfTextJoiner {
  /**
   * The factory to create instances of {@link PdfTextParagraphJoiner}.
   */
  protected PdfTextParagraphJoinerFactory paragraphJoinerFactory;

  // ==========================================================================
  // The constructor.

  /**
   * Creates a new PlainPdfDocumentTokenizer.
   * 
   * @param paraJoinerFactory
   *        The factory to create instances of {@link PdfTextParagraphJoiner}.
   */
  @Inject
  public PlainPdfTextJoiner(PdfTextParagraphJoinerFactory paraJoinerFactory) {
    this.paragraphJoinerFactory = paraJoinerFactory;
  }

  // ==========================================================================

  @Override
  public void join(PdfDocument pdf) {
    pdf.setParagraphs(this.paragraphJoinerFactory.create().join(pdf));
  }
}
