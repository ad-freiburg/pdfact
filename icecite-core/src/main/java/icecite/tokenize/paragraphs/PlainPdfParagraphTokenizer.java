package icecite.tokenize.paragraphs;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator;
import icecite.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator.PdfWordDehyphenatorFactory;
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

  /**
   * The factory to create instances of {@link PdfWordDehyphenator}.
   */
  protected PdfWordDehyphenatorFactory dehyphenatorFactory;

  // ==========================================================================

  /**
   * Creates a new {@link PlainPdfParagraphTokenizer}.
   * 
   * @param paragraphFactory
   *        The factory to create instances of {@link PdfParagraph}.
   * @param dehyphenatorFactory
   *        The factory to create instances of {@link PdfWordDehyphenator}.
   */
  @Inject
  public PlainPdfParagraphTokenizer(PdfParagraphFactory paragraphFactory,
      PdfWordDehyphenatorFactory dehyphenatorFactory) {
    this.paragraphFactory = paragraphFactory;
    this.dehyphenatorFactory = dehyphenatorFactory;
  }

  // ==========================================================================

  // TODO: Compose the text of the paragraphs, including the dehyphenation of
  // words.

  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    List<PdfParagraph> paragraphs = identifyParagraphs(pdf);
    composeTexts(pdf, paragraphs);

    return paragraphs;
  }

  /**
   * Identifies the paragraphs in the given PDF document.
   * 
   * @param pdf
   *        The PDF document.
   * 
   * @return The list of identified paragraphs.
   */
  protected List<PdfParagraph> identifyParagraphs(PdfDocument pdf) {
    List<PdfParagraph> paragraphs = new ArrayList<>();

    // Put all blocks to a single list to be able to iterate them in one go.
    List<PdfTextBlock> textBlocks = new ArrayList<>();
    for (PdfPage page : pdf.getPages()) {
      textBlocks.addAll(page.getTextBlocks());
    }

    // Identify the paragraphs from the text blocks.
    for (int i = 0; i < textBlocks.size(); i++) {
      PdfTextBlock block = textBlocks.get(i);

      if (block.getParentPdfParagraph() != null) {
        // The block was already added to a paragraph. Ignore it.
        continue;
      }

      // Create a new paragraph.
      PdfParagraph paragraph = this.paragraphFactory.create();
      paragraph.addTextBlock(block);
      block.setParentPdfParagraph(paragraph);

      // If the role of the block is "body text", check if there is another
      // block in the remaining blocks that belongs to the same paragraph.
      if (block.getRole() == PdfRole.BODY_TEXT) {
        for (int j = i + 1; j < textBlocks.size(); j++) {
          PdfTextBlock otherBlock = textBlocks.get(j);
          if (otherBlock.getRole() != PdfRole.BODY_TEXT) {
            continue;
          }
          if (!belongsToParagraph(otherBlock, paragraph)) {
            break;
          }
          // Add the block to the existing paragraph.
          paragraph.addTextBlock(otherBlock);
          otherBlock.setParentPdfParagraph(paragraph);
        }
      }
      paragraphs.add(paragraph);
    }
    return paragraphs;
  }

  /**
   * Checks, if the given text block belongs to the given paragraph.
   * 
   * @param block
   *        The text block to process.
   * @param para
   *        The paragraph to process.
   * 
   * @return True, if the given text block should be added to the paragraph.
   */
  protected boolean belongsToParagraph(PdfTextBlock block, PdfParagraph para) {
    if (block == null || para == null) {
      return false;
    }

    // The block belongs to the paragraph, if the paragraph doesn't end with
    // a punctuation mark.
    PdfWord word = para.getLastTextBlock().getLastTextLine().getLastWord();
    PdfCharacter lastChar = word != null ? word.getLastCharacter() : null;
    if (!PdfCharacterUtils.isPunctuationMark(lastChar)) {
      return true;
    }

    // The block belongs to the paragraph, if the block starts with an
    // lowercased letter.
    PdfWord firstWord = block.getFirstTextLine().getFirstWord();
    if (PdfCharacterUtils.isLowercase(firstWord.getFirstCharacter())) {
      return true;
    }

    return false;
  }

  // ==========================================================================

  /**
   * Compose the texts for the paragraphs.
   * 
   * @param pdf
   *        The PDF document.
   * @param paragraphs
   *        The paragraphs to process.
   */
  protected void composeTexts(PdfDocument pdf, List<PdfParagraph> paragraphs) {
    PdfWordDehyphenator deyphenator = this.dehyphenatorFactory.create(pdf);

    for (PdfParagraph paragraph : pdf.getParagraphs()) {
      // Put all words to a single list to be able to iterate them in one go.
      List<PdfWord> words = new ArrayList<>();
      for (PdfTextBlock block : paragraph.getTextBlocks()) {
        for (PdfTextLine line : block.getTextLines()) {
          words.addAll(line.getWords());
        }
      }

      List<PdfWord> newWords = new ArrayList<>();
      for (int i = 0; i < words.size() - 1; i++) {
        PdfWord word = words.get(i);
        PdfWord nextWord = words.get(i + 1);

        if (word.isHyphenated()) {
          PdfWord dehyphenated = deyphenator.dehyphenate(word, nextWord);
          newWords.add(dehyphenated);
          i++;
        } else {
          newWords.add(word);
        }
      }
      // TODO: Don't forget the last word.
      newWords.add(words.get(words.size() - 1));
      paragraph.setText(CollectionUtils.join(newWords, " "));
    }
  }

}
