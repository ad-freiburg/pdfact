package icecite.tokenize.paragraphs;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.utils.character.PdfCharacterUtils;
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

  // TODO: Iterate over the text blocks of the PDF document and join them to
  // paragraphs.
  // TODO: Compose the text of the paragraphs, including the dehyphenation of
  // words.
  
  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    List<PdfParagraph> paragraphs = new ArrayList<>();
    List<PdfTextBlock> textBlocks = pdf.getTextBlocks();
    
    for (int i = 0; i < textBlocks.size(); i++) {
      PdfTextBlock block = textBlocks.get(i);

      if (block.getParentPdfParagraph() != null) {
        // The block was already added to a paragraph. Ignore it.
        continue;
      }

      PdfParagraph paragraph = this.paragraphFactory.create();
      paragraph.addTextBlock(block);
      block.setParentPdfParagraph(paragraph);

      if (block.getRole() == PdfRole.BODY_TEXT) {
        for (int j = i + 1; j < textBlocks.size(); j++) {
          PdfTextBlock nextBlock = textBlocks.get(j);
          if (nextBlock.getRole() == PdfRole.BODY_TEXT) {
            if (shouldJoin(paragraph, nextBlock)) {
              paragraph.addTextBlock(nextBlock);
              nextBlock.setParentPdfParagraph(paragraph);
            } else {
              break;
            }
          }
        }
      }

      String text = CollectionUtils.join(paragraph.getTextBlocks(), " ");
      paragraph.setText(text);

      paragraphs.add(paragraph);
    }

    return paragraphs;
  }

  /**
   * TODO
   * 
   * @param paragraph sad
   * @param block sad
   * @return sad
   */
  protected boolean shouldJoin(PdfParagraph paragraph, PdfTextBlock block) {
    if (paragraph == null) {
      return false;
    }

    if (block == null) {
      return false;
    }

    if (!PdfCharacterUtils.isPunctuationMark(paragraph.getLastCharacter())) {
      return true;
    }

    if (PdfCharacterUtils.isLowercase(block.getFirstCharacter())) {
      return true;
    }

    return false;
  }

}
