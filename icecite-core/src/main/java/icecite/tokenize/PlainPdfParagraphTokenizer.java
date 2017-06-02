package icecite.tokenize;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;

/**
 * A plain implementation of {@link PdfParagraphTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphTokenizer implements PdfParagraphTokenizer {
  /**
   * The factory to create instances of {@link PdfParagraph}.
   */
  protected PdfParagraphFactory paragraphFactory;

  // ==========================================================================

  /**
   * Creates a new {@link PlainPdfParagraphTokenizer}.
   * 
   * @param paragraphFactory
   *        The factory to create instances of {@link PdfParagraph}.
   */
  @Inject
  public PlainPdfParagraphTokenizer(PdfParagraphFactory paragraphFactory) {
    this.paragraphFactory = paragraphFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }
    
    List<PdfParagraph> paragraphs = new ArrayList<>();
    PdfParagraph currentParagraph = this.paragraphFactory.create();
    
    for (PdfPage page : pdf.getPages()) {
      for (PdfTextBlock block : page.getTextBlocks()) {
        if (block.getRole() != PdfRole.BODY_TEXT) {
          continue;
        }
        
        currentParagraph = this.paragraphFactory.create();
        for (PdfTextLine textLine : block.getTextLines()) {
          for (PdfWord word : textLine.getWords()) {
            currentParagraph.addWord(word);
          }
        }
        
        String text = CollectionUtils.join(currentParagraph.getWords(), " ");
        currentParagraph.setText(text);
        
        paragraphs.add(currentParagraph);
      }
    }
    
    return paragraphs;
  }

}
