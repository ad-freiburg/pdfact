package icecite.tokenizer;

import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfParagraphTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphTokenizer implements PdfParagraphTokenizer {
  /**
   * The factory to create instances of PdfParagraph.
   */
  protected PdfParagraphFactory paragraphFactory;

  /**
   * Creates a new tokenizer to that tokenizes text lines into paragrahps.
   * 
   * @param paragraphFactory
   *        The factory to create instances of PdfParagraph.
   */
  @Inject
  public PlainPdfParagraphTokenizer(PdfParagraphFactory paragraphFactory) {
    this.paragraphFactory = paragraphFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf, PdfPage page,
      List<PdfTextLine> textLines) {
    // TODO:
    PdfParagraph paragraph = this.paragraphFactory.create(textLines);
    return Arrays.asList(paragraph);
  }
}
