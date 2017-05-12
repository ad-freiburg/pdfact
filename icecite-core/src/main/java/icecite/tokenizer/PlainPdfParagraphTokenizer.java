package icecite.tokenizer;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfParagraphTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphTokenizer implements PdfParagraphTokenizer {

  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf, PdfPage page,
      List<PdfTextLine> textLines) {
    // TODO Auto-generated method stub
    return null;
  }

}
